package com.tiy.webapp;

/**
 * Created by Paul Dennis on 2/6/2017.
 */
public class Ship {

    private String name;
    private int health;
    private int maxHealth;
    private String imageString;
    private String location;

    public Ship(String name, int health, int maxHealth, String imageString, String location) {
        this.name = name;
        this.health = health;
        this.maxHealth = maxHealth;
        this.imageString = imageString;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
