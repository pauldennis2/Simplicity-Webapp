package com.tiy.webapp.starship;


import com.tiy.webapp.Player;
import com.tiy.webapp.starsys.Location;

/**
 * Created by erronius on 12/20/2016.
 */
public class Destroyer extends Starship {

    public Destroyer (Location location, Player owner) {
        super(location, owner);
        initializeVars();
    }

    public Destroyer (Location location, Player owner, StarshipSetup setup) {
        super(location, owner);
        this.fighterBerths = 2;
        this.setWeapons(setup.getWeaponList());
        initializeVars();
    }

    public Destroyer (Location location, Player owner, String name) {
        super(location, owner, name);
        initializeVars();
    }

    public void initializeVars () {
        fighterBerths = ShipChassis.DESTROYER.getFighterBerths();
        health = ShipChassis.DESTROYER.getHealth();
        maxHealth = health;
        shield = Shield.getTemplateShield(ShipChassis.DESTROYER);
        generator = Generator.getTemplateGenerator(ShipChassis.DESTROYER);
    }

    @Override
    public String toString () {
        return this.getName() + " (Destroyer) @" + location.getName();
    }
}
