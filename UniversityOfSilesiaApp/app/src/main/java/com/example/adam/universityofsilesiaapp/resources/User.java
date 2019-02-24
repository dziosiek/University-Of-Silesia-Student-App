package com.example.adam.universityofsilesiaapp.resources;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    Integer id;
    String login, firstname, lastname;
    List<UserGroups> groups;

    public User() {
    }

    public User(Integer id, String login, String firstname, String lastname, List<UserGroups> groups) {
        this.id = id;
        this.login = login;
        this.firstname = firstname;
        this.lastname = lastname;
        this.groups = groups;
    }

    public List<UserGroups> getGroups() {
        return groups;
    }

    public void setGroups(List<UserGroups> groups) {
        this.groups = groups;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFullName() {
        return getFirstname() + " " + getLastname();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", groups=" + groups +
                '}';
    }
}
