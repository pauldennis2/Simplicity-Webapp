package com.tiy.webapp.starsys;

import com.tiy.webapp.starship.Starship;

import java.util.List;

/**
 * Created by Paul Dennis on 2/7/2017.
 */
public class StarSystemInfoWrapper {

    StarSystem starSystem;
    List<Starship> ships;

    public StarSystemInfoWrapper(StarSystem starSystem, List<Starship> ships) {
        this.starSystem = starSystem;
        this.ships = ships;
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
}
