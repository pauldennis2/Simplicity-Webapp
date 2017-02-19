package com.tiy.webapp.starship;

/**
 * Created by erronius on 12/20/2016.
 */
public enum ShipChassis {
    //ShipChassis Constructor:
    COLONIZER (50, 60, 0),

    FIGHTER (25, 20, 8),
    DESTROYER (100, 80, 30),
    CRUISER (150, 100, 45);
    //CRUISER (data),
    //BATTLESHIP (data),
    //CAPITOL_SHIP (data);

    private final int health;
    private final int baseProductionCost;
    private final int damage;

    ShipChassis(int health, int baseProductionCost, int damage) {
        this.health = health;
        this.baseProductionCost = baseProductionCost;
        this.damage = damage;
    }

    public int getHealth() {
        return health;
    }

    public static ShipChassis getShipChassis (String name) {
        name = name.toLowerCase();
        switch (name) {
            case "fighter":
                return FIGHTER;
            case "destroyer":
                return DESTROYER;
            /*case "cruiser":
                return CRUISER;
            case "battleship":
                return BATTLESHIP;
            case "capitol ship":
                return CAPITOL_SHIP;*/
            default:
                return null;
        }
    }

    public Shield getShield () {
        return Shield.getTemplateShield(this);
    }

    public Generator getGenerator () {
        return Generator.getTemplateGenerator(this);
    }

    public int getBaseProductionCost () {
        return baseProductionCost;
    }

    public int getDamage() {
        return damage;
    }
}
