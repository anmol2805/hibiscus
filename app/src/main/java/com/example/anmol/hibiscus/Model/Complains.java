package com.example.anmol.hibiscus.Model;

/**
 * Created by anmol on 9/14/2017.
 */

public class Complains {
    String date,title,status;

    public Complains() {
    }

    public Complains(String date, String title, String status) {
        this.date = date;
        this.title = title;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
