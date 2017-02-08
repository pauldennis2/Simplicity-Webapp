package com.tiy.webapp;

import com.tiy.webapp.starship.Shipyard;
import com.tiy.webapp.starship.Starship;
import com.tiy.webapp.starship.Weapon;
import com.tiy.webapp.starship.WeaponClass;
import com.tiy.webapp.starsys.Planet;
import com.tiy.webapp.starsys.StarSystem;
import com.tiy.webapp.starsys.Technology;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by erronius on 12/20/2016.
 */
public class Player {

    @GeneratedValue
    @Id
    private Integer id;

    @OneToMany
    private List<Planet> planets;

    @OneToMany
    private List<Starship> ships;

    @OneToOne
    private Shipyard shipyard;

    int totalResearch;

    @Column(nullable = false)
    private String name;

    @OneToOne
    private StarSystem homeSystem;

    public Player(StarSystem homeSystem, String name) {
        planets = new ArrayList<>();
        ships = new ArrayList<>();
        shipyard = new Shipyard(homeSystem, this);

        planets.add(homeSystem.getPlanets().get(0));//BAD (we shouldn't be assuming this. works for now) todo fix
        totalResearch = 0;
        this.name = name;
        this.homeSystem = homeSystem;
    }

    public void addShip (Starship starship) {
        ships.add(starship);
    }

    public void addPlanet (Planet planet) {
        /*if (planet.getOwner().equals(this)) {
            planets.add(planet);
        } else {
            throw new AssertionError("Attempted to add a planet I don't own.");
        }*/
        throw new AssertionError("Fix");
    }

    public void doTurn () {
        int production = 0;
        int research = 0;
        for (Planet planet : planets) {
            production += planet.getProduction();
            research += planet.getResearch();
            planet.growPopulation();
        }
        System.out.println("Adding " + research + " research to pool.");
        totalResearch += research;
        System.out.println("Adding " + production + " production to " + shipyard.getName() + " Shipyard.");
        shipyard.addProductionToCurrentProject(production);
        /*if (totalResearch > tech.getResearchCost()) {
            tech.setComplete(true);
        }*/
        throw new AssertionError("Fix");

    }

    public void moveShips () {
        for (Starship starship : ships) {
            starship.moveToDestination();
        }
    }

    public String getName () {
        return name;
    }

    public void removeShip (Starship ship) {
        ships.remove(ship);
    }

    public StarSystem getHomeSystem () {
        return homeSystem;
    }

    public List<Planet> getPlanets () {
        return planets;
    }

    public List<Starship> getShips () {
        return ships;
    }

    public Shipyard getShipyard () {
        return shipyard;
    }

    /*public List<Weapon> getAvailableSmallWeaps () {
        List<Weapon> smallWeaps = new ArrayList<>();
        smallWeaps.add(new Weapon(WeaponClass.BEAM, false));
        smallWeaps.add(new Weapon(WeaponClass.MISSILE, false));
        return smallWeaps;
    }

    public List<Weapon> getAvailableLargeWeaps () {
        List<Weapon> largeWeaps = new ArrayList<>();
        largeWeaps.add(new Weapon(WeaponClass.BEAM, true));
        largeWeaps.add(new Weapon(WeaponClass.MISSILE, true));
        return largeWeaps;
    }*/
}
