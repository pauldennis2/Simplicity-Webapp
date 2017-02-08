package com.tiy.webapp.starship;

/**
 * Created by erronius on 12/20/2016.
 */
public abstract class ShipComponent {

    private SlotType slotType;
    private int baseProductionCost;

    public ShipComponent () {

    }

    public ShipComponent (SlotType slotType, int baseProductionCost) {
        this.slotType = slotType;
        this.baseProductionCost = baseProductionCost;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public int getBaseProductionCost() {
        return baseProductionCost;
    }
}
