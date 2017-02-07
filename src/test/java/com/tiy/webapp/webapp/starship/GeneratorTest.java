package com.tiy.webapp.webapp.starship;

import com.tiy.webapp.starship.Generator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by erronius on 12/26/2016.
 */
public class GeneratorTest {

    Generator generator;

    @Before
    public void setUp() throws Exception {
        generator = new Generator(100, 10);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testPowerGeneration () {
        generator.generatePower();
        int availablePower = generator.getAvailablePower();
        assertEquals(110, availablePower);
        availablePower -= 40; //Spend some power
        generator.returnUnusedPower(availablePower);
        generator.generatePower();
        availablePower = generator.getAvailablePower();
        assertEquals(80, availablePower);
        availablePower -= 80; //un-necessary line showing we spent the power
        generator.returnUnusedPower(availablePower);
        generator.generatePower();
        availablePower = generator.getAvailablePower();
        assertEquals(10, availablePower);
    }
}