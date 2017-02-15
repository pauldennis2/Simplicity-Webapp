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

    /*@RequestMapping(path = "/user-login.json", method = RequestMethod.POST)
    public Response login (@RequestBody User user) {
        User retrievedUser = users.findFirstByEmail(user.getEmail());
        if (retrievedUser != null) {
            if (retrievedUser.getPassword().equals(user.getPassword())) {
                usersInSession.add(retrievedUser);
                return new Response(true);
            }
        }
        return new Response(false);
    }*/

    @RequestMapping(path = "/user-registration.json", method = RequestMethod.POST)
    public Response registration (@RequestBody User user) {
        if (user != null) {
            users.save(user);
            return new Response(true);
        }
        return new Response(false);
    }

    @RequestMapping(path = "/new-empty-game.json", method = RequestMethod.POST)
    public Response newEmptyGame (@RequestBody IdRequestWrapper wrapper) {
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
        Player player = new Player("Default Leader", race);

        List<Player> players = new ArrayList<>();
        players.add(player);
        double d = Math.random() * Math.PI + 239 * Math.random();



        StarSystemGraph ssgraph = new StarSystemGraph("4p_med_ring_map.txt");
        StarSystem homeSystem = ssgraph.findByName("P" + (1 + raceId) + " Home");
        Planet homePlanet = homeSystem.getPlanets().get(0);
        homePlanet.setOwnerRaceNum(raceId);
        homePlanet.setPopulation(8);
        ssgraph.setName("Map " + d);
        String name = "Game " + d;
        player.addShip(new Starship( homeSystem, ShipChassis.FIGHTER, "Fighter", "default"));
        Game game = new Game(name, players, ssgraph);
        games.save(game);

        return new Response(true, game.getId());
    }

    /*boolean ssgInit = false;
    StarSystemGraphWrapper ssg;
    @RequestMapping(path = "/ssg-main-info.json", method = RequestMethod.POST)
    public StarSystemGraphWrapper ssgMainInfo (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        if (!ssgInit) {
            initializeSsg();
            ssgInit = true;
        }
        return ssg;
    }*/

    @RequestMapping(path = "/ssg-info.json", method = RequestMethod.POST)
    public StarSystemGraph ssgInfo (@RequestBody IdRequestWrapper wrapper) {
        StarSystemGraph deltaQuadrant = ssGraphs.findFirstByName("Delta Quadrant");
        if (deltaQuadrant == null) {
            deltaQuadrant = new StarSystemGraph("4p_med_ring_map.txt");
            deltaQuadrant.setName("Delta Quadrant");
            ssGraphs.save(deltaQuadrant);
        }
        return deltaQuadrant;
    }

    @RequestMapping(path = "/system-info.json", method = RequestMethod.POST)
    public StarSystemInfoWrapper systemInfo (@RequestBody IdRequestWrapper wrapper) {
        initializeSystemInfo();
        //Integer gameId = wrapper.getGameId();
        //Integer playerId = wrapper.getPlayerId();
        //Integer systemId = wrapper.getSystemId();
        /*StarSystem hardCodedSystem = new StarSystem();
        List<SpaceTunnel> tunnels = new ArrayList<>();
        tunnels.add(new SpaceTunnel());
        tunnels.add(new SpaceTunnel());
        List<Planet> planets = new ArrayList<>();
        planets.add(new Planet("Awesome I", 10, "earth"));
        planets.add(new Planet("Awesome II", 8, "zebulon"));
        hardCodedSystem.setPlanets(planets);
        List<Starship> ships = new ArrayList<>();
        ships.add(new Starship(null, ShipChassis.DESTROYER, "Enterprise"));
        ships.add(new Starship(null, ShipChassis.FIGHTER, "Voyager"));
        return new StarSystemInfoWrapper(hardCodedSystem, ships, null);*/
        /*User user = (User) session.getAttribute("user");
        if (user == null) {
            //throw new AssertionError("Must be logged in to view content");
        }*/
        StarSystem zebulon = starSystems.findFirstByName("Zebulon");
        //List<SpaceTunnel> zebulonTunnels = tunnels.findByFirstSystemOrSecondSystem(zebulon);
        List<SpaceTunnel> zebulonTunnels = new ArrayList<>();
        zebulonTunnels.add(tunnels.findFirstByName("Avalon - Zebulon"));
        List<Starship> ships = new ArrayList<>();
        ships.add(new Starship(null, ShipChassis.DESTROYER, "Enterprise", "default"));
        ships.add(new Starship(null, ShipChassis.FIGHTER, "Voyager", "default"));
        return new StarSystemInfoWrapper(zebulon, ships, zebulonTunnels);
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
        Random random = new Random();
        for (Planet planet : starSystem.getPlanets()) {
            if (random.nextBoolean()) {
                planet.setOwnerRaceNum(-1);
            } else {
                planet.setOwnerRaceNum(random.nextInt(4));
            }
            //todo fix to actually set correctly
        }
        List<SpaceTunnel> spaceTunnels = tunnels.findByFirstSystem(starSystem);
        spaceTunnels.addAll(tunnels.findBySecondSystem(starSystem));
        return new StarSystemInfoWrapper(starSystem, shipList, spaceTunnels);
    }

    public void initializeTunnels () {
        StarSystem zebulon = starSystems.findFirstByName("Zebulon");
        StarSystem avalon = starSystems.findFirstByName("Avalon");
        SpaceTunnel avazeb = tunnels.findFirstByName("Avalon - Zebulon");
        if (avazeb == null) {
            avazeb = new SpaceTunnel(2, avalon, zebulon);
            tunnels.save(avazeb);
        }
    }

    @RequestMapping(path = "/simple-system-info.json", method = RequestMethod.POST)
    public StarSystemTemp simpleInfo (@RequestBody IdRequestWrapper wrapper) {
        List<Ship> ships = new ArrayList<>();
        ships.add(new Ship("Enterprise", 300, 300, null, null));
        ships.add(new Ship("Voyager", 180, 180, null, null));
        return new StarSystemTemp(2, 2, ships);
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
    public List<Ship> shipsInfo (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();

        List<Ship> hardCodedList = new ArrayList<>();
        hardCodedList.add(new Ship("Defiant", 25, 30, "assets/ships/fighterblue.png", "Zebulon System"));
        hardCodedList.add(new Ship("Voyager", 100, 100, "assets/ships/destroyerblue.png", "Zebulon System"));
        hardCodedList.add(new Ship("Aegis", 30, 30, "assets/ships/fighterblue.png", "Terran System"));

        if (false) {
            hardCodedList.add(new Ship("Defiant", 25, 30, "assets/ships/fighterblue.png", "Zebulon System"));
            hardCodedList.add(new Ship("Voyager", 100, 100, "assets/ships/destroyerblue.png", "Zebulon System"));
            hardCodedList.add(new Ship("Aegis", 30, 30, "assets/ships/fighterblue.png", "Terran System"));
            hardCodedList.add(new Ship("Defiant", 25, 30, "assets/ships/fighterblue.png", "Zebulon System"));
            hardCodedList.add(new Ship("Voyager", 100, 100, "assets/ships/destroyerblue.png", "Zebulon System"));
            hardCodedList.add(new Ship("Aegis", 30, 30, "assets/ships/fighterblue.png", "Terran System"));
            hardCodedList.add(new Ship("Defiant", 25, 30, "assets/ships/fighterblue.png", "Zebulon System"));
            hardCodedList.add(new Ship("Voyager", 100, 100, "assets/ships/destroyerblue.png", "Zebulon System"));
            hardCodedList.add(new Ship("Aegis", 30, 30, "assets/ships/fighterblue.png", "Terran System"));
        }
        return hardCodedList;
    }

    @RequestMapping(path = "/process-attack.json", method = RequestMethod.POST)
    public Starship processAttack (@RequestBody Starship starship) {
        starship.takeDamage(starship.getDamage());
        return starship;
    }

    @RequestMapping(path = "/planets-info.json", method = RequestMethod.POST)
    public List<Planet> planetsInfo (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        List<Planet> hardCodedList = new ArrayList<>();
        hardCodedList.add(new Planet("Earth", 12, "assets/planets/earth.png"));
        hardCodedList.add(new Planet("Zebulon IV", 5, "assets/planets/zebulon.png"));
        hardCodedList.add(new Planet("Alpha Centauri III", 3, "assets/planets/alpha_centauri.png"));
        return hardCodedList;
    }

    @RequestMapping(path = "/shipyard-info.json", method = RequestMethod.POST)
    public Response shipyardInfo (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        return null;
    }

    @RequestMapping(path = "/end-turn.json", method = RequestMethod.POST)
    public Response endTurn () {
        return null;
    }

    @RequestMapping(path = "/colonize-planet.json", method = RequestMethod.POST)
    public Response colonizePlanet (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        Integer shipId = wrapper.getShipId();
        Integer planetId = wrapper.getPlanetId();
        return null;
    }

    @RequestMapping(path = "/combat-info.json", method = RequestMethod.POST)
    public CombatInfoWrapper combatInfo () {
        List<Starship> friendShips = new ArrayList<>();
        List<Starship> enemyShips = new ArrayList<>();
        friendShips.add(new Starship(null, ShipChassis.DESTROYER, "Defiant", "destroyer"));
        friendShips.add(new Starship(null, ShipChassis.DESTROYER, "Valiant", "destroyer"));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Tempest", "enemy"));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Earthquake", "enemy"));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Hurricane", "enemy"));
        enemyShips.add(new Starship(null, ShipChassis.FIGHTER, "Landslide", "enemy"));
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

    @RequestMapping(path = "/update-production-queue.json", method = RequestMethod.POST)
    public Response updateProductionQueue () {
        return null;
    }

    @RequestMapping(path = "/return-combat-result.json", method = RequestMethod.POST)
    public Response returnCombatResult () {
        return null;
    }

    @RequestMapping(path = "/process-turn.json", method = RequestMethod.POST)
    public TurnInfoWrapper processTurn () {
        Iterable<Starship> shipList = ships.findAll();
        for (Starship ship : shipList) {
            ship.moveToDestination();
            ships.save(ship);
        }
        System.out.println("!!Alert!!: We are advancing ALL ships. This is OK for now because we only have one game.");
        System.out.println("This will need to be fixed by using the game object and repo.");
        return new TurnInfoWrapper(5, 10);
    }

    public void initializeSystemInfo () {
        if (!initialized) {
            if (starSystems == null || ships == null || tunnels == null || planets == null) {
                throw new AssertionError("One of the repos is null");
            }
            initializePlanets();
            initializeSystems();
            initializeTunnels();
            initializeShips();
            initialized = true;
        }
    }

    public void initializePlanets () {
        Planet zebulon1 = planets.findFirstByName("Zebulon I");
        Planet zebulon2 = planets.findFirstByName("Zebulon II");
        if (zebulon1 == null) {
            zebulon1 = new Planet("Zebulon I", 10, "zebulon");
            planets.save(zebulon1);
        }
        if (zebulon2 == null) {
            zebulon2 = new Planet("Zebulon II", 8, "earth");
            planets.save(zebulon2);
        }
        Planet avalon1 = planets.findFirstByName("Avalon I");
        Planet avalon2 = planets.findFirstByName("Avalon II");
        if (avalon1 == null) {
            avalon1 = new Planet("Avalon I", 12, "alpha_centauri");
            planets.save(avalon1);
        }
        if (avalon2 == null) {
            avalon2 = new Planet("Avalon II", 6, "bleh");
            planets.save(avalon2);
        }
    }

    public void initializeSystems () {
        StarSystem zebulon = starSystems.findFirstByName("Zebulon");
        StarSystem avalon = starSystems.findFirstByName("Avalon");
        if (zebulon == null) {
            zebulon = new StarSystem("Zebulon", 0, 5);
            List<Planet> zebulonPlanets = new ArrayList<>();
            zebulonPlanets.add(planets.findFirstByName("Zebulon I"));
            zebulonPlanets.add(planets.findFirstByName("Zebulon II"));
            zebulon.setPlanets(zebulonPlanets);
            starSystems.save(zebulon);
        }
        if (avalon == null) {
            avalon = new StarSystem("Avalon", 2, 2);
            List<Planet> avalonPlanets = new ArrayList<>();
            avalonPlanets.add(planets.findFirstByName("Avalon I"));
            avalonPlanets.add(planets.findFirstByName("Avalon II"));
            avalon.setPlanets(avalonPlanets);
            starSystems.save(avalon);
        }
    }

    public void initializeShips () {
        Starship ship = ships.findFirstByName("P2 Gate Defender");

        StarSystem p2gate = starSystems.findFirstByName("P2 Gate");
        if (p2gate == null) {
            throw new AssertionError("It's null");
        }
        if (ship == null) {
            ship = new Starship(p2gate, ShipChassis.DESTROYER, "P2 Gate Defender", "default");
            ships.save(ship);
        }
        Starship colonizer = ships.findFirstByName("Colonizer");
        if (colonizer == null) {
            colonizer = new Starship(p2gate, ShipChassis.COLONIZER, "Colonizer", "default");
            ships.save(colonizer);
        }
    }

    //In order to get intellij to think these functions are "getting called"
    private void makeFunctionsBlack () {
        processTurn();
        returnCombatResult();
        changeOutput(null);
        changeResearch(null);
        enterTunnel(null);
        updateProductionQueue();
        scrapShip(null);
        colonizePlanet(null);
        simpleInfo(null);
        researchInfo(null);
        planetsInfo(null);
        getUsers();
        getMyUser(null);
        changeUsersLobby(null, null);
        shipsInfo(null);
        diplomacyInfo(null);
        processAttack(null);
        shipyardInfo(null);
        endTurn();
        ssgInfo(null);
        systemInfo( null);
        combatInfo();
        registration(null);
        newEmptyGame(null);
    }
}
