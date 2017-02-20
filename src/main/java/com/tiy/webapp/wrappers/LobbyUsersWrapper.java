package com.tiy.webapp.wrappers;

import com.tiy.webapp.User;

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

    Integer alphaGameId;
    Integer bakerGameId;
    Integer charlieGameId;
    Integer deltaGameId;

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

    public Integer getAlphaGameId() {
        return alphaGameId;
    }

    public void setAlphaGameId(Integer alphaGameId) {
        this.alphaGameId = alphaGameId;
    }

    public Integer getBakerGameId() {
        return bakerGameId;
    }

    public void setBakerGameId(Integer bakerGameId) {
        this.bakerGameId = bakerGameId;
    }

    public Integer getCharlieGameId() {
        return charlieGameId;
    }

    public void setCharlieGameId(Integer charlieGameId) {
        this.charlieGameId = charlieGameId;
    }

    public Integer getDeltaGameId() {
        return deltaGameId;
    }

    public void setDeltaGameId(Integer deltaGameId) {
        this.deltaGameId = deltaGameId;
    }
}
