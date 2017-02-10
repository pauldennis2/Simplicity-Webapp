package com.tiy.webapp.starsys;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by erronius on 12/20/2016.
 */
@Entity
@Table (name = "systems")
public class StarSystem {

    @GeneratedValue
    @Id
    private Integer id;

    @Column (nullable = false)
    private String name;

    @OneToMany (cascade = CascadeType.ALL)
    private List<Planet> planets;

    @Column (nullable = false)
    private Integer gridCoordX;

    @Column (nullable = false)
    private Integer gridCoordY;

    public static final int MAX_PLANETS = 4;
    public static final String[] ROMAN_NUMERALS = {"I", "II", "III", "IV", "V"};
    public static final int DEFAULT_PLANET_SIZE = 10;

    public StarSystem () {

    }

    public StarSystem (String name, int gridCoordX, int gridCoordY) {
        this.name = name;
        planets = new ArrayList<>();
        this.gridCoordX = gridCoordX;
        this.gridCoordY = gridCoordY;
        planets.add(new Planet(name + " " + ROMAN_NUMERALS[0], DEFAULT_PLANET_SIZE, "default"));
    }

    public StarSystem (String name, int gridCoordX, int gridCoordY, Random random) {
        this.name = name;
        planets = new ArrayList<>();
        this.gridCoordX = gridCoordX;
        this.gridCoordY = gridCoordY;
        //decide how many planets to create, 0-3
        int numPlanets = random.nextInt(MAX_PLANETS);
        for (int index = 0; index < numPlanets; index++) {
            int size = random.nextInt(3);
            planets.add(new Planet(name + " " + ROMAN_NUMERALS[index], DEFAULT_PLANET_SIZE + size, "default"));
        }
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    public Integer getGridCoordX() {
        return gridCoordX;
    }

    public void setGridCoordX(Integer gridCoordX) {
        this.gridCoordX = gridCoordX;
    }

    public Integer getGridCoordY() {
        return gridCoordY;
    }

    public void setGridCoordY(Integer gridCoordY) {
        this.gridCoordY = gridCoordY;
    }

    public String getName() {
        return name;
    }

    /**
     * Calculates the Cartesian distance between the two StarSystems (namely the length of the tunnel between them
     * if the tunnel follows normal Cartesian rules). We don't care if there IS a tunnel.
     * I think, therefore this works.
     * @param firstSystem
     * @param secondSystem
     * @return Distance between the systems (rounded to nearest whole number)
     */
    public static int calculateCartesianDistance (StarSystem firstSystem, StarSystem secondSystem) {
        int xDif = Math.abs(firstSystem.getGridCoordX() - secondSystem.getGridCoordX());
        int yDif = Math.abs(firstSystem.getGridCoordY() - secondSystem.getGridCoordY());
        double hypotenuseLength = Math.sqrt(xDif*xDif + yDif*yDif);
        return (int)Math.round(hypotenuseLength);
    }


}
