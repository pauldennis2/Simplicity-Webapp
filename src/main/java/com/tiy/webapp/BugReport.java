package com.tiy.webapp;

import javax.persistence.*;

/**
 * Created by Paul Dennis on 2/11/2017.
 */
@Entity
@Table(name = "bug_reports")
public class BugReport {

    @GeneratedValue
    @Id
    Integer id;

    @Column(nullable = false)
    String text;

    @ManyToOne
    User user;

    @Column(nullable = false)
    String gameArea;

    public BugReport() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGameArea() {
        return gameArea;
    }

    public void setGameArea(String gameArea) {
        this.gameArea = gameArea;
    }
}
