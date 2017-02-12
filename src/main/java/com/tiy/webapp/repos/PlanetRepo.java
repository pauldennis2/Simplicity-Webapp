package com.tiy.webapp.repos;

import com.tiy.webapp.starsys.Planet;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Paul Dennis on 2/7/2017.
 */
public interface PlanetRepo extends CrudRepository<Planet, Integer> {
    Planet findFirstByName (String name);
}
