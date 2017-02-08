package com.tiy.webapp.repos;

import com.tiy.webapp.starship.Starship;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Paul Dennis on 2/7/2017.
 */
public interface ShipRepo extends CrudRepository<Starship, Integer> {
}
