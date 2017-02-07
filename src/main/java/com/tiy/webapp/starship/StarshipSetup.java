package com.tiy.webapp.starship;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erronius on 12/22/2016.
 */
public class StarshipSetup {

    /*This class might be temporary.
        Basic concept is that this is the box containing all the stuff we want to
        put in a starship. We'll pass it into the constructor for the starship and the
        ship will unpack it.
     */

    private List<Weapon> weaponList;
    private Shield shield;

    public StarshipSetup(ShipChassis shipType) {
        weaponList = new ArrayList<Weapon>();
        if (shipType == ShipChassis.FIGHTER) {
            Weapon smallBeamWeapon = new Weapon(WeaponClass.BEAM, false);
            Weapon smallMsilWeapon = new Weapon(WeaponClass.MISSILE, false);
            weaponList.add(smallBeamWeapon);
            weaponList.add(smallMsilWeapon);
        }
        if (shipType == ShipChassis.DESTROYER) {
            Weapon smallBeamWeapon = new Weapon(WeaponClass.BEAM, false);
            Weapon smallMsilWeapon = new Weapon(WeaponClass.MISSILE, false);
            Weapon largeBeamWeapon = new Weapon(WeaponClass.BEAM, true);
            weaponList.add(smallBeamWeapon);
            weaponList.add(smallMsilWeapon);
            weaponList.add(largeBeamWeapon);
        }

    }

    public List<Weapon> getWeaponList () {
        return weaponList;
    }
}
