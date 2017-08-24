package com.example.anmol.hibiscus.Model;

import java.io.Serializable;

/**
 * Created by anmol on 2017-08-14.
 */

public class Notice implements Serializable {
    String title,date,posted_by,attention,id;
    int key;

    public Notice(String title,String date,int key,String posted_by,String attention,String id) {
        this.title = title;this.date = date;this.key = key;this.posted_by = posted_by;this.attention = attention;
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

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
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
