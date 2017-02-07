package com.tiy.webapp.starship;


import com.tiy.webapp.IllegalMoveException;
import com.tiy.webapp.ImproperFunctionInputException;
import com.tiy.webapp.Player;
import com.tiy.webapp.starsys.Location;
import com.tiy.webapp.starsys.SpaceTunnel;
import com.tiy.webapp.starsys.StarSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erronius on 12/20/2016.
 */
public abstract class Starship extends Location {

    private String name;

    Shield shield;
    Generator generator;

    int fighterBerths;
    private List<Weapon> weapons;

    int health;
    int maxHealth;

    boolean isDestroyed; //This will be replaced later by logic

    Player owner;


    private List<Fighter> attachedFighters;
    private StarSystem immediateDestination;
    private StarSystem finalDestination; //for later
    private int turnsToDestination;

    public static final String[] SHIP_NAMES = {"Voyager", "Enterprise", "Defiant", "Valiant",
            "Wuddshipp", "Ariel", "Kestrel", "Lightning", "Tornado", "Artanis"};
    public static final String[] SHIP_PREFIXES = {"Starship", "Warship", "Vessel"};
    public static int namesIndex = 0;
    public static int prefixIndex = 0;

    //StarSystem currentSystem;
    Location location;

    private boolean inTunnel;

    public Starship(Location location, Player owner) {
        name = SHIP_PREFIXES[prefixIndex] + " " + SHIP_NAMES[namesIndex];

        namesIndex++;
        if (namesIndex > SHIP_NAMES.length - 1) {
            namesIndex = 0;
            prefixIndex++;
            if (prefixIndex > SHIP_PREFIXES.length - 1) {
                prefixIndex = 0;
            }
        }
        this.location = location;
        attachedFighters = new ArrayList<>();
        isDestroyed = false;
        this.owner = owner;
    }

    public Starship (Location location, Player owner, String name) {
        this.name = name;
        this.location = location;
        attachedFighters = new ArrayList<>();
        isDestroyed = false;
        this.owner = owner;
    }

    public void startTurn () {
        generator.generatePower();
        int availablePower = generator.getAvailablePower();
        if (shield.shieldsUp()) {
            //Check to make sure we have enough power to run shields
            if (availablePower < shield.getMaxDamageAbsorb()) {
                shield.lowerShields();
            } else {
                //Pay for running shields
                availablePower -= shield.getMaxDamageAbsorb();
            }
        }
        int powerUsedToRegen = shield.regenerate(availablePower);
        availablePower -= powerUsedToRegen;
        generator.returnUnusedPower(availablePower);
    }

    public static Starship buildSpaceShip () {
        return null;
    }

    public boolean isInTunnel () {
        return inTunnel;
    }

    public void enterTunnel (SpaceTunnel tunnel) throws IllegalMoveException {
        if (inTunnel) {
            throw new IllegalMoveException("Cannot enter a tunnel, already in one");
        }
        immediateDestination = tunnel.getOtherSystem((StarSystem) location);
        if (!tunnel.getOtherSystem(immediateDestination).equals((StarSystem) location)) {
            throw new IllegalMoveException("This tunnel is not connected to the current system");
        }
        inTunnel = true;
        turnsToDestination = tunnel.getLength();

        location = tunnel;
    }

    public boolean attach (Fighter fighter) {
        //This should have already been checked
        if (attachedFighters.size() < fighterBerths) {
            attachedFighters.add(fighter);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Fire as many weapons as possible, returning the total damage to apply to the enemy ship. Won't work for later
     * since we'll need to know the type of each attack (to see if the enemy ship can dodge it for example).
     * @return total weapon damage
     */
    public int fireAllWeapons () {
        int availablePower = generator.getAvailablePower();
        int totalDamage = 0;
        for (Weapon weapon : weapons) {
            if (weapon.getWeaponClass() == WeaponClass.BEAM) {
                //requires power
                int weaponPower = weapon.getFiredWeaponDamage();
                if (availablePower > weaponPower) {
                    totalDamage += weaponPower;
                    availablePower -= weaponPower;
                }
            }
            if (weapon.getWeaponClass() == WeaponClass.MISSILE) {
                //no power required, just ammo (checked in Weapon class)
                totalDamage += weapon.getFiredWeaponDamage();
            }
        }
        generator.returnUnusedPower(availablePower);
        return totalDamage;
    }

    public StarSystem getImmediateDestination() {
        return immediateDestination;
    }

    public StarSystem getFinalDestination() {
        return finalDestination;
    }

    public int getTurnsToDestination() {
        return turnsToDestination;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation (Location newLocation) throws IllegalMoveException {
        //For now, in order to follow business rules, the only valid set comes in moving
        //a ship out of shipyard. Other sets will be valid later

        if (location.getClass() == Shipyard.class) {
            StarSystem shipyardSystem = ((Shipyard)location).getSystem();
            if (newLocation.equals(shipyardSystem)) {
                location = shipyardSystem;
            } else {
                throw new IllegalMoveException ("I'm at a Shipyard that is not in the system you want to move me to");
            }
        } else {
            throw new IllegalMoveException("I'm not at a shipyard; you will have to use tunnels to move me");
        }
    }

    public void moveToDestination () {
        if (inTunnel) {
            turnsToDestination--;
            if (turnsToDestination == 0) {
                //pop out
                inTunnel = false;
                location = immediateDestination;
            }
        } else {
            System.out.println("Not in a tunnel. Not moving.");
        }
    }

    public List<Fighter> getAttachedFighters () {
        return attachedFighters;
    }

    public void setWeapons (List<Weapon> weaponList) {
        this.weapons = weaponList;
    }

    /**
     * Possibly temporary. Written for testing
     */
    public void drainPower () {
        generator.getAvailablePower();
    }

    public void takeDamage (int damage) throws ImproperFunctionInputException {
        if (damage < 0) {
            throw new ImproperFunctionInputException("Cannot take negative damage");
        }
        if (shield.shieldsUp()) {
            int maxDamageAbsorb = shield.getMaxDamageAbsorb();
            int powerAvailable = generator.getAvailablePower();

            if (powerAvailable >= maxDamageAbsorb) {
                int returnedDamage = shield.takeDamage(damage);
                health -= returnedDamage;
                powerAvailable -= damage - returnedDamage; //Subtract power equal to the amount of damage absorbed by shield
                generator.returnUnusedPower(powerAvailable);
            } else { //powerAvailable < maxDamageAbsorb
                int returnedDamage = shield.takeDamage(powerAvailable);
                health -= returnedDamage;
                //powerAvailable = 0;
            }
        } else {
            health -= damage;
        }
        if (health <= 0) {
            isDestroyed = true;
            location = new StarSystem("Junk Pile");
        }
    }

    public int getHealth () {
        return health;
    }

    public boolean isDestroyed () {
        return isDestroyed;
    }

    public void setOwner (Player owner) {
        this.owner = owner;
    }

    public Shield getShield () {
        return shield;
    }

    public Generator getGenerator () {
        return generator;
    }

    @Override
    public String toString () {
        String response = name + " @" + location.getName();
        return response;
    }

    public String getName () {
        return name;
    }

    public int getMaxHealth () {
        return maxHealth;
    }

}
