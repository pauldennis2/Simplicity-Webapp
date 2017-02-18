package com.tiy.webapp.starship;


import com.tiy.webapp.starsys.SpaceTunnel;
import com.tiy.webapp.starsys.StarSystem;

import javax.persistence.*;

/**
 * Created by erronius on 12/20/2016.
 */
@Entity
@Table (name = "starships")
public class Starship {

    @GeneratedValue
    @Id
    Integer id;

    @ManyToOne
    StarSystem starSystem;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    Integer damage;

    @Column(nullable = false)
    private Integer health;

    @Column(nullable = false)
    private Integer maxHealth;

    @Column(nullable = true)
    private Integer turnsToDestination;

    @Column(nullable = false)
    private Boolean isDestroyed;

    @Column(nullable = true)
    private Integer maxDamageAbsorb;

    @Column(nullable = true)
    private Integer shieldHealth;

    @Column(nullable = true)
    private Integer maxShieldHealth;

    @Column(nullable = true)
    private Integer regenRate;

    @Column (nullable = true)
    private Integer currentReservePower;

    @Column (nullable = true)
    private Integer maxReservePower;

    @Column (nullable = true)
    private Integer powerPerTurn;

    @Column(nullable = true)
    private Boolean shieldsUp;

    @Column (nullable = false)
    ShipChassis chassis;

    @Column (nullable = false)
    private String imageString;

    @Column (nullable = false)
    private Integer ownerRaceNum;

    public Starship() {

    }

    public void takeDamage (int damage) {
        int tempMaxDamageAbsorb = maxDamageAbsorb;
        while (damage > 0 && shieldHealth > 0 && currentReservePower > 0 && tempMaxDamageAbsorb > 0) {
            damage--;
            shieldHealth--;
            currentReservePower--;
            tempMaxDamageAbsorb--;
        }
        if (damage > health) {
            health = 0;
        } else {
            health -= damage;
        }
    }

    /*public void takeDamage (int damage) {
        //Shield part first
        if (currentReservePower >= maxDamageAbsorb) {//If we can afford to fully take a hit on shields energy-wise
            if (shieldHealth >= damage) { //If we can afford to fully take the hit on shield shield health-wise
                shieldHealth -= maxDamageAbsorb;
                damage -= maxDamageAbsorb;
                currentReservePower -= maxDamageAbsorb;
            }
        }
        //Health part 2nd
    }*/

    /*public void takeDamage (int damage) {
        if (currentReservePower >= maxDamageAbsorb) {
            damage -= maxDamageAbsorb;
            currentReservePower -= maxDamageAbsorb;
            shieldHealth -= maxDamageAbsorb;
        } else {
            damage -= currentReservePower;
            currentReservePower = 0;
        }
        if (damage <= 0) {
            return;
        }
        if (damage >= health) {
            health = 0;
        } else {
            health -= damage;
        }
    }*/

    public Starship(StarSystem starSystem, ShipChassis chassis, String name, String imageString, Integer ownerRaceNum) {
        this.name = name;
        this.starSystem = starSystem;
        isDestroyed = false;
        this.chassis = chassis;
        health = chassis.getHealth();
        maxHealth = chassis.getHealth();

        Shield shield = chassis.getShield();
        if (shield != null) {
            maxDamageAbsorb = shield.getMaxDamageAbsorb();
            shieldHealth = shield.getShieldHealth();
            maxShieldHealth = shield.getMaxShieldHealth();
            regenRate = shield.getRegenRate();
        }
        damage = chassis.getDamage();

        Generator generator = chassis.getGenerator();
        if (generator != null) {
            powerPerTurn = generator.getPowerPerTurn();
            maxReservePower = generator.getMaxReservePower();
            currentReservePower = maxReservePower;
        }
        this.imageString = imageString;
        this.ownerRaceNum = ownerRaceNum;
    }

    public Starship (ShipChassis chassis) {
        health = chassis.getHealth();
        maxHealth = chassis.getHealth();
        Shield shield = chassis.getShield();
        if (shield != null) {
            maxDamageAbsorb = shield.getMaxDamageAbsorb();
            shieldHealth = shield.getShieldHealth();
            maxShieldHealth = shield.getMaxShieldHealth();
            regenRate = shield.getRegenRate();
        }
        damage = chassis.getDamage();

        Generator generator = chassis.getGenerator();
        if (generator != null) {
            powerPerTurn = generator.getPowerPerTurn();
            maxReservePower = generator.getMaxReservePower();
            currentReservePower = maxReservePower;
        }
    }

    public void startTurn () {
        currentReservePower += powerPerTurn;
        if (shieldsUp) {
            if (currentReservePower < maxDamageAbsorb) {
                shieldsUp = false;
            } else {
                currentReservePower -= maxDamageAbsorb;
            }
        }
        int powerUsedToRegen = regenerateShields(currentReservePower);
        currentReservePower -= powerUsedToRegen;
    }

    public int regenerateShields (int maxRegenAmount) {
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

    public void enterTunnel (SpaceTunnel tunnel, StarSystem destination) {
        starSystem = destination;
        turnsToDestination = tunnel.getLength();
    }

    public void moveToDestination () {
        if (turnsToDestination != null) {
            if (turnsToDestination > 0) {
                turnsToDestination--;
            }
        }
    }

    /*public void takeDamage (int damage) {
        if (shieldsUp) {
            if (currentReservePower >= maxDamageAbsorb) {
                int returnedDamage = takeShieldDamage(damage);
                health -= returnedDamage;
                currentReservePower -= damage - returnedDamage;
            } else {
                takeShieldDamage(currentReservePower);
                health -= damage - currentReservePower;
                currentReservePower = 0;
            }
        } else {
            health -= damage;
        }
        if (health <= 0) {
            isDestroyed = true;
        }
    }

    private int takeShieldDamage (int damageAmount) {
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
    }*/

    public int getHealth () {
        return health;
    }

    public String getName () {
        return name;
    }

    public int getMaxHealth () {
        return maxHealth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public void setMaxHealth(Integer maxHealth) {
        this.maxHealth = maxHealth;
    }

    public Integer getTurnsToDestination() {
        return turnsToDestination;
    }

    public void setTurnsToDestination(Integer turnsToDestination) {
        this.turnsToDestination = turnsToDestination;
    }

    public Boolean getDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(Boolean destroyed) {
        isDestroyed = destroyed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaxDamageAbsorb() {
        return maxDamageAbsorb;
    }

    public void setMaxDamageAbsorb(Integer maxDamageAbsorb) {
        this.maxDamageAbsorb = maxDamageAbsorb;
    }

    public Integer getShieldHealth() {
        return shieldHealth;
    }

    public void setShieldHealth(Integer shieldHealth) {
        this.shieldHealth = shieldHealth;
    }

    public Integer getMaxShieldHealth() {
        return maxShieldHealth;
    }

    public void setMaxShieldHealth(Integer maxShieldHealth) {
        this.maxShieldHealth = maxShieldHealth;
    }

    public Integer getRegenRate() {
        return regenRate;
    }

    public void setRegenRate(Integer regenRate) {
        this.regenRate = regenRate;
    }

    public Integer getCurrentReservePower() {
        return currentReservePower;
    }

    public void setCurrentReservePower(Integer currentReservePower) {
        this.currentReservePower = currentReservePower;
    }

    public Integer getMaxReservePower() {
        return maxReservePower;
    }

    public void setMaxReservePower(Integer maxReservePower) {
        this.maxReservePower = maxReservePower;
    }

    public Integer getPowerPerTurn() {
        return powerPerTurn;
    }

    public void setPowerPerTurn(Integer powerPerTurn) {
        this.powerPerTurn = powerPerTurn;
    }

    public Boolean getShieldsUp() {
        return shieldsUp;
    }

    public void setShieldsUp(Boolean shieldsUp) {
        this.shieldsUp = shieldsUp;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public StarSystem getStarSystem() {
        return starSystem;
    }

    public void setStarSystem(StarSystem starSystem) {
        this.starSystem = starSystem;
    }

    public ShipChassis getChassis() {
        return chassis;
    }

    public void setChassis(ShipChassis chassis) {
        this.chassis = chassis;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public Integer getOwnerRaceNum() {
        return ownerRaceNum;
    }

    public void setOwnerRaceNum(Integer ownerRaceNum) {
        this.ownerRaceNum = ownerRaceNum;
    }
}
