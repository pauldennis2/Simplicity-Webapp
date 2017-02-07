package com.tiy.webapp.webapp.starsys;

import com.tiy.webapp.starsys.StarSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Paul Dennis on 1/13/2017.
 */
public class StarSystemTest {

    StarSystem sys1;
    StarSystem sys2;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCartesianDistanceCal () {
        sys1 = new StarSystem("Alpha", 3, 6);
        sys2 = new StarSystem("Beta", 5, 8);

        assertEquals(3, StarSystem.calculateCartesianDistance(sys1, sys2));

        sys1 = new StarSystem("Alpha", 1, 1);
        sys2 = new StarSystem("Beta", 4, 5);

        assertEquals(5, StarSystem.calculateCartesianDistance(sys1, sys2));

        sys1 = new StarSystem("Alpha", 3, 6);
        sys2 = new StarSystem("Beta", 3, 6);

        assertEquals(0, StarSystem.calculateCartesianDistance(sys1, sys2));

        sys1 = new StarSystem("Alpha", 6, 6);
        sys2 = new StarSystem("Beta", 9, 7);

        assertEquals(3, StarSystem.calculateCartesianDistance(sys1, sys2));
    }
}