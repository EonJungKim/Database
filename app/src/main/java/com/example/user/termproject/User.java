package com.example.user.termproject;

/**
 * Created by user on 2017-10-04.
 */

public class User {

    String name;
    String ID;
    String password;
    String favoriteState;
    String eMail;
    String favoriteActivity;
    String state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFavoriteState() {
        return favoriteState;
    }

    public void setFavoriteState(String favoriteState) {
        this.favoriteState = favoriteState;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getFavoriteActivity() {
        return favoriteActivity;
    }

    public void setFavoriteActivity(String favoriteActivity) {
        this.favoriteActivity = favoriteActivity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}