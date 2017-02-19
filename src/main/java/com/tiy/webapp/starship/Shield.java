package com.tiy.webapp.starship;

import javax.persistence.*;

/**
 * Created by erronius on 12/22/2016.
 */
public class Shield {

    private Integer maxDamageAbsorb;
    private Integer shieldHealth;
    private Integer maxShieldHealth;
    private Integer regenRate;

    public Shield() {
    }

    public Shield(int maxDamageAbsorb, int shieldHealth, int regenRate) {
        this.maxDamageAbsorb = maxDamageAbsorb;
        this.maxShieldHealth = shieldHealth;
        this.shieldHealth = maxShieldHealth;
        this.regenRate = regenRate;

    }


    public int getMaxDamageAbsorb() {
        return maxDamageAbsorb;
    }

    public int getShieldHealth() {
        return shieldHealth;
    }


    public static Shield getTemplateShield (ShipChassis shipType){
        switch (shipType) {
            case FIGHTER:
                return new Shield(15, 20, 0);
            case DESTROYER:
                return new Shield(25, 80, 1);
            case CRUISER:
                return new Shield(35, 120, 2);
        }
        return null;
    }


    public int getMaxShieldHealth () {
        return maxShieldHealth;
    }

    public int getRegenRate () {
        return regenRate;
    }

}
