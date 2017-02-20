package com.tiy.webapp.repos;

import com.tiy.webapp.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Paul Dennis on 2/15/2017.
 */
public interface GameRepo extends CrudRepository<Game, Integer> {
    List<Game> findByJustStarted (Boolean justStarted);
}
