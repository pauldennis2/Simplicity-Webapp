package com.tiy.webapp.wrappers;

import com.tiy.webapp.starship.ShipChassis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul Dennis on 2/16/2017.
 */
public class ShipyardInfoWrapper {

    private List<ShipChassis> possibleShips;
    private List<Integer> possibleShipCosts;

    private Integer productionAvailable;

    public ShipyardInfoWrapper () {

    }

    public ShipyardInfoWrapper (Integer productionAvailable) {
        possibleShips = new ArrayList<>();
        possibleShipCosts = new ArrayList<>();
        for (ShipChassis chassis : ShipChassis.values()) {
            possibleShips.add(chassis);
            possibleShipCosts.add(chassis.getBaseProductionCost());
        }
        this.productionAvailable = productionAvailable;
    }

    public List<ShipChassis> getPossibleShips() {
        return possibleShips;
    }

    public void setPossibleShips(List<ShipChassis> possibleShips) {
        this.possibleShips = possibleShips;
    }

    public List<Integer> getPossibleShipCosts() {
        return possibleShipCosts;
    }

    public void setPossibleShipCosts(List<Integer> possibleShipCosts) {
        this.possibleShipCosts = possibleShipCosts;
    }

    public Integer getProductionAvailable() {
        return productionAvailable;
    }

    public void setProductionAvailable(Integer productionAvailable) {
        this.productionAvailable = productionAvailable;
    }
}
