package com.tiy.webapp.webapp.starsys;

import com.tiy.webapp.starsys.MapSize;
import com.tiy.webapp.starsys.StarSystem;
import com.tiy.webapp.starsys.StarSystemGraph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Paul Dennis on 1/13/2017.
 */
public class StarSystemGraphTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testIsConnected() throws Exception {
        StarSystemGraph smallRing = new StarSystemGraph(0, MapSize.SMALL);
        assertTrue(smallRing.isConnected());

        smallRing.addSystem(new StarSystem("Waga Waga"));
        assertFalse(smallRing.isConnected());

        //Todo add more robust tests
    }

    @Test
    public void testFileRead4PMap() {
        //StarSystemGraph medRing = new StarSystemGraph(new Point(0,0));
        //assertTrue(medRing.isConnected());
    }

}