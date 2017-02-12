package com.tiy.webapp.db;

import com.tiy.webapp.repos.*;
import com.tiy.webapp.starship.*;
import com.tiy.webapp.starsys.SpaceTunnel;
import com.tiy.webapp.starsys.StarSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Paul Dennis on 2/7/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StarSystemDbTest {

    @Autowired
    StarSystemRepo systems;

    @Autowired
    ShipRepo ships;

    @Autowired
    TunnelRepo tunnels;

    Starship destroyer;
    StarSystem testSystem;
    StarSystem testSystem2;
    SpaceTunnel tunnel;

    Integer shipId;

    @Before
    public void setUp() throws Exception {
        System.out.println("Setup");
        testSystem = new StarSystem("Beetlejuice", 4, 5);
        systems.save(testSystem);
        destroyer = new Starship(testSystem, ShipChassis.DESTROYER, "Test", null);
        shipId = ships.save(destroyer).getId();

        testSystem2 = new StarSystem("Omega", 2, 2);
        systems.save(testSystem2);
        tunnel = new SpaceTunnel(2, testSystem, testSystem2);
        tunnels.save(tunnel);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Teardown");
        //ships.save(destroyer);
        //tunnels.save(tunnel);
        //systems.save(testSystem);
        //systems.save(testSystem2);
        ships.delete(destroyer);
        assertNotNull(tunnels.findOne(tunnel.getId()));
        systems.delete(testSystem);
        assertNull(tunnels.findOne(tunnel.getId()));
        systems.delete(testSystem2);
    }

    @Test
    public void testShipCrud () {
        assertFalse(systems == null || tunnels == null || ships == null);

        Starship retrievedDestroyer = ships.findOne(shipId);
        assertNotNull(retrievedDestroyer);
    }

}