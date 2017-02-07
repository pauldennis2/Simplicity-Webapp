package com.tiy.webapp.starship;


import com.tiy.webapp.IllegalMoveException;
import com.tiy.webapp.Player;
import com.tiy.webapp.starsys.Location;
import com.tiy.webapp.starsys.SpaceTunnel;

/**
 * Created by erronius on 12/20/2016.
 */
public class Fighter extends Starship {

    Starship attachedTo;
    boolean attached;

    public Fighter (Location location, Player owner) {
        super(location, owner);
        initializeVars();
    }

    public Fighter (Location location, Player owner, StarshipSetup setup) {
        super(location, owner);
        this.setWeapons(setup.getWeaponList());
        initializeVars();
    }

    public Fighter (Location location, Player owner, String name) {
        super (location, owner, name);
        initializeVars();
    }

    public void initializeVars () {
        fighterBerths = 0;
        health = ShipChassis.FIGHTER.getHealth();
        maxHealth = health;
        shield = Shield.getTemplateShield(ShipChassis.FIGHTER);
        generator = Generator.getTemplateGenerator(ShipChassis.FIGHTER);
    }

    @Override
    public void enterTunnel (SpaceTunnel tunnel) throws IllegalMoveException {
        throw new IllegalMoveException("Fighters can't enter tunnels");
    }

    public void attachTo (Starship starship) throws IllegalMoveException {
        int berths = starship.fighterBerths;
        if (!this.getLocation().equals(starship.getLocation())) {
            throw new IllegalMoveException("Cannot attach - not in same system");
        }

        if (berths > starship.getAttachedFighters().size()) {
            //Room at the inn
            starship.attach(this);
            attachedTo = starship;
            attached = true;
            location = starship;
        } else {
            throw new IllegalMoveException("Cannot attach; no berths available.");
        }
    }

    @Override
    public String toString () {
        return this.getName() + " (Fighter) @" + location.getName();
    }
}
