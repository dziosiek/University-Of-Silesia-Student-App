package com.api.us.restapi.student_group;

import com.api.us.restapi.events.Event;
import com.api.us.restapi.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class StudentGroup {

    @Id
    @GeneratedValue
    Integer id;

    @NotNull
    String specialization;

    @NotNull
    int year;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "groupList")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler","groupList"})
    List<User> userList = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler","group"})
    List<Event> events = new ArrayList<>();

    public StudentGroup() {
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

}
