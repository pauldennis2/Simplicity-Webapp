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


    @RequestMapping(path = "/users.json", method = RequestMethod.GET)
    public LobbyUsersWrapper getUsers () {
        List<User> alphaUsers = users.findByLobbyStatus(LobbyStatus.ALPHA);
        List<User> bakerUsers = users.findByLobbyStatus(LobbyStatus.BAKER);
        List<User> charlieUsers = users.findByLobbyStatus(LobbyStatus.CHARLIE);
        List<User> deltaUsers = users.findByLobbyStatus(LobbyStatus.DELTA);
        List<User> mainLobbyUsers = users.findByLobbyStatus(LobbyStatus.MAIN);
        return new LobbyUsersWrapper(alphaUsers, bakerUsers, charlieUsers, deltaUsers, mainLobbyUsers);
    }

    @RequestMapping(path = "/my-user.json", method = RequestMethod.GET)
    public User getMyUser (HttpSession session) {
        return (User)session.getAttribute("user");
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

    @RequestMapping(path = "/new-empty-game.json", method = RequestMethod.POST)
    public Response newEmptyGame (@RequestBody IdRequestWrapper wrapper, HttpSession session) {
        Integer raceId = wrapper.getRaceId();
        AlienRace race;
        switch (raceId) {
            case 0:
                race = AlienRace.KITTY;
                break;
            case 1:
                race = AlienRace.DOGE;
                break;
            case 2:
                race = AlienRace.HORSIE;
                break;
            case 3:
                race = AlienRace.SSSNAKE;
                break;
            default:
                throw new AssertionError("Race id is bad");
        }

        double d = Math.random() * Math.PI + 239 * Math.random();

        StarSystemGraph ssgraph = new StarSystemGraph("4p_med_ring_map.txt");
        StarSystem homeSystem = ssgraph.findByName("P" + (1 + raceId) + " Home");

        Player player = new Player("Default Leader", race, homeSystem);

        List<Player> players = new ArrayList<>();
        players.add(player);

        Planet homePlanet = homeSystem.getPlanets().get(0);
        homePlanet.setOwnerRaceNum(raceId);
        homePlanet.setPopulation(8);
        player.addPlanet(homePlanet);
        ssgraph.setName("Map " + d);
        String name = "Game " + d;
        String imageString = "assets/ships/fighter/fighter_" + getColorFromRace(race) + ".png";
        player.addShip(new Starship(homeSystem, ShipChassis.FIGHTER, "Fighter", imageString, raceId));
        Game game = new Game(name, players, ssgraph);
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
        for (Starship ship : shipList) {
            if (ship.getTurnsToDestination() != null) {
                if (ship.getTurnsToDestination() > 0) {
                    shipList.remove(ship);
                }
            }
        }
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

    @RequestMapping(path = "/simple-diplomacy-info.json", method = RequestMethod.POST)
    public List<PlayerTemp> simpleDiplomacyInfo (@RequestBody IdRequestWrapper wrapper) {
        List<PlayerTemp> hardCodedList = new ArrayList<>();
        hardCodedList.add(new PlayerTemp(10, "Kitties", "assets/races/race1.jpg"));
        hardCodedList.add(new PlayerTemp(15, "Doges", "assets/races/race2.jpg"));
        hardCodedList.add(new PlayerTemp(0, "Horsies", "assets/races/race3.jpg"));
        hardCodedList.add(new PlayerTemp(1, "Sssnakesss", "assets/races/race4.jpg"));
        int total = 0;
        for (PlayerTemp playerTemp : hardCodedList) {
            total += playerTemp.getPopulation();
        }
        for (PlayerTemp playerTemp : hardCodedList) {
            playerTemp.calculatePercentageOfTotalPop((double) total);
        }
        return hardCodedList;
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
    public List<Planet> planetsInfo (@RequestBody IdRequestWrapper wrapper, HttpSession session) {
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

    @RequestMapping(path = "/sample-combat-info.json", method = RequestMethod.POST)
    public CombatInfoWrapper sampleCombatInfo () {
        List<Starship> friendShips = new ArrayList<>();
        List<Starship> enemyShips = new ArrayList<>();
        friendShips.add(new Starship(null, ShipChassis.DESTROYER, "Defiant", "gold-destroyer", null));
        friendShips.add(new Starship(null, ShipChassis.DESTROYER, "Valiant", "gold-destroyer", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Tempest", "purple-fighter", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Earthquake", "purple-fighter", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Hurricane", "purple-fighter", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Landslide", "purple-fighter", null));
        return new CombatInfoWrapper(friendShips, enemyShips);
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
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Tempest", "purple-fighter", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Earthquake", "purple-fighter", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Hurricane", "purple-fighter", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Landslide", "purple-fighter", null));

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
    public TurnInfoWrapper processTurn (HttpSession session) {
        Integer gameId = (Integer) session.getAttribute("gameId");
        Game game = games.findOne(gameId);
        List<Player> players = game.getPlayers();
        int productionAmt = 0;
        int researchAmt = 0;
        for (Player player : players) {
            List<Starship> shipList = player.getShips();
            for (Starship ship : shipList) {
                ship.moveToDestination();
            }

            for(Planet planet : player.getPlanets()) {
                productionAmt += planet.getProductionPct() * planet.getPopulation();
                researchAmt += planet.getResearchPct() * planet.getPopulation();
                planet.growPop();
            }
            player.setProductionPoolTotal(player.getProductionPoolTotal() + productionAmt);
            player.setResearchPoolTotal(player.getResearchPoolTotal() + researchAmt);
        }
        games.save(game);

        return new TurnInfoWrapper(researchAmt, productionAmt);
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
}
