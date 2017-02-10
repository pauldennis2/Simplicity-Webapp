package com.tiy.webapp.starsys;

import com.tiy.webapp.starship.Starship;

import java.util.List;

/**
 * Created by Paul Dennis on 2/7/2017.
 */
public class StarSystemInfoWrapper {

    StarSystem starSystem;
    List<Starship> ships;
    List<SpaceTunnel> tunnels;

    public StarSystemInfoWrapper(StarSystem starSystem, List<Starship> ships, List<SpaceTunnel> tunnels) {
        this.starSystem = starSystem;
        this.ships = ships;
        this.tunnels = tunnels;
    }

    public StarSystem getStarSystem() {
        return starSystem;
    }

    public void setStarSystem(StarSystem starSystem) {
        this.starSystem = starSystem;
    }

    public List<Starship> getShips() {
        return ships;
    }

    public void setShips(List<Starship> ships) {
        this.ships = ships;
    }

    public List<SpaceTunnel> getTunnels() {
        return tunnels;
    }

    public void setTunnels(List<SpaceTunnel> tunnels) {
        this.tunnels = tunnels;
    }
}
