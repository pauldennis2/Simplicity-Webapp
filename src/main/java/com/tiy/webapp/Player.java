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

    @Column
    private Integer totalResearch;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private AlienRace race;

    public Player () {

    }

    public Player(String name) {
        planets = new ArrayList<>();
        ships = new ArrayList<>();
        totalResearch = 0;
        this.name = name;
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


    public List<Planet> getPlanets () {
        return planets;
    }

    public List<Starship> getShips () {
        return ships;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    public void setShips(List<Starship> ships) {
        this.ships = ships;
    }

    public int getTotalResearch() {
        return totalResearch;
    }

    public void setTotalResearch(int totalResearch) {
        this.totalResearch = totalResearch;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTotalResearch(Integer totalResearch) {
        this.totalResearch = totalResearch;
    }

    public AlienRace getRace() {
        return race;
    }

    public void setRace(AlienRace race) {
        this.race = race;
    }
}
