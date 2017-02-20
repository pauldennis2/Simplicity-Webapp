package com.tiy.webapp.wrappers;

import com.tiy.webapp.Player;
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

    public ShipyardInfoWrapper (Integer productionAvailable, Player player) {
        possibleShips = new ArrayList<>();
        possibleShipCosts = new ArrayList<>();
        /*for (ShipChassis chassis : ShipChassis.values()) {
            possibleShips.add(chassis);
            possibleShipCosts.add(chassis.getBaseProductionCost());
        }*/
        possibleShips.add(ShipChassis.FIGHTER);
        possibleShipCosts.add(ShipChassis.FIGHTER.getBaseProductionCost());
        possibleShips.add(ShipChassis.COLONIZER);
        possibleShipCosts.add(ShipChassis.COLONIZER.getBaseProductionCost());

        if (player.getSecondTechResearched()) { //Second Tech = destroyer
            possibleShips.add(ShipChassis.DESTROYER);
            possibleShipCosts.add(ShipChassis.DESTROYER.getBaseProductionCost());
        }
        if (player.getCruiserTechResearched()) {
            possibleShips.add(ShipChassis.CRUISER);
            possibleShipCosts.add(ShipChassis.CRUISER.getBaseProductionCost());
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
