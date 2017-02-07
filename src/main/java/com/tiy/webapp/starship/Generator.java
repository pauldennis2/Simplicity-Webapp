package com.tiy.webapp.starship;

/**
 * Created by erronius on 12/26/2016.
 */
public class Generator {

    private int currentReservePower;
    private int maxReservePower;
    private int powerPerTurn;

    public Generator (int maxReservePower, int powerPerTurn) {
        this.maxReservePower = maxReservePower;
        this.powerPerTurn = powerPerTurn;
        currentReservePower = maxReservePower;
    }

    public int getAvailablePower() {
        int availablePower = currentReservePower;
        currentReservePower = 0;
        return availablePower;
    }

    public int getPowerPerTurn () {
        return powerPerTurn;
    }

    public void generatePower () {
        currentReservePower += powerPerTurn;
    }

    public void returnUnusedPower (int unusedPower) {
        if (unusedPower < 0) {
            System.out.println("houston problem");
            throw new AssertionError("Can't return negative power");
        }
        if (currentReservePower + unusedPower <= maxReservePower) {
            currentReservePower += unusedPower;
        } else {
            currentReservePower = maxReservePower;
        }
    }

    public static Generator getTemplateGenerator (ShipChassis type) {
        if (type == ShipChassis.DESTROYER) {
            return new Generator(400, 75);
        }
        if (type == ShipChassis.FIGHTER) {
            return new Generator(100, 25);
        }
        return null;
    }

    public int getMaxReservePower () {
        return maxReservePower;
    }

    public int getCurrentReservePower () {
        return currentReservePower;
    }
}
