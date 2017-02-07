package com.tiy.webapp.webapp.starship;

import com.tiy.webapp.IllegalMoveException;
import com.tiy.webapp.Player;
import com.tiy.webapp.starship.Destroyer;
import com.tiy.webapp.starship.Fighter;
import com.tiy.webapp.starsys.SpaceTunnel;
import com.tiy.webapp.starsys.StarSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * Created by erronius on 12/20/2016.
 */
public class StarshipTunnelAndFighterAttachTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    StarSystem startSystem;
    StarSystem endSystem;
    SpaceTunnel tunnel;
    Destroyer destrier;
    Fighter charlie;

    Player owner;

    StarSystem uncon1;
    StarSystem uncon2;
    SpaceTunnel unconTunnel;

    @Before
    public void setUp() throws Exception {
        startSystem = new StarSystem("Valefor");
        owner = new Player(startSystem, "Danaerys");
        endSystem = new StarSystem("Ifrit");
        tunnel = new SpaceTunnel(3, startSystem, endSystem);
        destrier = new Destroyer(startSystem, owner);
        charlie = new Fighter(startSystem, owner);

        uncon1 = new StarSystem("Alpha");
        uncon2 = new StarSystem("Beta"); //This is probably unnecessary; we could use null/null instead
        unconTunnel = new SpaceTunnel(2, uncon1, uncon2);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFighterCannotEnter () throws IllegalMoveException {
        expectedException.expect(IllegalMoveException.class);
        charlie.enterTunnel(tunnel);
    }

    @Test
    public void testStarTunnelEnter () throws IllegalMoveException {
        destrier.enterTunnel(tunnel);
        assertEquals(tunnel, destrier.getLocation());
        assertEquals(tunnel.getLength(), destrier.getTurnsToDestination());

        expectedException.expect(IllegalMoveException.class);
        destrier.enterTunnel(tunnel);
    }


    @Test
    public void testStarTunnelMovement () throws IllegalMoveException {
        destrier.enterTunnel(tunnel);
        destrier.moveToDestination();
        destrier.moveToDestination();
        assertEquals(1, destrier.getTurnsToDestination());
        destrier.moveToDestination();
        assertEquals(endSystem, destrier.getLocation());
    }

    @Test
    public void testWrongFunctionFighterAttach () {
        //This function should not be called externally (internal call in fighter.attachTo(Starship)
        assertEquals(true, destrier.attach(charlie));
        assertEquals(true, destrier.attach(new Fighter(null, owner)));
        assertEquals(false, destrier.attach(new Fighter(null, owner)));
    }

    @Test
    public void testWrongSystemFighterAttach () throws IllegalMoveException {
        expectedException.expect(IllegalMoveException.class);
        new Fighter(uncon1, owner).attachTo(destrier);
    }

    @Test
    public void testTooManyFightersAttach () throws IllegalMoveException {
        new Fighter(startSystem, owner).attachTo(destrier);
        new Fighter(startSystem, owner).attachTo(destrier);
        expectedException.expect(IllegalMoveException.class);
        new Fighter(startSystem, owner).attachTo(destrier);
    }

    @Test
    public void testStarTunnelWithAttachedFighters () throws IllegalMoveException {
        charlie.attachTo(destrier);
        assertEquals(destrier, charlie.getLocation());
        destrier.enterTunnel(tunnel);
        assertEquals(destrier, charlie.getLocation());
        destrier.moveToDestination();
        destrier.moveToDestination();
        destrier.moveToDestination();
        assertEquals(endSystem, destrier.getLocation());
    }

}