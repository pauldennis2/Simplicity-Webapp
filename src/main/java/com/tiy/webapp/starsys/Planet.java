package com.tiy.webapp.starsys;

import javax.persistence.*;

/**
 * Created by erronius on 12/20/2016.
 */

@Entity
@Table(name = "planets")
public class Planet {

    @GeneratedValue
    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer size;

    @Column (nullable = false)
    private String imageString;

    @Column (nullable = false)
    private Integer population;

    @Column (nullable = true)
    private Double researchPct;

    @Column (nullable = true)
    private Double productionPct;

    @Column (nullable = true)
    private Integer turnsToGrowth;

    @Column (nullable = false)
    private Integer ownerRaceNum;

    public static final double BASE_GROWTH_RATE = 1.05; //Planets grow by 5% per turn

    public Planet () {

    }

    public Planet(String name, Integer size, String imageString) {
        this.size = size;
        this.name = name;
        this.imageString = imageString;
        productionPct = 0.9;
        researchPct = 0.1;
        population = 0;
        ownerRaceNum = -1;
    }

    @Transient
    public void growPop () {
        if (population > 0 && population < size) {
            if (turnsToGrowth == null) {
                turnsToGrowth = calculateTurnsToGrowth();
            }
            if (turnsToGrowth > 1) {
                turnsToGrowth--;
            } else {
                population++;
                turnsToGrowth = calculateTurnsToGrowth();
            }
        }
    }

    @Transient
    private int calculateTurnsToGrowth () {
        if (population > 0 && population < size) {
            double pop = (double) population;
            int numTurns = 0;
            while (pop < population + 1) {
                pop *= BASE_GROWTH_RATE;
                numTurns++;
            }
            return numTurns;
        } else {
            return -1;
        }
    }

    @Transient
    public void setProduction (Integer productionNumber) {
        if (productionNumber > 100 || productionNumber < 0) {
            throw new AssertionError("Bad input");
        }
        if (productionNumber > 95) {
            productionPct = 1.0;
            researchPct = 0.0;
        } else if (productionNumber > 85) {
            productionPct = 0.9;
            researchPct = 0.1;
        } else if (productionNumber > 75) {
            productionPct = 0.8;
            researchPct = 0.2;
        } else if (productionNumber > 65) {
            productionPct = 0.7;
            researchPct = 0.3;
        } else if (productionNumber > 55) {
            productionPct = 0.6;
            researchPct = 0.4;
        } else if (productionNumber > 45) {
            productionPct = 0.5;
            researchPct = 0.5;
        } else if (productionNumber > 35) {
            productionPct = 0.4;
            researchPct = 0.6;
        } else if (productionNumber > 25) {
            productionPct = 0.3;
            researchPct = 0.7;
        } else if (productionNumber > 15) {
            productionPct = 0.2;
            researchPct = 0.8;
        } else if (productionNumber > 5) {
            productionPct = 0.1;
            researchPct = 0.9;
        } else {
            productionPct = 0.0;
            researchPct = 1.0;
        }
    }


    public void setPopulation(Integer population) {
        this.population = population;
        turnsToGrowth = calculateTurnsToGrowth();
    }

    /*public void setPopulation (int pop) {
        population = pop;
    }


    public void setResearchPct (double researchPct) throws ImproperFunctionInputException {
        if (researchPct > 1.0f || researchPct < 0.0f) {
            throw new ImproperFunctionInputException("Value must be between 0.0 and 1.0 inclusive.");
        }
        this.researchPct = researchPct;
        productionPct = 1.0f - researchPct;
    }

    @Transient
    public int produceResearch () {
        //for now always round to nearest number.
        //Later go back and add to partialResearch
        //Todo fix rounding
        double researchProduced = researchPct * population;
        return Math.round(Math.round(researchProduced));
    }

    @Transient
    public int produceProduction () {
        double productionProduced = productionPct * population;
        return Math.round(Math.round(productionProduced));
    }

    public void growPopulation () {
        if (turnsToGrowth > 1) {
            turnsToGrowth--;
        } else {
            population++;
            turnsToGrowth = calculateTurnsToGrowth();
        }
    }

    public int getPopulation () {
        return population;
    }

    @Transient
    public int calculateTurnsToGrowth () {
        if (population > 0 && population < size) {
            double pop = (double) population;
            int numTurns = 0;
            while (pop < population + 1) {
                pop *= BASE_GROWTH_RATE;
                numTurns++;
            }
            return numTurns;
        } else {
            return -1;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getResearchPct() {
        return researchPct;
    }

    public void setResearchPct(Double researchPct) {
        this.researchPct = researchPct;
    }

    public Double getProductionPct() {
        return productionPct;
    }

    public void setProductionPct(Double productionPct) {
        this.productionPct = productionPct;
    }

    public Integer getTurnsToGrowth() {
        return turnsToGrowth;
    }

    public void setTurnsToGrowth(Integer turnsToGrowth) {
        this.turnsToGrowth = turnsToGrowth;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public Integer getPopulation() {
        return population;
    }


    public Double getResearchPct() {
        return researchPct;
    }

    public void setResearchPct(Double researchPct) {
        this.researchPct = researchPct;
    }

    public Double getProductionPct() {
        return productionPct;
    }

    public void setProductionPct(Double productionPct) {
        this.productionPct = productionPct;
    }

    public Integer getTurnsToGrowth() {
        return turnsToGrowth;
    }

    public void setTurnsToGrowth(Integer turnsToGrowth) {
        this.turnsToGrowth = turnsToGrowth;
    }

    public int getOwnerRaceNum() {
        return ownerRaceNum;
    }

    public void setOwnerRaceNum(int ownerRaceNum) {
        this.ownerRaceNum = ownerRaceNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOwnerRaceNum(Integer ownerRaceNum) {
        this.ownerRaceNum = ownerRaceNum;
    }
}
