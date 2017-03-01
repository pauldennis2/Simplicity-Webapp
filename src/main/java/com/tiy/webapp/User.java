package com.tiy.webapp;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Created by Paul Dennis on 2/9/2017.
 */
@Entity
@Table(name = "users")
public class User {

    @GeneratedValue
    @Id
    Integer id;

    @Column (nullable = false, unique = true)
    private String email;

    @Column (nullable = false)
    private String password;

    @Column (nullable = false)
    private String handle;

    @Column (nullable = false)
    LobbyStatus lobbyStatus;

    @ElementCollection (fetch = FetchType.EAGER)
    Set<Integer> savedGameIds; //Type: LinkedHashSet to maintain order
    //Using a set as a hassle-free way to avoid duplicates

    public User () {

    }

    public User(String email, String password, String handle) {
        this.email = email;
        this.password = password;
        this.handle = handle;
        savedGameIds = new LinkedHashSet<>();
    }

    public void addSaveGameId (Integer id) {
        if (savedGameIds == null) {
            savedGameIds = new LinkedHashSet<>();
        }
        savedGameIds.add(id);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LobbyStatus getLobbyStatus() {
        return lobbyStatus;
    }

    public void setLobbyStatus(LobbyStatus lobbyStatus) {
        this.lobbyStatus = lobbyStatus;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Integer> getSavedGameIds() {
        return savedGameIds;
    }

    public void setSavedGameIds(Set<Integer> savedGameIds) {
        this.savedGameIds = savedGameIds;
    }
}
