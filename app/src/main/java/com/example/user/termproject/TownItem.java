package com.example.user.termproject;

/**
 * Created by user on 2017-11-20.
 */

public class TownItem {

    String name;
    String state;
    String city;
    String activity;

    public TownItem(String name, String state, String city, String activity) {
        this.name = name;
        this.state = state;
        this.city = city;
        this.activity = activity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    @Override
    public String toString() {
        return "TownItem{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", activity='" + activity + '\'' +
                '}';
    }
}
