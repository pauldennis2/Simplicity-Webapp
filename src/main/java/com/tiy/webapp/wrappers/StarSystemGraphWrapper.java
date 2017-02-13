package com.tiy.webapp.wrappers;

import com.tiy.webapp.repos.TunnelRepo;
import com.tiy.webapp.starsys.Point;
import com.tiy.webapp.starsys.SpaceTunnel;
import com.tiy.webapp.starsys.StarSystem;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Paul Dennis on 2/11/2017.
 */
public class StarSystemGraphWrapper {

    @Autowired
    TunnelRepo tunnelRepo;

    List<SpaceTunnel> tunnels = new ArrayList<>();
    List<StarSystem> starSystems = new ArrayList<>();

    public StarSystemGraphWrapper () {

    }

    public StarSystemGraphWrapper (String fileName) {
        Map<String, StarSystem> nameStarSystemMap = new HashMap<>();
        Map<Point, StarSystem> pointStarSystemMap = new HashMap<>();
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
                starSystems.add(new StarSystem(systemName, xCoord, yCoord, new Random()));
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
                SpaceTunnel tunnel = new SpaceTunnel(tunnelLength, firstSystem, secondSystem);

                tunnelRepo.save(tunnel);
                tunnels.add(tunnel);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    public List<SpaceTunnel> getTunnels() {
        return tunnels;
    }

    public void setTunnels(List<SpaceTunnel> tunnels) {
        this.tunnels = tunnels;
    }

    public List<StarSystem> getStarSystems() {
        return starSystems;
    }

    public void setStarSystems(List<StarSystem> starSystems) {
        this.starSystems = starSystems;
    }
}
