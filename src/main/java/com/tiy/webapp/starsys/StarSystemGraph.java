package com.tiy.webapp.starsys;

import com.tiy.webapp.Player;
import com.tiy.webapp.repos.StarSystemRepo;
import com.tiy.webapp.repos.TunnelRepo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by erronius on 12/20/2016.
 */
@Entity
@Table(name = "star_system_graphs")
public class StarSystemGraph {

    @GeneratedValue
    @Id
    private Integer id;

    @Column (nullable = false, unique = true)
    private String name;

    @OneToMany
    private List<StarSystem> starSystems;

    @OneToMany
    private List<SpaceTunnel> tunnels;

    @Transient
    @Autowired
    TunnelRepo tunnelRepo;

    @Transient
    @Autowired
    StarSystemRepo starSystemRepo;

    public StarSystemGraph (String fileName) {
        Map<String, StarSystem> nameStarSystemMap;
        Map<Point, StarSystem> pointStarSystemMap;
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
                StarSystem system = new StarSystem(systemName, xCoord, yCoord);
                starSystems.add(system);
                starSystemRepo.save(system);
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

    public List<StarSystem> getStarSystems () {
        return starSystems;
    }

    public List<SpaceTunnel> getTunnels () {
        return tunnels;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStarSystems(List<StarSystem> starSystems) {
        this.starSystems = starSystems;
    }

    public void setTunnels(List<SpaceTunnel> tunnels) {
        this.tunnels = tunnels;
    }
}
