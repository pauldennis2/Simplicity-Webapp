package com.tiy.webapp.starsys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by erronius on 12/27/2016.
 */
@Entity
@Table(name = "locations")
public abstract class Location {

    @GeneratedValue
    @Id
    private Integer id;

    public abstract String getName();
}
