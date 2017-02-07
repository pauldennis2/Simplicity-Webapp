package com.tiy.webapp.starship;
import java.util.ArrayList;
import java.util.List;

import com.tiy.webapp.starsys.Location;
import com.tiy.webapp.starsys.StarSystem;
import com.tiy.webapp.Player;

/**
 * Created by erronius on 12/20/2016.
 */
public class Shipyard extends Location {

    private StarSystem system;
    private List<Project> projects;
    private Project currentProject;
    private int surplusProduction;
    private Player owner;

    private String name;

    public Shipyard (StarSystem system, Player owner) {
        this.system = system;
        projects = new ArrayList<>();
        this.owner = owner;
        name = "Utopia Planitia";
    }

    public void addProject(Project project) {
        projects.add(project);
        if (currentProject == null) {
            currentProject = project;
        }
    }

    public void addProjectTopPriority (Project project) {
        projects.add(0, project);
        if (currentProject == null) {
            currentProject = project;
        }
    }

    public void addProductionToCurrentProject (int production) {
        if (currentProject == null) {
            System.out.println("Current project is null. Please give me something to do.");
            surplusProduction += production;
        } else {
            int surplusReturned = currentProject.addProduction(production);
            if (surplusReturned > 0) { //Project is coded to return at least 1 if production completes
                surplusProduction += surplusReturned;
                projects.remove(currentProject);
                if (projects.size() > 0) {
                    currentProject = projects.get(0); //get the next project, arbitrarily at the beginning
                } else {
                    currentProject = null;
                }
            }
        }
    }

    public int getSurplusProduction () {
        return surplusProduction;
    }

    public Project getCurrentProject () {
        return currentProject;
    }

    public Player getOwner () {
        return owner;
    }

    public StarSystem getSystem () {
        return system;
    }

    public List<Project> getProjects () {
        return projects;
    }

    public String getName () {
        return name;
    }

    public boolean removeProject (Project project) {
        if (projects.contains(project)) {
            projects.remove(project);
            return true;
        }
        return false;
    }

    @Override
    public String toString () {
        return name + " @ " + system.getName();
    }
}
