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

    @Column(nullable = false)
    private String imageString;

    @Column(nullable = true)
    private Integer population;

    @Column(nullable = true)
    private Double researchPct;

    @Column(nullable = true)
    private Double productionPct;

    @Column(nullable = true)
    private Integer turnsToGrowth;

    public static final double BASE_GROWTH_RATE = 1.05; //Planets grow by 5% per turn

    public Planet () {

    }

    public Planet(String name, Integer size, String imageString) {
        this.size = size;
        this.name = name;
        this.imageString = imageString;
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

    public void setPopulation(Integer population) {
        this.population = population;
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
}
