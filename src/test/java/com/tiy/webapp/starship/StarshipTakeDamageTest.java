package com.tiy.webapp.starship;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Paul Dennis on 2/17/2017.
 */
public class StarshipTakeDamageTest {

    Starship ship;
    int MAX_DAMAGE_ABSORB;
    int MAX_SHIELD_HEALTH;
    int MAX_HEALTH;
    int MAX_RESERVE_POWER;

    @Before
    public void setUp() throws Exception {
        ship = new Starship(ShipChassis.DESTROYER);
        MAX_DAMAGE_ABSORB = ship.getMaxDamageAbsorb();
        MAX_SHIELD_HEALTH = ship.getShieldHealth();
        MAX_HEALTH = ship.getMaxHealth();
        MAX_RESERVE_POWER = ship.getMaxReservePower();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testStarshipBasicTakeDamage () {
        ship.takeDamage(MAX_DAMAGE_ABSORB + 5);
        assertEquals(ship.getHealth(), ShipChassis.DESTROYER.getHealth() - 5);
        assertEquals((int)ship.getCurrentReservePower(), MAX_RESERVE_POWER - MAX_DAMAGE_ABSORB);
        assertEquals((int)ship.getShieldHealth(), MAX_SHIELD_HEALTH - MAX_DAMAGE_ABSORB);
    }

    @Test
    public void testStarshipMultipleSmallHits () {
        for (int i = 0; ship.getShieldHealth() > 0; i++) {
            ship.takeDamage(5);
            assertEquals(ship.getHealth(), ship.getMaxHealth());
            //This is OK only because we happen to know generators have more life that shields
            assertEquals((int)ship.getCurrentReservePower(), MAX_RESERVE_POWER - (i+1)*5);
            assertEquals((int)ship.getShieldHealth(), MAX_SHIELD_HEALTH - (i+1)*5);
        }
    }

    @Test
    public void testNoEnergy () {
        ship.setCurrentReservePower(0);
        ship.takeDamage(20);
        assertEquals(ship.getHealth(), ship.getMaxHealth() - 20);
        assertEquals((int)ship.getCurrentReservePower(), 0);
        assertEquals((int) ship.getShieldHealth(), MAX_SHIELD_HEALTH);
    }

    @Test
    public void testLowEnergyJeb () {
        ship.setCurrentReservePower(5);
        ship.takeDamage(20);
        assertEquals(ship.getHealth(), ship.getMaxHealth() - 15);
        assertEquals((int)ship.getCurrentReservePower(), 0);
        assertEquals((int)ship.getShieldHealth(), ship.getMaxShieldHealth() - 5);
    }

    @Test
    public void testLowEnergyButGreaterThanAbsorb () {
        ship.setCurrentReservePower(MAX_DAMAGE_ABSORB + 5);
        ship.takeDamage(MAX_DAMAGE_ABSORB + 5);
        assertEquals(ship.getHealth(), ship.getMaxHealth() - 5);
        assertEquals((int)ship.getCurrentReservePower(), 5);
        assertEquals((int)ship.getShieldHealth(), ship.getMaxShieldHealth() - MAX_DAMAGE_ABSORB);

        ship.takeDamage(10);
        assertEquals(ship.getHealth(), ship.getMaxHealth() - 10);
        assertEquals((int)ship.getCurrentReservePower(), 0);
        assertEquals((int)ship.getShieldHealth(), ship.getMaxShieldHealth() - MAX_DAMAGE_ABSORB - 5);
    }

}