package com.tiy.webapp.repos;

import com.tiy.webapp.Player;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Paul Dennis on 2/12/2017.
 */
public interface PlayerRepo extends CrudRepository<Player, Integer> {
    Player findFirstByName (String name);
}
