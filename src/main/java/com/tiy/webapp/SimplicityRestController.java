package com.tiy.webapp;

import com.tiy.webapp.repos.*;
import com.tiy.webapp.starship.ShipChassis;
import com.tiy.webapp.starship.Starship;
import com.tiy.webapp.starsys.*;
import com.tiy.webapp.wrappers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Paul Dennis on 2/6/2017.
 */

@RestController
public class SimplicityRestController {

    @Autowired
    StarSystemRepo starSystems;

    @Autowired
    TunnelRepo tunnels;

    @Autowired
    ShipRepo ships;

    @Autowired
    PlanetRepo planets;

    @Autowired
    StarSystemGraphRepo ssGraphs;

    @Autowired
    GameRepo games;

    boolean initialized = false;

    @Autowired
    UserRepo users;

    @Autowired
    PlayerRepo players;

    public static final int NEW_PLANET_POP = 2;

    public static final int DEFAULT_STARTING_POP = 8;

    public static final String CAPITOL_SYMBOL = "\u235f";


    @RequestMapping(path = "/users.json", method = RequestMethod.GET)
    public LobbyUsersWrapper getUsers () {
        List<User> alphaUsers = users.findByLobbyStatus(LobbyStatus.ALPHA);
        List<User> bakerUsers = users.findByLobbyStatus(LobbyStatus.BAKER);
        List<User> charlieUsers = users.findByLobbyStatus(LobbyStatus.CHARLIE);
        List<User> deltaUsers = users.findByLobbyStatus(LobbyStatus.DELTA);
        List<User> mainLobbyUsers = users.findByLobbyStatus(LobbyStatus.MAIN);
        return new LobbyUsersWrapper(alphaUsers, bakerUsers, charlieUsers, deltaUsers, mainLobbyUsers);
    }

    @RequestMapping(path = "/open-savedGames.json", method= RequestMethod.GET)
    public List<Game> openGames () {
        return games.findByJustStarted(true);
    }

    @RequestMapping(path = "/my-user.json", method = RequestMethod.GET)
    public User getMyUser (HttpSession session) {
        User user = (User)session.getAttribute("user");
        return user;
    }

    @RequestMapping(path = "/game-id.json", method = RequestMethod.GET)
    public Integer getGameId (HttpSession session) {
        return (Integer) session.getAttribute("gameId");
    }

    @RequestMapping(path = "/change-users-lobby.json", method = RequestMethod.POST)
    public Response changeUsersLobby (@RequestBody Integer lobby, HttpSession session) {
        LobbyStatus newStatus = LobbyStatus.values()[lobby];
        User user = (User)session.getAttribute("user");
        user.setLobbyStatus(newStatus);
        users.save(user);
        return new Response (true);
    }

    @RequestMapping(path = "/user-registration.json", method = RequestMethod.POST)
    public Response registration (@RequestBody User user) {
        if (user != null) {
            users.save(user);
            return new Response(true);
        }
        return new Response(false);
    }

    @RequestMapping(path = "/new-multiplayer-game.json", method = RequestMethod.POST)
    public Response newMultiGame (@RequestBody IdRequestWrapper wrapper, HttpSession session) {
        Integer raceId = wrapper.getRaceId();
        AlienRace race = getRaceFromId(raceId);

        String firstFiveRandom = "";
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            firstFiveRandom += "" + random.nextInt(10);
        }

        StarSystemGraph ssgraph = new StarSystemGraph("4p_med_ring_map.txt");
        StarSystem homeSystem = ssgraph.findByName("P" + (1 + getRaceId(race)) + " Home");
        User user = (User) session.getAttribute("user");
        Player player = new Player (user.getHandle(), race, homeSystem);

        List<Player> playerList = new ArrayList<>();
        playerList.add(player);

        Planet homePlanet = homeSystem.getPlanets().get(0);
        homePlanet.setOwnerRaceNum(getRaceId(race));
        homePlanet.setPopulation(DEFAULT_STARTING_POP);
        player.addPlanet(homePlanet);

        ssgraph.setName("Map " + firstFiveRandom);
        String name = user.getHandle() + ":" + wrapper.getLobbyName() + firstFiveRandom;

        String imageString = "assets/ships/fighter/fighter_" + getColorFromRace(race) + ".png";
        player.addShip(new Starship(homeSystem, ShipChassis.FIGHTER, "Fighter", imageString, raceId));

        Game game = new Game(name, playerList, ssgraph);

        games.save(game);
        session.setAttribute("gameId", game.getId());
        session.setAttribute("playerId", player.getId());
        return new Response(true, game.getId());
    }

    @RequestMapping(path = "/load-game.json", method = RequestMethod.POST)
    public Response loadGame (@RequestBody IdRequestWrapper wrapper, HttpSession session) {
        Integer gameId = wrapper.getGameId();
        User user = (User)session.getAttribute("user");
        Game game = games.findOne(gameId);
        if (game != null) {
            Integer playerId = game.getPlayers().get(0).getId();
            session.setAttribute("playerId", playerId);
            session.setAttribute("gameId", gameId);
            return new Response(true);
        }
        return new Response(false);
    }

    @RequestMapping (path = "/save-game.json", method = RequestMethod.POST)
    public Response saveGame (HttpSession session) {
        Integer gameId = (Integer) session.getAttribute("gameId");
        User user = (User) session.getAttribute("user");
        user.addSaveGameId(gameId);
        users.save(user);
        return new Response(true);
    }

    @RequestMapping (path =  "/get-saved-game-ids.json", method = RequestMethod.POST)
    public Set<Integer> getSavedGames (HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user.getSavedGameIds();
    }

    @RequestMapping(path = "/join-multiplayer-game.json", method = RequestMethod.POST)
    public Response joinMultiGame (@RequestBody IdRequestWrapper wrapper, HttpSession session) {
        Integer gameId = wrapper.getGameId();
        Game game = games.findOne(gameId);
        Integer raceId = wrapper.getRaceId();
        AlienRace race = getRaceFromId(raceId);
        if (game == null) {
            throw new AssertionError("Game is null");
        }
        if (!game.getJustStarted()) {
            throw new AssertionError("Game did not just start. Cannot join an in-progress game");
        }
        List<Player> playerList = game.getPlayers();

        //Code to check if the desired race is taken; if so, find the first available race
        Response response = new Response(true);
        boolean newRaceNeeded = false;
        for (Player player : playerList) {
            if (player.getRace() == race) {
                response.setMessage("Race " + race + " was already taken by " + player.getName()
                        + " (sorry). You were assigned a different race.");
                newRaceNeeded = true;
            }
        }
        if (newRaceNeeded) {
            List<AlienRace> possibleRaces = new ArrayList<>();
            for (AlienRace race2 : AlienRace.values()) {
                possibleRaces.add(race2);
            }
            for (Player player : playerList) {
                possibleRaces.remove(player.getRace());
            }
            race = possibleRaces.get(0);
        }
        StarSystemGraph ssgraph = game.getStarSystemGraph();

        StarSystem homeSystem = ssgraph.findByName("P" + (1 + getRaceId(race)) + " Home");

        Planet homePlanet = homeSystem.getPlanets().get(0);
        homePlanet.setOwnerRaceNum(getRaceId(race));
        homePlanet.setPopulation(DEFAULT_STARTING_POP);
        User user = (User) session.getAttribute("user");
        Player player = new Player (user.getHandle(), race, homeSystem);
        String imageString = "assets/ships/fighter/fighter_" + getColorFromRace(race) + ".png";
        player.addShip(new Starship(homeSystem, ShipChassis.FIGHTER, "Fighter", imageString, raceId));
        player.addPlanet(homePlanet);

        players.save(player);

        game.getPlayers().add(player);

        games.save(game);
        System.out.println("Player id = " + player.getId());
        session.setAttribute("gameId", game.getId());
        session.setAttribute("playerId", player.getId());

        Integer retrievedId = (Integer)session.getAttribute("playerId");

        System.out.println("retrievedId = " + retrievedId);

        response.setId(game.getId());
        return response;
    }

    @RequestMapping(path = "/new-empty-game.json", method = RequestMethod.POST)
    public Response newEmptyGame (@RequestBody IdRequestWrapper wrapper, HttpSession session) {
        Integer raceId = wrapper.getRaceId();
        AlienRace race = getRaceFromId(raceId);

        double d = Math.random() * Math.PI + 239 * Math.random();
        //This is to give the game a unique name without picking one and without trial/error

        StarSystemGraph ssgraph = new StarSystemGraph("4p_med_ring_map.txt");
        StarSystem homeSystem = ssgraph.findByName("P" + (1 + raceId) + " Home");
        User user = (User) session.getAttribute("user");
        Player player = new Player(user.getHandle(), race, homeSystem);

        List<Player> playerList = new ArrayList<>();
        playerList.add(player);

        Planet homePlanet = homeSystem.getPlanets().get(0);
        homePlanet.setOwnerRaceNum(raceId);
        homePlanet.setPopulation(DEFAULT_STARTING_POP);
        homePlanet.setName(player.getName() + " Home " + CAPITOL_SYMBOL);
        player.addPlanet(homePlanet);
        ssgraph.setName("Map " + d);
        String name = "Game " + d;
        String imageString = "assets/ships/fighter/fighter_" + getColorFromRace(race) + ".png";
        player.addShip(new Starship(homeSystem, ShipChassis.FIGHTER, "Fighter", imageString, raceId));
        Game game = new Game(name, playerList, ssgraph);
        games.save(game);
        session.setAttribute("gameId", game.getId());
        session.setAttribute("playerId", player.getId());
        return new Response(true, game.getId());
    }

    @RequestMapping(path = "/ssg-info.json", method = RequestMethod.POST)
    public StarSystemGraph ssgInfo (HttpSession session) {
        Integer gameId = (Integer) session.getAttribute("gameId");
        return games.findOne(gameId).getStarSystemGraph();
    }

    @RequestMapping(path = "/specific-system-info.json", method = RequestMethod.POST)
    public StarSystemInfoWrapper specificSystemInfo (@RequestBody IdRequestWrapper wrapper) {
        Integer systemId = wrapper.getSystemId();
        if (systemId == null) {
            throw new AssertionError("Slow your roll there speedster");
        }
        StarSystem starSystem = starSystems.findOne(systemId);
        List<Starship> shipList = ships.findByStarSystem(starSystem);

        for (int i = 0; i < shipList.size(); i++) {
            Starship ship = shipList.get(i);
            if (ship.getTurnsToDestination() != null) {
                if (ship.getTurnsToDestination() > 0) {
                    shipList.remove(i);
                    i--;
                }
            }
        }

        /*for (Starship ship : shipList) {
            if (ship.getTurnsToDestination() != null) {
                if (ship.getTurnsToDestination() > 0) {
                    shipList.remove(ship);
                }
            }
        }*/
        List<SpaceTunnel> spaceTunnels = tunnels.findByFirstSystem(starSystem);
        spaceTunnels.addAll(tunnels.findBySecondSystem(starSystem));
        return new StarSystemInfoWrapper(starSystem, shipList, spaceTunnels);
    }

    @RequestMapping(path = "/research-info.json", method = RequestMethod.POST)
    public Player researchInfo (HttpSession session) {
        Integer playerId = (Integer) session.getAttribute("playerId");
        Player player = players.findOne(playerId);
        return player;
    }

    @RequestMapping(path = "/diplomacy-info.json", method = RequestMethod.POST)
    public List<Player> diplomacyInfo (HttpSession session) {
        Integer gameId = (Integer) session.getAttribute("gameId");
        List<Player> players = games.findOne(gameId).getPlayers();
        int totalPop = 0;
        for (Player player : players) {
            int population = 0;
            for (Planet planet : player.getPlanets()) {
                population += planet.getPopulation();
            }
            player.setPopulation(population);
            totalPop += population;

            switch (player.getRace()) {
                case KITTY:
                    player.setImageFile("assets/races/race1.jpg");
                    break;
                case DOGE:
                    player.setImageFile("assets/races/race2.jpg");
                    break;
                case HORSIE:
                    player.setImageFile("assets/races/race3.jpg");
                    break;
                case SSSNAKE:
                    player.setImageFile("assets/races/race4.jpg");
                    break;
                default:
                    throw new AssertionError("Race not recognized");
            }
            if (population == 0) {
                player.setImageFile(player.getImageFile().substring(0, 18) + "_extinct.jpg");
            }
        }
        for (Player player : players) {
            int pop = player.getPopulation();
            double pct = (double)pop / (double) totalPop;
            player.setPercentOfTotalPop(pct);
        }
        return players;
    }

    @RequestMapping(path = "/ships-info.json", method = RequestMethod.POST)
    public List<Starship> shipsInfo (HttpSession session) {
        Integer playerId = (Integer) session.getAttribute("playerId");
        return players.findOne(playerId).getShips();
    }

    @RequestMapping(path = "/process-attack.json", method = RequestMethod.POST)
    public Starship processAttack (@RequestBody Starship starship) {
        starship.takeDamage(starship.getDamage());
        return starship;
    }

    @RequestMapping(path = "/planets-info.json", method = RequestMethod.POST)
    public List<Planet> planetsInfo (HttpSession session) {
        Integer playerId = (Integer) session.getAttribute("playerId");
        return players.findOne(playerId).getPlanets();
    }

    @RequestMapping(path = "/end-turn.json", method = RequestMethod.POST)
    public Response endTurn () {
        return null;
    }

    @RequestMapping(path = "/colonize-planet.json", method = RequestMethod.POST)
    public Response colonizePlanet (@RequestBody IdRequestWrapper wrapper, HttpSession session) {
        Integer shipId = wrapper.getShipId();
        Integer planetId = wrapper.getPlanetId();
        Integer playerId = (Integer) session.getAttribute("playerId");

        Starship ship = ships.findOne(shipId);
        Planet planet = planets.findOne(planetId);
        Player player = players.findOne(playerId);

        //PERMISSIONS EXAMPLE
        if (ship.getOwnerRaceNum() == getRaceId(player.getRace())) {

            Integer raceId = getRaceId(player.getRace());

            if (ship == null || player == null || planet == null) {
                throw new AssertionError("one is null");
            }
            if (ship.getChassis() != ShipChassis.COLONIZER) {
                throw new AssertionError("Not a colonizer");
            }
            if (planet.getOwnerRaceNum() > -1) {
                throw new AssertionError("planet already owned");
            }
            planet.setPopulation(NEW_PLANET_POP);
            planet.setOwnerRaceNum(raceId);
            player.addPlanet(planet);
            player.removeShip(ship);
            ships.delete(ship);
            players.save(player);
            return new Response(true);
        }
        Response response = new Response(false);
        response.setMessage("Not your ship.");
        return response;
    }

    @RequestMapping (path = "/empty-combat-info.json", method = RequestMethod.POST)
    public CombatInfoWrapper emptyCombatInfo (@RequestBody IdRequestWrapper wrapper, HttpSession session) {
        Integer playerId = (Integer) session.getAttribute("playerId");
        Player player = players.findOne(playerId);
        Integer systemId = wrapper.getSystemId();
        StarSystem starSystem = starSystems.findOne(systemId);
        List<Starship> shipsInSystem = ships.findByStarSystem(starSystem);
        List<Starship> playersShips = player.getShips();
        List<Starship> playersShipsInSystem = getOverlap(shipsInSystem, playersShips);
        /*for (Starship ship : playersShipsInSystem) {
            if (ship.getChassis() == ShipChassis.COLONIZER) {
                playersShipsInSystem.remove(ship);
            }
        }*/
        for (int index = 0; index < playersShipsInSystem.size(); index++) {
            if (playersShipsInSystem.get(index).getChassis() == ShipChassis.COLONIZER) {
                playersShipsInSystem.remove(index);
                index--;
            }
        }

        Collections.sort(playersShipsInSystem);

        List<Starship> enemyShips = new ArrayList<>();
        enemyShips.add(new Starship(null, ShipChassis.CRUISER, "Thunder", "purple-cruiser", null));
        enemyShips.add(new Starship(null, ShipChassis.DESTROYER, "Tempest", "purple-destroyer", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Earthquake", "purple-fighter", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Hurricane", "purple-fighter", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Landslide", "purple-fighter", null));

        enemyShips.add(new Starship(null, ShipChassis.DESTROYER, "Nerevar", "purple-destroyer", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Ayleid", "purple-fighter", null));

        Collections.sort(enemyShips);
        return new CombatInfoWrapper(playersShipsInSystem, enemyShips);
    }

    @RequestMapping(path = "/scrap-ship.json", method = RequestMethod.POST)
    public Response scrapShip (@RequestBody IdRequestWrapper wrapper, HttpSession session) {
        Integer shipId = wrapper.getShipId();
        Integer playerId = (Integer) session.getAttribute("playerId");
        Player player = players.findOne(playerId);
        Starship ship = ships.findOne(shipId);
        if (player == null || ship == null) {
            throw new AssertionError("Neither player nor ship can be null to scrap ship");
        }
        player.removeShip(ship);
        ships.delete(ship);
        players.save(player);
        return new Response(true);
    }

    @RequestMapping(path = "/enter-tunnel.json", method = RequestMethod.POST)
    public Response enterTunnel (@RequestBody IdRequestWrapper wrapper) {
        Integer shipId = wrapper.getShipId();
        Integer destinationId = wrapper.getSystemId();
        Integer tunnelId = wrapper.getTunnelId();
        SpaceTunnel tunnel = tunnels.findOne(tunnelId);
        if (tunnel == null) {
            throw new AssertionError("Tunnel cannot be null");
        }
        StarSystem destination = starSystems.findOne(destinationId);
        if (destination == null) {
            throw new AssertionError("Destination cannot be null");
        }
        Starship ship = ships.findOne(shipId);
        if (ship == null) {
            throw new AssertionError("Ship cannot be null");
        }

        ship.setStarSystem(destination);
        ship.setTurnsToDestination(tunnel.getLength());
        ships.save(ship);

        return new Response(true);
    }

    @RequestMapping(path = "/change-output.json", method = RequestMethod.POST)
    public Response changeOutput (@RequestBody IdRequestWrapper wrapper) {
        Integer planetId = wrapper.getPlanetId();
        Planet planet = planets.findOne(planetId);
        Integer productionNumber = wrapper.getProductionNumber();
        if (planet == null) {
            throw new AssertionError("Planet cannot be null");
        }
        planet.setProduction(productionNumber);
        planets.save(planet);
        return new Response(true);
    }

    @RequestMapping(path = "/research-tech.json", method = RequestMethod.POST)
    public Player researchTech (@RequestBody IdRequestWrapper wrapper, HttpSession session) {
        Integer techId = wrapper.getTechId();
        Integer playerId = (Integer) session.getAttribute("playerId");
        Player player = players.findOne(playerId);
        if (techId == 0) {
            if (player.getFirstTechResearched()) {
                throw new AssertionError("already researched");
            }
            if (player.getResearchPoolTotal() < 40) {
                throw new AssertionError("not enough rp");
            }
            player.setFirstTechResearched(true);
            player.setResearchPoolTotal(player.getResearchPoolTotal() - 40);
        }
        if (techId == 1) {
            if (player.getSecondTechResearched()) {
                throw new AssertionError("already researched");
            }
            if (player.getResearchPoolTotal() < 50) {
                throw new AssertionError("not enough rp");
            }
            player.setSecondTechResearched(true);
            player.setResearchPoolTotal(player.getResearchPoolTotal() - 50);
        }
        if (techId == 2) {
            if (player.getCruiserTechResearched()) {
                throw new AssertionError("already researched");
            }
            if (player.getResearchPoolTotal() < 90) {
                throw new AssertionError("not enough rp");
            }
            player.setCruiserTechResearched(true);
            player.setResearchPoolTotal(player.getResearchPoolTotal() - 90);
        }
        players.save(player);
        return player;
    }

    @RequestMapping(path = "/create-ship.json", method = RequestMethod.POST)
    public Starship createShip (@RequestBody Starship shipWrapper, HttpSession session) {
        Integer playerId = (Integer) session.getAttribute("playerId");
        Player player = players.findOne(playerId);
        Integer raceId = getRaceId(player.getRace());
        Integer productionAvailable = player.getProductionPoolTotal();
        if (productionAvailable >= shipWrapper.getChassis().getBaseProductionCost()) {
            player.setProductionPoolTotal(player.getProductionPoolTotal() - shipWrapper.getChassis().getBaseProductionCost());
            ShipChassis chassis = shipWrapper.getChassis();
            String imageString = "assets/ships/" + chassis.toString().toLowerCase() + "/" + chassis.toString().toLowerCase() + "_";
            imageString += getColorFromRaceId(raceId) + ".png";
            Starship createdShip = new Starship(player.getHomeSystem(), shipWrapper.getChassis(), shipWrapper.getName(),
                    imageString, raceId);
            player.addShip(createdShip);
            players.save(player);
            return createdShip;
        }
        return null;
    }

    @RequestMapping(path = "/get-turn-number.json", method = RequestMethod.POST)
    public Integer getTurnNumber (HttpSession session) {
        Integer gameId = (Integer) session.getAttribute("gameId");
        return games.findOne(gameId).getTurnNumber();
    }

    @RequestMapping(path = "/shipyard-info.json", method = RequestMethod.POST)
    public ShipyardInfoWrapper shipyardInfo (HttpSession session) {
        Integer playerId = (Integer) session.getAttribute("playerId");
        Player player = players.findOne(playerId);
        if (player == null) {
            throw new AssertionError("whats the happy haps?");
        }
        System.out.println("Player = " + player);
        return new ShipyardInfoWrapper(player.getProductionPoolTotal(), player);
    }

    @RequestMapping(path = "/return-combat-result.json", method = RequestMethod.POST)
    public Response returnCombatResult () {
        return null;
    }

    @RequestMapping(path = "/process-turn.json", method = RequestMethod.POST)
    public Integer processTurn (HttpSession session) {
        Integer playerId = (Integer) session.getAttribute("playerId");
        Player currentPlayer = players.findOne(playerId);
        currentPlayer.setTurnCommitted(true);
        players.save(currentPlayer);
        Integer gameId = (Integer) session.getAttribute("gameId");
        Game game = games.findOne(gameId);
        List<Player> playerList = game.getPlayers();

        boolean allPlayersDone = true;
        for (Player player : playerList) {
            if (!player.getTurnCommitted()) {
                allPlayersDone = false;
            }
        }

        if (allPlayersDone) {
            game.incrementTurn();

            for (Player player : playerList) {
                player.setTurnCommitted(false);
                List<Starship> shipList = player.getShips();
                for (Starship ship : shipList) {
                    ship.moveToDestination();
                }

                int productionAmt = 0;
                int researchAmt = 0;
                for (Planet planet : player.getPlanets()) {
                    productionAmt += planet.getProductionPct() * planet.getPopulation();
                    researchAmt += planet.getResearchPct() * planet.getPopulation();
                    planet.growPop();
                }
                player.setProductionPoolTotal(player.getProductionPoolTotal() + productionAmt);
                player.setResearchPoolTotal(player.getResearchPoolTotal() + researchAmt);
            }
            games.save(game);

            return game.getTurnNumber();
        }
        return game.getTurnNumber();
    }


    public static Integer getRaceId (AlienRace race) {
        switch (race) {
            case KITTY:
                return 0;
            case DOGE:
                return 1;
            case HORSIE:
                return 2;
            case SSSNAKE:
                return 3;
        }
        return -1;
    }

    public static String getColorFromRaceId (Integer raceId) {
        switch (raceId) {
            case 0:
                return "ltblue";
            case 1:
                return "red";
            case 2:
                return "gold";
            case 3:
                return "green";
        }
        return null;
    }

    public static String getColorFromRace (AlienRace race) {
        return getColorFromRaceId(getRaceId(race));
    }


    public static List<Starship> getOverlap (List<Starship> firstList, List<Starship> secondList) {
        List<Starship> overlap = new ArrayList<>();

        for (Starship ship : firstList) {
            if (secondList.contains(ship)) {
                overlap.add(ship);
            }
        }

        return overlap;
    }

    public static AlienRace getRaceFromId (Integer raceId) {
        switch (raceId) {
            case -1:
                System.out.println("No race selected. Defaulting to first");
                return AlienRace.KITTY;
            case 0:
                return AlienRace.KITTY;
            case 1:
                return AlienRace.DOGE;
            case 2:
                return AlienRace.HORSIE;
            case 3:
                return AlienRace.SSSNAKE;
            default:
                throw new AssertionError("Race id is bad");
        }
    }
}
