package com.tiy.webapp.webapp.starsys;

import com.tiy.webapp.ImproperFunctionInputException;
import com.tiy.webapp.starsys.Planet;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * Created by erronius on 12/20/2016.
 */
public class PlanetTest {

    Planet captainPlanet;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        captainPlanet = new Planet("Captain Planet's Planet", 12, true, null);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testProduction () throws ImproperFunctionInputException {
        captainPlanet.setPopulation(10);
        captainPlanet.setResearchPct(0.5f);
        assertEquals (5, captainPlanet.getResearch());
        assertEquals (5, captainPlanet.getProduction());

        captainPlanet.setPopulation(7);
        captainPlanet.setResearchPct(0.3f);
        assertEquals(2, captainPlanet.getResearch());
        assertEquals(5, captainPlanet.getProduction());

        //This is undesirable! Will be fixed later
        //Todo fix this so people can't abuse the rounding this way
        captainPlanet.setResearchPct(0.5f);
        assertEquals(4, captainPlanet.getProduction());
        assertEquals(4, captainPlanet.getResearch());
    }

    @Test
    public void testSettingResearchProduction () throws ImproperFunctionInputException {
        captainPlanet.setResearchPct(0.2f);
        assertEquals(0.8f, captainPlanet.getProductionPct(), 0.0001f);
        expectedException.expect(ImproperFunctionInputException.class);
        captainPlanet.setResearchPct(-0.1f);
        captainPlanet.setResearchPct(1.2f);
    }

    @Test
    public void testPopulationGrowthCalc () {
        //Growth rate is 5% by default
        captainPlanet.setPopulation(0);
        assertEquals (-1, captainPlanet.calculateTurnsToGrowth());
        captainPlanet.setPopulation(1);
        assertEquals(15, captainPlanet.calculateTurnsToGrowth());
        captainPlanet.setPopulation(8);
        assertEquals(3, captainPlanet.calculateTurnsToGrowth());
    }

    @Test
    public void testActualPopulationGrowth () {
        captainPlanet.setPopulation(5);
        captainPlanet.growPopulation();
        captainPlanet.growPopulation();
        assertEquals(5, captainPlanet.getPopulation());
        captainPlanet.growPopulation();
        captainPlanet.growPopulation();
        assertEquals(6, captainPlanet.getPopulation());
        captainPlanet.growPopulation();
        captainPlanet.growPopulation();
        captainPlanet.growPopulation();
        captainPlanet.growPopulation();
        assertEquals(7, captainPlanet.getPopulation());
        captainPlanet.growPopulation();
        captainPlanet.growPopulation();
        captainPlanet.growPopulation();
        assertEquals(8, captainPlanet.getPopulation());
    }

    @Test
    public void testMaxPopulationGrowth () {
        captainPlanet.setPopulation(12);
        assertEquals(-1, captainPlanet.calculateTurnsToGrowth());
    }

}