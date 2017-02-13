package com.tiy.webapp;

import java.util.List;

/**
 * Created by Paul Dennis on 2/12/2017.
 */
public class LobbyUsersWrapper {

    List<User> alphaUsers;
    List<User> bakerUsers;
    List<User> charlieUsers;
    List<User> deltaUsers;
    List<User> mainLobbyUsers;

    public LobbyUsersWrapper () {

    }

    public LobbyUsersWrapper(List<User> alphaUsers, List<User> bakerUsers,
                             List<User> charlieUsers, List<User> deltaUsers, List<User> mainLobbyUsers) {
        this.alphaUsers = alphaUsers;
        this.bakerUsers = bakerUsers;
        this.charlieUsers = charlieUsers;
        this.deltaUsers = deltaUsers;
        this.mainLobbyUsers = mainLobbyUsers;
    }

    public List<User> getAlphaUsers() {
        return alphaUsers;
    }

    public void setAlphaUsers(List<User> alphaUsers) {
        this.alphaUsers = alphaUsers;
    }

    public List<User> getBakerUsers() {
        return bakerUsers;
    }

    public void setBakerUsers(List<User> bakerUsers) {
        this.bakerUsers = bakerUsers;
    }

    public List<User> getCharlieUsers() {
        return charlieUsers;
    }

    public void setCharlieUsers(List<User> charlieUsers) {
        this.charlieUsers = charlieUsers;
    }

    public List<User> getDeltaUsers() {
        return deltaUsers;
    }

    public void setDeltaUsers(List<User> deltaUsers) {
        this.deltaUsers = deltaUsers;
    }

    public List<User> getMainLobbyUsers() {
        return mainLobbyUsers;
    }

    public void setMainLobbyUsers(List<User> mainLobbyUsers) {
        this.mainLobbyUsers = mainLobbyUsers;
    }
}
