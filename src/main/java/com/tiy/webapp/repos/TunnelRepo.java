package com.tiy.webapp.repos;

import com.tiy.webapp.starsys.SpaceTunnel;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Paul Dennis on 2/8/2017.
 */
public interface TunnelRepo extends CrudRepository<SpaceTunnel, Integer> {
}
