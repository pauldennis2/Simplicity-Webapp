package com.tiy.webapp.repos;

import com.tiy.webapp.starsys.SpaceTunnel;
import com.tiy.webapp.starsys.StarSystem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Paul Dennis on 2/8/2017.
 */
public interface TunnelRepo extends CrudRepository<SpaceTunnel, Integer> {
    SpaceTunnel findFirstByName (String name);
    //List<SpaceTunnel> findByFirstSystemOrSecondSystem (StarSystem system);
}
