package com.tiy.webapp;

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
    public Response systemInfo (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        Integer systemId = wrapper.getSystemId();
        return null;
    }

    @RequestMapping(path = "/research-info.json", method = RequestMethod.POST)
    public Response researchInfo (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        return null;
    }

    @RequestMapping(path = "/diplomacy-info.json", method = RequestMethod.POST)
    public List<Player> diplomacyInfo (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        List<Player> hardCodedList = new ArrayList<>();
        hardCodedList.add(new Player(10, "Snovemdomas", "assets/1.jpg"));
        hardCodedList.add(new Player(15, "Mebes", "assets/2.jpg"));
        hardCodedList.add(new Player(9, "Oculons", "assets/3.jpg"));
        hardCodedList.add(new Player(0, "Chamachies", "assets/4.jpg"));
        int total = 0;
        for (Player player : hardCodedList) {
            total += player.getPopulation();
        }
        for (Player player : hardCodedList) {
            player.calculatePercentageOfTotalPop((double) total);
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
    public List<Planet> planetsInfo (@RequestBody IdRequestWrapper wrapper) {
        Integer gameId = wrapper.getGameId();
        Integer playerId = wrapper.getPlayerId();
        List<Planet> hardCodedList = new ArrayList<>();
        hardCodedList.add(new Planet("Earth", "Terran", 12, "assets/earth.png"));
        hardCodedList.add(new Planet("Zebulon IV", "Zebulon", 5, "assets/zebulon.png"));
        hardCodedList.add(new Planet("Alpha Centauri III", "Alpha Centauri", 3, "assets/alphacentauri.png"));
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
