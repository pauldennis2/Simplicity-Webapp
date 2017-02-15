package com.tiy.webapp.repos;

import com.tiy.webapp.starship.Starship;
import com.tiy.webapp.starsys.StarSystem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Paul Dennis on 2/7/2017.
 */
public interface ShipRepo extends CrudRepository<Starship, Integer> {
    List<Starship> findByStarSystem (StarSystem starSystem);
    Starship findFirstByName (String name);
}
