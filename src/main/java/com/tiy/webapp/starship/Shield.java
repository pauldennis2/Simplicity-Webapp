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
    private Boolean shieldsUp;

    public Shield() {
    }

    public Shield(int maxDamageAbsorb, int shieldHealth, int regenRate) {
        this.maxDamageAbsorb = maxDamageAbsorb;
        this.maxShieldHealth = shieldHealth;
        this.shieldHealth = maxShieldHealth;
        this.regenRate = regenRate;

        shieldsUp = true;
    }


    /**
     *
     * @param damageAmount damage to attempt to shield
     * @return amount not absorbed to be taken by ship/armor
     */
    public int takeDamage (int damageAmount) {
        //relevant nums: maxDamageAbsorb, shieldHealth
        if (shieldHealth >= damageAmount) {
            if (damageAmount <= maxDamageAbsorb) {
                shieldHealth -= damageAmount;
                return 0;
            } else { //damageAmount > maxAbsorb
                shieldHealth -= maxDamageAbsorb;
                return damageAmount - maxDamageAbsorb;
            }
        } else { //shieldHealth < damageAmount
            if (damageAmount <= maxDamageAbsorb) {
                int returnDamage = damageAmount - shieldHealth;
                shieldHealth = 0;
                return returnDamage;
            } else { //damageAmount > maxDamageAbsorb
                if (shieldHealth >= maxDamageAbsorb) {
                    shieldHealth -= maxDamageAbsorb;
                    return damageAmount - maxDamageAbsorb;
                } else {
                    int returnDamage = damageAmount - shieldHealth;
                    shieldHealth = 0;
                    return returnDamage;
                }
            }
        }
        /*
        //We'll allow shieldHealth to dip below 0
        if (damageAmount >= maxDamageAbsorb) {
            if (shieldHealth >= maxDamageAbsorb) {
                shieldHealth -= maxDamageAbsorb;
                return damageAmount - maxDamageAbsorb;
            } else { //shieldHealth < maxDamageAbsorb
                int damageAbsorbed = shieldHealth;
                shieldHealth = 0;
                return damageAmount - damageAbsorbed
            }
        } else {
            shieldHealth -= damageAmount;
        }
        if (shieldHealth < 0){
            int returnDamage = Math.abs(shieldHealth);
            shieldHealth = 0;
            return returnDamage;
        }
        return 0;*/
    }

    public boolean shieldsUp () {
        return shieldsUp;
    }

    //Even though we already know our regen rate, we have to pass in the max power available for shield regen

    /**
     *
     * @param maxRegenAmount max power available to regen shields
     * @return power used
     */
    public int regenerate (int maxRegenAmount) {
        int regenAmount;
        if (regenRate <= maxRegenAmount) {
            regenAmount = regenRate;
        } else {
            regenAmount = maxRegenAmount;
        }
        if (regenAmount + shieldHealth <= maxShieldHealth) {
            shieldHealth += regenAmount;
            return regenAmount;
        } else {
            int difference = maxShieldHealth - shieldHealth;
            shieldHealth = maxShieldHealth;
            return difference;
        }
    }


    public int getMaxDamageAbsorb() {
        return maxDamageAbsorb;
    }

    public void setMaxDamageAbsorb(int maxDamageAbsorb) {
        this.maxDamageAbsorb = maxDamageAbsorb;
    }

    public int getShieldHealth() {
        return shieldHealth;
    }

    public void setShieldHealth(int shieldHealth) {
        this.shieldHealth = shieldHealth;
    }

    public static Shield getTemplateShield (ShipChassis shipType){
        if (shipType == ShipChassis.DESTROYER) {
            return new Shield(12, 100, 1);
        }
        if (shipType == ShipChassis.FIGHTER) {
            return new Shield(10, 30, 0);
        }
        return null;
    }

    public void raiseShields () {
        shieldsUp = true;
    }

    public void lowerShields () {
        shieldsUp = false;
    }

    public int getMaxShieldHealth () {
        return maxShieldHealth;
    }

    public int getRegenRate () {
        return regenRate;
    }

    @Override
    public String toString () {
        String response = "Shield: ";
        if (shieldsUp) {
            response += "Up, ";
        } else {
            response += "Down, ";
        }
        response += shieldHealth + "/" + maxShieldHealth;
        return response;
    }

    public void setMaxDamageAbsorb(Integer maxDamageAbsorb) {
        this.maxDamageAbsorb = maxDamageAbsorb;
    }

    public void setShieldHealth(Integer shieldHealth) {
        this.shieldHealth = shieldHealth;
    }

    public void setMaxShieldHealth(Integer maxShieldHealth) {
        this.maxShieldHealth = maxShieldHealth;
    }

    public void setRegenRate(Integer regenRate) {
        this.regenRate = regenRate;
    }

    public Boolean getShieldsUp() {
        return shieldsUp;
    }

    public void setShieldsUp(Boolean shieldsUp) {
        this.shieldsUp = shieldsUp;
    }
}
