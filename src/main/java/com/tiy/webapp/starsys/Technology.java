package com.tiy.webapp.starsys;

/**
 * Created by erronius on 12/30/2016.
 */
public enum Technology {

    HEPHAESTUS_I(15);

    private boolean complete;
    private final int researchCost;

    Technology(int researchCost) {
        this.researchCost = researchCost;
        complete = false;
    }

    public void setComplete (boolean status) {
        complete = status;
    }

    public int getResearchCost () {
        return researchCost;
    }

    public boolean isComplete () {
        return complete;
    }
}
