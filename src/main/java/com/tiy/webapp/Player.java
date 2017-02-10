package com.tiy.webapp;

import com.tiy.webapp.starship.Starship;
import com.tiy.webapp.starsys.Planet;
import com.tiy.webapp.starsys.StarSystem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by erronius on 12/20/2016.
 */
@Entity
@Table (name = "players")
public class Player {

    @GeneratedValue
    @Id
    private Integer id;

    @OneToMany
    private List<Planet> planets;

    @OneToMany
    private List<Starship> ships;

    int totalResearch;

    @Column(nullable = false)
    private String name;

    @OneToOne
    private StarSystem homeSystem;

    public Player(StarSystem homeSystem, String name) {
        planets = new ArrayList<>();
        ships = new ArrayList<>();

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
        /*int production = 0;
        int research = 0;
        for (Planet planet : planets) {
            production += planet.produceProduction();
            research += planet.produceResearch();
            planet.growPopulation();
        }
        System.out.println("Adding " + research + " research to pool.");
        totalResearch += research;
        System.out.println("Adding " + production + " production to " + shipyard.getName() + " Shipyard.");
        shipyard.addProductionToCurrentProject(production);
        if (totalResearch > tech.getResearchCost()) {
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
}
