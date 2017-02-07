package com.tiy.webapp.starsys;

/**
 * Created by erronius on 12/20/2016.
 */
public class SpaceTunnel extends Location {

    //TODO make some version of this class that works with any 2 Locations (superclass of StarSystem anyway)
    private int length;
    private StarSystem firstSystem;
    private StarSystem secondSystem;
    private TunnelType type;

    public SpaceTunnel (int length, StarSystem firstSystem, StarSystem secondSystem) {
        this.length = length;
        this.firstSystem = firstSystem;
        this.secondSystem = secondSystem;
        type = TunnelType.BLUE;
    }

    public int getLength () {
        return length;
    }

    /**
     *
     * @param notThisOne
     * @return the OTHER star system this tunnel connects to. NULL if it doesn't connect to the specified system
     */
    public StarSystem getOtherSystem (StarSystem notThisOne) {
        if (firstSystem.equals(notThisOne)) {
            return secondSystem;
        }
        if (secondSystem.equals(notThisOne)) {
            return firstSystem;
        }
        return null;
    }

    public String getName () {
        return "Tunnel between " + firstSystem.getName() + " and " + secondSystem.getName();
    }

    public StarSystem getFirstSystem() {
        return firstSystem;
    }

    public StarSystem getSecondSystem() {
        return secondSystem;
    }

    @Override
    public String toString () {
        return this.getName();
    }
}
