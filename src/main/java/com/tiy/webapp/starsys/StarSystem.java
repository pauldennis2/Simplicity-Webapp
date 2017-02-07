package com.tiy.webapp.starsys;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by erronius on 12/20/2016.
 */
public class StarSystem extends Location {

    String name;
    List<Planet> planets;
    List<SpaceTunnel> tunnels;

    public int gridCoordX;
    public int gridCoordY;

    private boolean discoveredByPlayer = false;
    private boolean[] discoveredByPlayers;

    public static final int MAX_PLANETS = 4;

    public static final String[] ROMAN_NUMERALS = {"I", "II", "III", "IV", "V"};

    public static final int DEFAULT_PLANET_SIZE = 10;

    //Create one habitable planet of default size
    public StarSystem (String name) {
        this.name = name;
        planets = new ArrayList<>();
        tunnels = new ArrayList<>();
        planets.add(new Planet(name + " " + ROMAN_NUMERALS[0], DEFAULT_PLANET_SIZE, true, this));
    }

    public StarSystem (String name, Random random) {
        this.name = name;
        planets = new ArrayList<>();
        tunnels = new ArrayList<>();
        //decide how many planets to create, 0-4
        int numPlanets = random.nextInt(5);
        for (int index = 0; index < numPlanets; index++) {
            boolean habitable = random.nextBoolean();
            int size = random.nextInt(3);
            planets.add(new Planet(name + " " + ROMAN_NUMERALS[index], DEFAULT_PLANET_SIZE + size, habitable, this));
        }
    }

    public StarSystem (String name, int gridCoordX, int gridCoordY) {
        this.name = name;
        planets = new ArrayList<>();
        tunnels = new ArrayList<>();
        this.gridCoordX = gridCoordX;
        this.gridCoordY = gridCoordY;
        //planets.add(new Planet())
    }

    public StarSystem (String name, int gridCoordX, int gridCoordY, Random random) {
        this.name = name;
        planets = new ArrayList<>();
        tunnels = new ArrayList<>();
        this.gridCoordX = gridCoordX;
        this.gridCoordY = gridCoordY;
        //decide how many planets to create, 0-3
        int numPlanets = random.nextInt(MAX_PLANETS);
        for (int index = 0; index < numPlanets; index++) {
            boolean habitable = random.nextBoolean();
            int size = random.nextInt(3);
            planets.add(new Planet(name + " " + ROMAN_NUMERALS[index], DEFAULT_PLANET_SIZE + size, habitable, this));
        }
    }

    public void addTunnel (SpaceTunnel tunnel) {
        tunnels.add(tunnel);
    }


    public String getName () {
        return name;
    }

    public List<SpaceTunnel> getTunnels () {
        return tunnels;
    }

    @Override
    public String toString() {
        String response = name + "\nPlanets:\n";
        for (Planet planet : planets) {
            response += planet.toString() + "\n";
        }
        response += "Tunnels:\n";
        for (SpaceTunnel tunnel : tunnels) {
            response += tunnel.getLength() + " days to " + tunnel.getOtherSystem(this).getName() + "\n";
        }
        return response;
    }

    public List<Planet> getPlanets () {
        return planets;
    }

    public List<StarSystem> getConnectedSystems () {
        List<StarSystem> connectedSystems = new ArrayList<>();
        for (SpaceTunnel tunnel : tunnels) {
            connectedSystems.add(tunnel.getOtherSystem(this));
        }
        return connectedSystems;
    }

    public int getX () {
        return gridCoordX;
    }

    public int getY () {
        return gridCoordY;
    }

    /**
     * Calculates the Cartesian distance between the two StarSystems (namely the length of the tunnel between them
     * if the tunnel follows normal Cartesian rules). We don't care if there IS a tunnel.
     * @param firstSystem
     * @param secondSystem
     * @return Distance between the systems (rounded to nearest whole number)
     */
    public static int calculateCartesianDistance (StarSystem firstSystem, StarSystem secondSystem) {
        int xDif = Math.abs(firstSystem.getX() - secondSystem.getX());
        int yDif = Math.abs(firstSystem.getY() - secondSystem.getY());
        double hypotenuseLength = Math.sqrt(xDif*xDif + yDif*yDif);
        return (int)Math.round(hypotenuseLength);
    }

}
