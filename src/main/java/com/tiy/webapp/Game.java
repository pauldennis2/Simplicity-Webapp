package com.tiy.webapp;

import com.tiy.webapp.starsys.StarSystemGraph;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Paul Dennis on 2/9/2017.
 */
@Entity
@Table (name = "games")
public class Game {

    @GeneratedValue
    @Id
    private Integer id;

    @Column (nullable = false, unique = true)
    private String name;

    @OneToMany (cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Player> players;

    @OneToOne (cascade = CascadeType.ALL,
            orphanRemoval = true)
    private StarSystemGraph starSystemGraph;

    @Column (nullable = false)
    private Integer turnNumber;

    public Game () {

    }

    public Game (String name, List<Player> players, StarSystemGraph starSystemGraph) {
        this.name = name;
        this.players = players;
        this.starSystemGraph = starSystemGraph;
        turnNumber = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public StarSystemGraph getStarSystemGraph() {
        return starSystemGraph;
    }

    public void setStarSystemGraph(StarSystemGraph starSystemGraph) {
        this.starSystemGraph = starSystemGraph;
    }

    public Integer getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(Integer turnNumber) {
        this.turnNumber = turnNumber;
    }
}
