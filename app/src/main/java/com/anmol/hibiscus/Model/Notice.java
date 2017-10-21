package com.anmol.hibiscus.Model;

import java.io.Serializable;

/**
 * Created by anmol on 2017-08-14.
 */

public class Notice implements Serializable {
    String title,date,posted_by,attention,id;


    public Notice(String title,String date,String posted_by,String attention,String id) {
        this.title = title;this.date = date;this.posted_by = posted_by;this.attention = attention;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Notice() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
