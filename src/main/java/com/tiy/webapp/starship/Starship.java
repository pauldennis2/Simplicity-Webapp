package com.tiy.webapp.starship;


import com.tiy.webapp.IllegalMoveException;
import com.tiy.webapp.ImproperFunctionInputException;
import com.tiy.webapp.Player;
import com.tiy.webapp.starsys.Location;
import com.tiy.webapp.starsys.SpaceTunnel;
import com.tiy.webapp.starsys.StarSystem;
import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;
import java.util.List;

/**
 * Created by erronius on 12/20/2016.
 */
@Entity
@Table (name = "starships")
public class Starship {

    public static final String[] SHIP_NAMES = {"Voyager", "Enterprise", "Defiant", "Valiant",
            "Wuddshipp", "Ariel", "Kestrel", "Lightning", "Tornado", "Artanis"};
    public static final String[] SHIP_PREFIXES = {"Starship", "Warship", "Vessel"};

    @GeneratedValue
    @Id
    Integer id;

    @Column(nullable = false)
    private String name;

    //@Column(nullable = true)
    @OneToOne
    private Shield shield;

    //@Column(nullable = true)
    @OneToOne(cascade = CascadeType.ALL)
    private Generator generator;

    @Column(nullable = false)
    Integer fighterBerths;

    @OneToMany
    private List<Weapon> weapons;

    @Column(nullable = false)
    private Integer health;

    @Column(nullable = false)
    private Integer maxHealth;

    @Column(nullable = true)
    private Integer turnsToDestination;

    @Column(nullable = false)
    private Boolean isDestroyed;

    public static int namesIndex = 0;
    public static int prefixIndex = 0;

    //List<Fighter> attachedFighters;

    //@Column(nullable = false)
    @ManyToOne
    Location location;

    //@Column(nullable = true)
    @ManyToOne
    StarSystem destination;

    @Column (nullable = false)
    ShipChassis chassis;

    public Starship() {
    }

    public Starship(Location location, ShipChassis chassis) {
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
        this.chassis = chassis;
        isDestroyed = false;
        fighterBerths = chassis.getFighterBerths();
        health = chassis.getHealth();
        maxHealth = chassis.getHealth();
        generator = chassis.getGenerator();
    }

    public Starship (Location location, ShipChassis chassis, String name) {
        this.name = name;
        this.location = location;
        isDestroyed = false;
        this.chassis = chassis;
        fighterBerths = chassis.getFighterBerths();
        health = chassis.getHealth();
        maxHealth = chassis.getHealth();
        generator = chassis.getGenerator();
        shield = chassis.getShield();
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

    public void enterTunnel (SpaceTunnel tunnel) throws IllegalMoveException {
        /*if (inTunnel) {
            throw new IllegalMoveException("Cannot enter a tunnel, already in one");
        }
        immediateDestination = tunnel.getOtherSystem((StarSystem) location);
        if (!tunnel.getOtherSystem(immediateDestination).equals((StarSystem) location)) {
            throw new IllegalMoveException("This tunnel is not connected to the current system");
        }
        inTunnel = true;
        turnsToDestination = tunnel.getLength();

        location = tunnel;
        */
        throw new AssertionError("Fix");
    }

    /*public boolean attach (Fighter fighter) {
        //This should have already been checked
        if (attachedFighters.size() < fighterBerths) {
            attachedFighters.add(fighter);
            return true;
        } else {
            return false;
        }
    }*/

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

    public void moveToDestination () {
        if (turnsToDestination > 0) {
            turnsToDestination--;
            if (turnsToDestination == 0) {
                location = destination;
                destination = null;
            }
        }
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

    public void setName(String name) {
        this.name = name;
    }

    public void setShield(Shield shield) {
        this.shield = shield;
    }

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    public Integer getFighterBerths() {
        return fighterBerths;
    }

    public void setFighterBerths(Integer fighterBerths) {
        this.fighterBerths = fighterBerths;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
