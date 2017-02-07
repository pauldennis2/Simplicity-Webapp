package com.tiy.webapp.starsys;

import com.tiy.webapp.ImproperFunctionInputException;
import com.tiy.webapp.Player;

import java.util.Random;

/**
 * Created by erronius on 12/20/2016.
 */
public class Planet {

    private boolean habitable;
    private int mineralLevel;
    private int size;
    private int population;
    private String name;
    private Player owner;

    private float researchPct;
    private float productionPct;

    private double partialResearch;
    private double partialProduction;

    private StarSystem system;

    public static final double BASE_GROWTH_RATE = 1.05; //Planets grow by 5% per turn
    private double planetGrowthRate = 1.0;
    private int turnsToGrowth;

    public Planet (String name, int size, boolean habitable, StarSystem system) {
        this.name = name;
        this.size = size;
        this.habitable = habitable;
        owner = null;
        population = 0;
        researchPct = 0.4f;
        productionPct = 0.6f;
        this.system = system;
    }

    public Planet (String name, Random random) {
        this.name = name;
    }

    @Override
    public String toString () {
        //Example:
        //Alpha Centauri Owned by Player 4/8
        String response = name + " ";
        if (owner != null) {
            response += "Owned by " + owner.getName() + " ";
        }
        response += population + "/" + size;
        return response;
    }

    public Player getOwner () {
        return owner;
    }

    public StarSystem getSystem () {
        return system;
    }

    public void setPopulation (int pop) {
        population = pop;
        turnsToGrowth = calculateTurnsToGrowth();
    }

    public void setOwner (Player owner) {
        this.owner = owner;
    }

    public void setResearchPct (float researchPct) throws ImproperFunctionInputException {
        if (researchPct > 1.0f || researchPct < 0.0f) {
            throw new ImproperFunctionInputException("Value must be between 0.0 and 1.0 inclusive.");
        }
        this.researchPct = researchPct;
        productionPct = 1.0f - researchPct;
    }

    public int getResearch () {
        //for now always round to nearest number.
        //Later go back and add to partialResearch
        float researchProduced = researchPct * population;
        return Math.round(researchProduced);
    }

    public int getProduction () {
        float productionProduced = productionPct * population;
        return Math.round(productionProduced);
    }

    public void growPopulation () {
        if (turnsToGrowth > 1) {
            turnsToGrowth--;
        } else {
            population++;
            turnsToGrowth = calculateTurnsToGrowth();
        }
    }

    public float getResearchPct () {
        return researchPct;
    }

    public float getProductionPct () {
        return productionPct;
    }

    public int getPopulation () {
        return population;
    }

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
}
