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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        player.addShip(new Starship(homeSystem, ShipChassis.FIGHTER, "Fighter", "assets/ships/destroyerblue.png", raceId));
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
    public Response researchInfo (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        return null;
    }

    @RequestMapping(path = "/diplomacy-info.json", method = RequestMethod.POST)
    public List<PlayerTemp> diplomacyInfo (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
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

    @RequestMapping(path = "/combat-info.json", method = RequestMethod.POST)
    public CombatInfoWrapper combatInfo () {
        List<Starship> friendShips = new ArrayList<>();
        List<Starship> enemyShips = new ArrayList<>();
        friendShips.add(new Starship(null, ShipChassis.DESTROYER, "Defiant", "destroyer", null));
        friendShips.add(new Starship(null, ShipChassis.DESTROYER, "Valiant", "destroyer", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Tempest", "enemy", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Earthquake", "enemy", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Hurricane", "enemy", null));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Landslide", "enemy", null));
        return new CombatInfoWrapper(friendShips, enemyShips);
    }

    @RequestMapping(path = "/scrap-ship.json", method = RequestMethod.POST)
    public Response scrapShip (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        Integer shipId = wrapper.getShipId();
        return null;
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
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        Integer planetId = wrapper.getPlanetId();
        return null;
    }

    @RequestMapping(path = "/change-research.json", method = RequestMethod.POST)
    public Response changeResearch (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        Integer techId = wrapper.getTechId();
        return null;
    }

    @RequestMapping(path = "/create-ship.json", method = RequestMethod.POST)
    public Starship createShip (@RequestBody Starship ship, HttpSession session) {
        Integer playerId = (Integer) session.getAttribute("playerId");
        Player player = players.findOne(playerId);
        Integer raceId = getRaceId(player.getRace());
        Integer productionAvailable = player.getProductionPoolTotal();
        if (productionAvailable >= ship.getChassis().getBaseProductionCost()) {
            player.setProductionPoolTotal(player.getProductionPoolTotal() - ship.getChassis().getBaseProductionCost());
            Starship createdShip = new Starship(player.getHomeSystem(), ship.getChassis(), ship.getName(), "assets/ships/destroyerblue.png", raceId);
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

        return new ShipyardInfoWrapper(player.getProductionPoolTotal());
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
}
