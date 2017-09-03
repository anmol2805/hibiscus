package com.example.anmol.hibiscus.Model;

import java.io.Serializable;

/**
 * Created by anmol on 2017-08-14.
 */

public class Noticel implements Serializable {
    String title,date,posted_by,attention,description;


    public Noticel(String title, String date, String posted_by, String attention,String description) {
        this.title = title;this.date = date;this.posted_by = posted_by;this.attention = attention;this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Noticel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
