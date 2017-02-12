package com.tiy.webapp;

import javax.persistence.*;

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

    @Column
    private Boolean logged;

    public User () {

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
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

    public Boolean getLoggedIn() {
        return logged;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.logged = loggedIn;
    }
}
