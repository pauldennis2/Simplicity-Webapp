package com.tiy.webapp;

import com.tiy.webapp.repos.StarSystemRepo;
import com.tiy.webapp.starship.ShipChassis;
import com.tiy.webapp.starship.Starship;
import com.tiy.webapp.starsys.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul Dennis on 2/6/2017.
 */

@RestController
public class SimplicityRestController {

    @Autowired
    StarSystemRepo starSystems;

    @RequestMapping(path = "/user-login.json", method = RequestMethod.POST)
    public Response login () {
        return null;
    }

    @RequestMapping(path = "/user-registration.json", method = RequestMethod.POST)
    public Response registration () {
        return null;
    }

    @RequestMapping(path = "/new-empty-game.json", method = RequestMethod.POST)
    public Response newEmptyGame () {
        return null;
    }

    @RequestMapping(path = "/main-info.json", method = RequestMethod.POST)
    public Response mainInfo (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        return null;
    }

    @RequestMapping(path = "/system-info.json", method = RequestMethod.POST)
    public StarSystemInfoWrapper systemInfo (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        Integer systemId = wrapper.getSystemId();
        //return new StarSystemTemp(2, 3);
        StarSystem hardCodedSystem = new StarSystem();
        List<SpaceTunnel> tunnels = new ArrayList<>();
        tunnels.add(new SpaceTunnel());
        tunnels.add(new SpaceTunnel());
        List<Planet> planets = new ArrayList<>();
        planets.add(new Planet("Awesome I", 10, "earth.png"));
        planets.add(new Planet("Awesome II", 8, "zebulon.png"));
        hardCodedSystem.setTunnels(tunnels);
        hardCodedSystem.setPlanets(planets);
        List<Starship> ships = new ArrayList<>();
        ships.add(new Starship(null, ShipChassis.DESTROYER));
        ships.add(new Starship(null, ShipChassis.FIGHTER));
        return new StarSystemInfoWrapper(hardCodedSystem, ships);
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
        hardCodedList.add(new PlayerTemp(10, "Snovemdomas", "assets/1.jpg"));
        hardCodedList.add(new PlayerTemp(15, "Mebes", "assets/2.jpg"));
        hardCodedList.add(new PlayerTemp(0, "Oculons", "assets/3.jpg"));
        hardCodedList.add(new PlayerTemp(1, "Chamachies", "assets/4.jpg"));
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
        hardCodedList.add(new Ship("Defiant", 25, 30, "assets/fighter.png", "Zebulon System"));
        hardCodedList.add(new Ship("Voyager", 100, 100, "assets/destroyer.png", "Zebulon System"));
        hardCodedList.add(new Ship("Aegis", 30, 30, "assets/fighter.png", "Terran System"));
        return hardCodedList;
    }

    @RequestMapping(path = "/planets-info.json", method = RequestMethod.POST)
    public List<PlanetTemp> planetsInfo (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        List<PlanetTemp> hardCodedList = new ArrayList<>();
        hardCodedList.add(new PlanetTemp("Earth", "Terran", 12, "assets/earth.png"));
        hardCodedList.add(new PlanetTemp("Zebulon IV", "Zebulon", 5, "assets/zebulon.png"));
        hardCodedList.add(new PlanetTemp("Alpha Centauri III", "Alpha Centauri", 3, "assets/alphacentauri.png"));
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

    @RequestMapping(path = "/start-combat.json", method = RequestMethod.POST)
    public Response startCombat () {
        return null;
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
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        Integer shipId = wrapper.getShipId();
        Integer tunnelId = wrapper.getTunnelId();
        return null;
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
}
