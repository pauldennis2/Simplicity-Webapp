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

    @OneToMany (cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Planet> planets;

    @OneToMany (cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Starship> ships;

    @Column (nullable = false)
    private String name;

    @Column (nullable = false)
    private AlienRace race;

    @Column (nullable = false)
    private Integer productionPoolTotal;

    @Column
    private Integer researchPoolTotal;

    @OneToOne
    private StarSystem homeSystem;

    public Player () {

    }

    public Player(String name, AlienRace alienRace, StarSystem homeSystem) {
        planets = new ArrayList<>();
        ships = new ArrayList<>();
        this.name = name;
        this.race = alienRace;
        this.homeSystem = homeSystem;
        productionPoolTotal = 0;
        researchPoolTotal = 0;
    }

    public void addShip (Starship starship) {
        ships.add(starship);
    }

    public void addPlanet (Planet planet) {
        planets.add(planet);
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

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public AlienRace getRace() {
        return race;
    }

    public void setRace(AlienRace race) {
        this.race = race;
    }

    public Integer getProductionPoolTotal() {
        return productionPoolTotal;
    }

    public void setProductionPoolTotal(Integer productionPoolTotal) {
        this.productionPoolTotal = productionPoolTotal;
    }

    public Integer getResearchPoolTotal() {
        return researchPoolTotal;
    }

    public void setResearchPoolTotal(Integer researchPoolTotal) {
        this.researchPoolTotal = researchPoolTotal;
    }

    public StarSystem getHomeSystem() {
        return homeSystem;
    }

    public void setHomeSystem(StarSystem homeSystem) {
        this.homeSystem = homeSystem;
    }


}
