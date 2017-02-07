package com.tiy.webapp.starsys;

import com.tiy.webapp.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by erronius on 12/20/2016.
 */
public class StarSystemGraph {

    List<StarSystem> starSystems;
    List<SpaceTunnel> tunnels;

    Map<String, StarSystem> nameStarSystemMap;
    Map<Point, StarSystem> pointStarSystemMap;

    public static final int FIRST_TUNNEL_LENGTH = 2;
    public static final int SECOND_TUNNEL_LENGTH = 4;
    public static final int THIRD_TUNNEL_LENGTH = 5;


    //Create a super simple graph, A <=> B <=> C
    public StarSystemGraph () {
        starSystems = new ArrayList<StarSystem>();
        StarSystem homeSystem = new StarSystem("Home");
        StarSystem emptySystem = new StarSystem("Alpha Centauri");
        StarSystem scarySystem = new StarSystem("ScaryTown");
        starSystems.add(homeSystem);
        starSystems.add(emptySystem);
        starSystems.add(scarySystem);
        SpaceTunnel homeToEmpty = new SpaceTunnel(2, homeSystem, emptySystem);
        SpaceTunnel emptyToScary = new SpaceTunnel(3, emptySystem, scarySystem);

        homeSystem.addTunnel(homeToEmpty);
        emptySystem.addTunnel(homeToEmpty);
        emptySystem.addTunnel(emptyToScary);
        scarySystem.addTunnel(emptyToScary);

        Planet homePlanet = homeSystem.getPlanets().get(0);
        homePlanet.setOwner(new Player(homeSystem, "Tester"));
        homePlanet.setPopulation(6);
    }

    public StarSystemGraph (int numPlayers, MapSize size) {
        starSystems = new ArrayList<StarSystem>();
        nameStarSystemMap = new HashMap<String, StarSystem>();
        numPlayers = 2;
        //Using ring design
        StarSystem p1home = new StarSystem("P1 Home");
        StarSystem p2home = new StarSystem("P2 Home");
        starSystems.add(p1home);
        starSystems.add(p2home);
        switch (size) {
            case SMALL:
                starSystems.add(new StarSystem("P1 Home CW"));
                starSystems.add(new StarSystem("P1 Home CCW"));
                starSystems.add(new StarSystem("P2 Home CW"));
                starSystems.add(new StarSystem("P2 Home CCW"));
                starSystems.add(new StarSystem("Left Mid"));
                starSystems.add(new StarSystem("Mid"));
                starSystems.add(new StarSystem("Right Mid"));
                break;
            case MEDIUM:
                throw new AssertionError("Not yet implemented");
            case LARGE:
                throw new AssertionError("Not yet implemented");
        }

        for (StarSystem system : starSystems) {
            nameStarSystemMap.put(system.getName(), system);
        }

        List<SpaceTunnel> tunnels = new ArrayList<>();
        tunnels.add(new SpaceTunnel(FIRST_TUNNEL_LENGTH, nameStarSystemMap.get("P1 Home"), nameStarSystemMap.get("P1 Home CW")));
        tunnels.add(new SpaceTunnel(FIRST_TUNNEL_LENGTH, nameStarSystemMap.get("P1 Home"), nameStarSystemMap.get("P1 Home CCW")));

        tunnels.add(new SpaceTunnel(FIRST_TUNNEL_LENGTH, nameStarSystemMap.get("P2 Home"), nameStarSystemMap.get("P2 Home CW")));
        tunnels.add(new SpaceTunnel(FIRST_TUNNEL_LENGTH, nameStarSystemMap.get("P2 Home"), nameStarSystemMap.get("P2 Home CCW")));

        tunnels.add(new SpaceTunnel(SECOND_TUNNEL_LENGTH, nameStarSystemMap.get("P1 Home CW"), nameStarSystemMap.get("Right Mid")));
        tunnels.add(new SpaceTunnel(SECOND_TUNNEL_LENGTH, nameStarSystemMap.get("P1 Home CCW"), nameStarSystemMap.get("Left Mid")));

        tunnels.add(new SpaceTunnel(SECOND_TUNNEL_LENGTH, nameStarSystemMap.get("P2 Home CW"), nameStarSystemMap.get("Left Mid")));
        tunnels.add(new SpaceTunnel(SECOND_TUNNEL_LENGTH, nameStarSystemMap.get("P2 Home CCW"), nameStarSystemMap.get("Right Mid")));

        tunnels.add(new SpaceTunnel(THIRD_TUNNEL_LENGTH, nameStarSystemMap.get("Left Mid"), nameStarSystemMap.get("Mid")));
        tunnels.add(new SpaceTunnel(THIRD_TUNNEL_LENGTH, nameStarSystemMap.get("Right Mid"), nameStarSystemMap.get("Mid")));

        for (SpaceTunnel tunnel : tunnels) {
            StarSystem firstSystem = tunnel.getFirstSystem();
            StarSystem secondSystem = tunnel.getSecondSystem();

            firstSystem.addTunnel(tunnel);
            secondSystem.addTunnel(tunnel);
        }
    }

    public StarSystemGraph (String fileName) {
        starSystems = new ArrayList<StarSystem>();
        nameStarSystemMap = new HashMap<String, StarSystem>();
        pointStarSystemMap = new HashMap<Point, StarSystem>();
        tunnels = new ArrayList<SpaceTunnel>();


        //read from file
        try {
            //File Syntax: First Section ... "=====" Second Section
            Scanner fileScanner = new Scanner(new File(fileName));
            while (fileScanner.hasNext()) {
                //First Section: System Information
                //System Name,X,Y
                String inputLine = fileScanner.nextLine();
                if (inputLine.contains("=")) {
                    break;
                }
                String[] splitInputLine = inputLine.split(",");
                String systemName = splitInputLine[0];
                int xCoord = Integer.parseInt(splitInputLine[1]);
                int yCoord = Integer.parseInt(splitInputLine[2]);
                starSystems.add(new StarSystem(systemName, xCoord, yCoord));
            }
            for (StarSystem starSystem : starSystems) {
                nameStarSystemMap.put(starSystem.getName(), starSystem);
                pointStarSystemMap.put(new Point(starSystem.getX(), starSystem.getY()), starSystem);
            }
            while (fileScanner.hasNext()) {
                //Second Section: Tunnel Information
                //First Sys Name,Second Sys Name<optional:,customTunnelLength>
                String inputLine = fileScanner.nextLine();
                String[] splitInputLine = inputLine.split(",");
                String firstSystemName = splitInputLine[0];
                String secondSystemName = splitInputLine[1];
                StarSystem firstSystem = nameStarSystemMap.get(firstSystemName);
                StarSystem secondSystem = nameStarSystemMap.get(secondSystemName);
                int tunnelLength;
                if (splitInputLine.length > 2) {
                    tunnelLength = Integer.parseInt(splitInputLine[2]);
                } else {
                    tunnelLength = StarSystem.calculateCartesianDistance(firstSystem, secondSystem);
                }
                SpaceTunnel tunnel = new SpaceTunnel(tunnelLength, firstSystem, secondSystem);
                tunnels.add(tunnel);
                firstSystem.addTunnel(tunnel);
                secondSystem.addTunnel(tunnel);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString () {
        //This assumes a simple simple graph A <=> B <=> C
        StringBuilder response = new StringBuilder("");
        List<SpaceTunnel> paintedTunnels = new ArrayList<>();
        for (StarSystem starSystem : starSystems) {
            response.append(starSystem.getName());
            for (SpaceTunnel tunnel : starSystem.getTunnels()) {
                if (!paintedTunnels.contains(tunnel)) { //if we haven't drawn it yet
                    paintedTunnels.add(tunnel); //Add it to the list of drawn tunnels
                    int length = tunnel.getLength();
                    response.append("<");
                    for (int index = 0; index < length; index++) {
                        response.append("=");
                    }
                    response.append(">");
                }
            }
        }
        return response.toString();
    }

    public List<StarSystem> getStarSystems () {
        return starSystems;
    }

    public void printInfo () {
        for (String systemName : nameStarSystemMap.keySet()) {
            StarSystem system = nameStarSystemMap.get(systemName);
            System.out.println("----");
            System.out.println("System name: " + systemName);
            System.out.println("Connected Systems:");
            for (SpaceTunnel tunnel : system.getTunnels()) {
                System.out.println("\t" + tunnel.getOtherSystem(system).getName());
            }
        }
    }

    public void addSystem (StarSystem system) {
        System.out.println("starSystemGraph.addSystem(): Only for testing purposes.");
        starSystems.add(system);
    }

    public boolean isConnected () {
        //Logic to search through a proposed system and ensure it is connected.
        //Suggestion: start at a given node, and start adding to a SET (not a list) all nodes we can find connected to it
        //Then compare that to the overall set of systems; if they match, it's connected

        Set<StarSystem> connectedSystems = new HashSet<StarSystem>();

        connectedSystems.add(starSystems.get(0));
        List<StarSystem> firstSystem = new ArrayList<>();
        firstSystem.add(starSystems.get(0));

        connectedSystems = discoverSystems(connectedSystems, firstSystem);

        for (StarSystem system : starSystems) {
            if (!connectedSystems.contains(system)) {
                return false;
            }
        }
        return true;
    }

    private Set<StarSystem> discoverSystems (Set<StarSystem> connectedSystems, List<StarSystem> lastRoundFoundSystems) {
        List<StarSystem> thisRoundFoundSystems = new ArrayList<>();

        for (StarSystem currentSystem : lastRoundFoundSystems) {
            for (SpaceTunnel tunnel : currentSystem.getTunnels()) {
                StarSystem otherSystem = tunnel.getOtherSystem(currentSystem);

                //If we found an undiscovered system, add it to the set and the recently found system list
                if (!connectedSystems.contains(otherSystem)) {
                    thisRoundFoundSystems.add(otherSystem);
                    connectedSystems.add(otherSystem);
                }
            }
        }
        if (thisRoundFoundSystems.size() > 0) {
            return discoverSystems(connectedSystems, thisRoundFoundSystems);
        }
        return connectedSystems;
    }

    public List<SpaceTunnel> getTunnels () {
        return tunnels;
    }

    public Map<String, StarSystem> getNameStarSystemMap () {
        return nameStarSystemMap;
    }
}
