package com.tiy.webapp;

import com.tiy.webapp.starsys.StarSystemGraph;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by Paul Dennis on 2/9/2017.
 */
//@Entity
//@Table (name = "games")
public class Game {

    @GeneratedValue
    @Id
    Integer id;

    List<Player> players;
    StarSystemGraph starSystemGraph;
}
