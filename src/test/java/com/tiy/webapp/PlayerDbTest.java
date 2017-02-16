package com.tiy.webapp;

import com.tiy.webapp.repos.PlanetRepo;
import com.tiy.webapp.repos.PlayerRepo;
import com.tiy.webapp.repos.ShipRepo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by Paul Dennis on 2/12/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PlayerDbTest {

    @Autowired
    PlayerRepo players;

    @Autowired
    ShipRepo ships;

    @Autowired
    PlanetRepo planets;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testPlayersAndShips () {
        Player player = new Player("Tester", null, null);
        players.save(player);
//        player.get
    }

    @Test
    public void testPlayersAndPlanets () {

    }
}