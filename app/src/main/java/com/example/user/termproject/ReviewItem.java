package com.example.user.termproject;

/**
 * Created by beeup on 2017-11-19.
 */

public class ReviewItem {

    Float rating;
    String ID;
    String title;
    String date;
    String num;

    public ReviewItem(Float rating, String ID, String title, String date, String num) {
        this.rating = rating;
        this.ID = ID;
        this.title = title;
        this.date = date;
        this.num = num;
    }

    public Float getrating() {
        return rating;
    }

    public void setrating(Float rating) {
        this.rating = rating;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "ReviewItem{" +
                "rating=" + rating +
                ", ID='" + ID + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
