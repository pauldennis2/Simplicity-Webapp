package com.tiy.webapp.starsys;

/**
 * Created by erronius on 12/25/2016.
 */
public class Governor {

    private String name;
    private int level;

    private Planet assignedPlanet;

    private String publicPriority; //change this to a ProductionType or something
    private String privatePriority;

    private String madSkillz;

    private int exp;

    public static final int[] EXP_NEEDED_FOR_LEVEL = {0, 100, 500, 1200, 2500, 5000};

    public Governor (String name, String publicPriority, String privatePriority) {
        this.name = name;
        this.publicPriority = publicPriority;
        this.privatePriority = privatePriority;
        exp = 0;
        level = 0;
    }

    public void addExp (int expToAdd) {
        exp += expToAdd;
        if (level <= EXP_NEEDED_FOR_LEVEL.length - 2) {
            if (exp >= EXP_NEEDED_FOR_LEVEL[level + 1]) {
                level++;
                doLevelUp();
            }
        }
    }

    public void doLevelUp () {
        madSkillz = "I've got level " + level + " mad skillz dog. Back off";
    }
}
