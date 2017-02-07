package com.tiy.webapp;

/**
 * Created by Paul Dennis on 2/6/2017.
 */
public class Planet {

    private String name;
    private String system;
    private int population;

    private String imageString;

    public Planet(String name, String system, int population, String imageString) {
        this.name = name;
        this.system = system;
        this.population = population;
        this.imageString = imageString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }
}
