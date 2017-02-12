package com.tiy.webapp;

import com.tiy.webapp.starship.Starship;

import java.util.List;

/**
 * Created by Paul Dennis on 2/12/2017.
 */
public class CombatInfoWrapper {
    List<Starship> friendShips;
    List<Starship> enemyShips;

    public CombatInfoWrapper () {

    }

    public CombatInfoWrapper(List<Starship> friendShips, List<Starship> enemyShips) {
        this.friendShips = friendShips;
        this.enemyShips = enemyShips;
    }

    public List<Starship> getFriendShips() {
        return friendShips;
    }

    public void setFriendShips(List<Starship> friendShips) {
        this.friendShips = friendShips;
    }

    public List<Starship> getEnemyShips() {
        return enemyShips;
    }

    public void setEnemyShips(List<Starship> enemyShips) {
        this.enemyShips = enemyShips;
    }
}
