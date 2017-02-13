package com.tiy.webapp;

/**
 * Created by Paul Dennis on 2/6/2017.
 */
public class PlayerTemp {

    private int population;
    private String name;
    private int id;
    private String imageFile;

    private double percentOfTotalPop;

    public PlayerTemp(int population, String name, String imageFile) {
        this.population = population;
        this.name = name;
        this.imageFile = imageFile;
        if (population == 0) {
            this.imageFile = imageFile.substring(0, 18) + "_extinct.jpg";
        }
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void calculatePercentageOfTotalPop (double total) {
        percentOfTotalPop = (double)population/total;
        percentOfTotalPop *= 100.0;
        String pct = "" + percentOfTotalPop;
        if (pct.length() > 3) {
            pct = pct.substring(0, 4);
        }
        percentOfTotalPop = Double.parseDouble(pct);
    }

    public double getPercentOfTotalPop () {
        return percentOfTotalPop;
    }

    public void setPercentOfTotalPop(double percentOfTotalPop) {
        this.percentOfTotalPop = percentOfTotalPop;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
}
