package com.tiy.webapp.starsys;

import com.tiy.webapp.Player;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by erronius on 12/20/2016.
 */
@Entity
@Table(name = "star-system-graphs")
public class StarSystemGraph {

    @GeneratedValue
    @Id
    Integer id;

    @OneToMany
    List<StarSystem> starSystems;

    @Transient
    List<SpaceTunnel> tunnels;

    @Transient
    Map<String, StarSystem> nameStarSystemMap;
    @Transient
    Map<Point, StarSystem> pointStarSystemMap;

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
                pointStarSystemMap.put(new Point(starSystem.getGridCoordX(), starSystem.getGridCoordY()), starSystem);
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
                /*SpaceTunnel tunnel = new SpaceTunnel(tunnelLength);
                tunnels.add(tunnel);
                firstSystem.addTunnel(tunnel);
                secondSystem.addTunnel(tunnel);*/
                throw new AssertionError("Fix");
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public List<StarSystem> getStarSystems () {
        return starSystems;
    }

    public List<SpaceTunnel> getTunnels () {
        return tunnels;
    }

    public Map<String, StarSystem> getNameStarSystemMap () {
        return nameStarSystemMap;
    }
}
