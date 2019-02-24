package com.example.adam.universityofsilesiaapp.resources;

public class Timetable {

    int id, group_id, subgroup;
    String url;

    public Timetable(int id, int group_id, String url, int subgroup) {
        this.id = id;
        this.group_id = group_id;
        this.url = url;
        this.subgroup = subgroup;
    }

    public Timetable() {
    }

    public int getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(int subgroup) {
        this.subgroup = subgroup;
    }

    @Override
    public String toString() {
        return Integer.toString(subgroup);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
