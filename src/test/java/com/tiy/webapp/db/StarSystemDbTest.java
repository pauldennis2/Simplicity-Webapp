package com.tiy.webapp.db;

import com.tiy.webapp.repos.ShipRepo;
import com.tiy.webapp.starship.*;
import com.tiy.webapp.repos.PlanetRepo;
import com.tiy.webapp.starsys.StarSystem;
import com.tiy.webapp.repos.StarSystemRepo;
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
    PlanetRepo planets;

    @Autowired
    ShipRepo ships;

    Starship savedDestroyer;
    StarSystem testSystem;
    StarSystem testSystem2;



    @Before
    public void setUp() throws Exception {
        System.out.println("Setup");
        testSystem = new StarSystem("Beetlejuice", 4, 5);
        systems.save(testSystem);
        Starship destroyer = new Starship(testSystem, ShipChassis.DESTROYER);
        List<Weapon> weaponList = new ArrayList<>();
        weaponList.add(new Weapon(WeaponClass.BEAM, false));
        destroyer.setWeapons(weaponList);
        savedDestroyer = ships.save(destroyer);

        testSystem2 = new StarSystem("Omega", 2, 2);

    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Teardown");


    }

    @Test
    public void testShipCrud () {
        assertFalse(systems == null || planets == null || ships == null);

        Starship retrievedDestroyer = ships.findOne(savedDestroyer.getId());
        assertNotNull(retrievedDestroyer);
        List<Weapon> weapons = retrievedDestroyer.getWeapons();
        assertEquals(1, weapons.size());
        assertEquals(WeaponClass.BEAM, weapons.get(0).getWeaponClass());

        Generator generator = retrievedDestroyer.getGenerator();
        assertNotNull(generator);
        assertEquals(ShipChassis.DESTROYER.getGenerator().getMaxReservePower(), generator.getMaxReservePower());

        Shield shield = retrievedDestroyer.getShield();
        assertNotNull(shield);
        assertEquals(ShipChassis.DESTROYER.getShield().getRegenRate(), shield.getRegenRate());
    }

    @Test
    public void testSystemAndPlanetCrud () {

    }
}