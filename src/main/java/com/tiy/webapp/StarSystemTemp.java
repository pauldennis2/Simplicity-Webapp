package com.tiy.webapp;

import java.util.List;

/**
 * Created by Paul Dennis on 2/7/2017.
 */
public class StarSystemTemp {

    private int numTunnels;
    private int numPlanets;

    List<Ship> ships;

    public StarSystemTemp () {

    }

    public StarSystemTemp(int numTunnels, int numPlanets) {
        this.numTunnels = numTunnels;
        this.numPlanets = numPlanets;
    }

    public StarSystemTemp(int numTunnels, int numPlanets, List<Ship> ships) {
        this.numTunnels = numTunnels;
        this.numPlanets = numPlanets;
        this.ships = ships;
    }

    public int getNumTunnels() {
        return numTunnels;
    }

    public void setNumTunnels(int numTunnels) {
        this.numTunnels = numTunnels;
    }

    public int getNumPlanets() {
        return numPlanets;
    }

    public void setNumPlanets(int numPlanets) {
        this.numPlanets = numPlanets;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }
}
