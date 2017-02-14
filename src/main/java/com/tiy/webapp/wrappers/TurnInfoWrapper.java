package com.tiy.webapp.wrappers;

/**
 * Created by Paul Dennis on 2/14/2017.
 */
public class TurnInfoWrapper {

    private int researchAmt;
    private int productionAmt;

    public TurnInfoWrapper(int researchAmt, int productionAmt) {
        this.researchAmt = researchAmt;
        this.productionAmt = productionAmt;
    }

    public int getResearchAmt() {
        return researchAmt;
    }

    public void setResearchAmt(int researchAmt) {
        this.researchAmt = researchAmt;
    }

    public int getProductionAmt() {
        return productionAmt;
    }

    public void setProductionAmt(int productionAmt) {
        this.productionAmt = productionAmt;
    }
}
