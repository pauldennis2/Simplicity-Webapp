package com.tiy.webapp.starsys;

import com.tiy.webapp.repos.StarSystemGraphRepo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by Paul Dennis on 2/13/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StarSystemGraphDbTest {

    @Autowired
    StarSystemGraphRepo starSystemGraphRepo;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSSGDb () {
        StarSystemGraph test = new StarSystemGraph("4p_med_ring_map.txt");
        starSystemGraphRepo.save(test);
    }

}