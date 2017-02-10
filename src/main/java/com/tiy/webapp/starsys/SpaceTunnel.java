package com.tiy.webapp.starsys;

import javax.persistence.*;

/**
 * Created by erronius on 12/20/2016.
 */

@Entity
@Table (name = "tunnels")
public class SpaceTunnel {

    @GeneratedValue
    @Id
    private Integer id;

    @Column(nullable = false)
    private Integer length;

    @ManyToOne //(cascade = CascadeType.ALL)
    StarSystem firstSystem;

    @ManyToOne //(cascade = CascadeType.ALL)
    StarSystem secondSystem;

    public SpaceTunnel () {

    }

    public SpaceTunnel(Integer length, StarSystem firstSystem, StarSystem secondSystem) {
        this.length = length;
        this.firstSystem = firstSystem;
        this.secondSystem = secondSystem;
    }

    public int getLength () {
        return length;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public StarSystem getFirstSystem() {
        return firstSystem;
    }

    public void setFirstSystem(StarSystem firstSystem) {
        this.firstSystem = firstSystem;
    }

    public StarSystem getSecondSystem() {
        return secondSystem;
    }

    public void setSecondSystem(StarSystem secondSystem) {
        this.secondSystem = secondSystem;
    }
}
