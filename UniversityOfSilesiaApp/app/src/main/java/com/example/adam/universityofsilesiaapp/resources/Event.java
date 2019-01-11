package com.example.adam.universityofsilesiaapp.resources;

import java.util.Date;

public class Event {

    private Integer id;
    private Date date;
    private String title, description;
    User user;
    UserGroups group;


    public Event() {
    }

    public Event(Integer id, Date date, String title, String description, User user, UserGroups group) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.description = description;
        this.user = user;
        this.group = group;
    }
    public Event(Date date, String title, String description) {
        this.date = date;
        this.title = title;
        this.description = description;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserGroups getGroup() {
        return group;
    }

    public void setGroup(UserGroups group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", date=" + date +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", user=" + user +
                ", group=" + group +
                '}';
    }
}
