package com.tiy.webapp.starship;

/**
 * Created by erronius on 12/20/2016.
 */

//Weapon will probably be abstract later. for now we just have 2 possibilities
public /*abstract*/ class Weapon extends ShipComponent {

    private int damage;
    private WeaponClass type;
    private int shots;
    private boolean isLarge;

    public static final int DEFAULT_NUM_MISSILES = 3;

    public static final int SMALL_BEAM_PRODUCTION = 2;
    public static final int SMALL_MSL_PRODUCTION = 3;
    public static final int LARGE_BEAM_PRODUCTION = 4;
    public static final int LARGE_MSL_PRODUCTION = 6;

    public static final int SMALL_BEAM_DAMAGE = 15;
    public static final int SMALL_MSL_DAMAGE = 30;
    public static final int LARGE_BEAM_DAMAGE = 35;
    public static final int LARGE_MSL_DAMAGE = 70;

    public Weapon (WeaponClass type, boolean large) {
        super (large ? SlotType.LARGE_WEAPON : SlotType.SMALL_WEAPON, Weapon.getProductionCost(type, large));

        this.type = type;
        this.isLarge = large;
        if (type == WeaponClass.BEAM) {
            shots = -1;
            if (isLarge) {
                damage = LARGE_BEAM_DAMAGE;
            } else {
                damage = SMALL_BEAM_DAMAGE;
            }
        }
        if (type == WeaponClass.MISSILE) {
            shots = 3;
            if (isLarge) {
                damage = LARGE_MSL_DAMAGE;
            } else {
                damage = SMALL_MSL_DAMAGE;
            }
        }
    }

    public Weapon (WeaponClass type, boolean large, int customDamage) {
        super (large ? SlotType.LARGE_WEAPON : SlotType.SMALL_WEAPON, Weapon.getProductionCost(type, large));

        this.type = type;
        this.isLarge = large;
        this.damage = customDamage;
        if (type == WeaponClass.BEAM) {
            shots = -1;
        }
        if (type == WeaponClass.MISSILE) {
            shots = 3;
        }
    }

    //Note: this is NOT an accessor method. It actually "fires a shot" returning the
    //damage to be applied to the enemy ship
    public int getFiredWeaponDamage () {
        if (shots == -1) {
            //Beam weapon, simply return damage
            return damage;
        }
        if (shots > 0) {
            shots--;
            return damage;
        }
        return 0;
    }

    public WeaponClass getWeaponClass () {
        return type;
    }

    public static int getProductionCost (WeaponClass type, boolean large) {
        if (type == WeaponClass.BEAM) {
            if (large) {
                return LARGE_BEAM_PRODUCTION;
            } else {
                return SMALL_BEAM_PRODUCTION;
            }
        } else if (type == WeaponClass.MISSILE) {
            if (large) {
                return LARGE_MSL_PRODUCTION;
            } else {
                return SMALL_MSL_PRODUCTION;
            }
        } else {
            throw new AssertionError("Not implemented");
        }
    }

    @Override
    public String toString () {
        if (type == WeaponClass.BEAM) {
            if (isLarge) {
                return "Beam (l)";
            } else {
                return "Beam (s)";
            }
        } else if (type == WeaponClass.MISSILE) {
            if (isLarge) {
                return "Missile (l)";
            } else {
                return "Missile (s)";
            }
        } else {
            throw new AssertionError("Not implemented");
        }
    }
}
