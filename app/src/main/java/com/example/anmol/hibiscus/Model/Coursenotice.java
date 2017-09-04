package com.example.anmol.hibiscus.Model;

/**
 * Created by anmol on 9/4/2017.
 */

public class Coursenotice {
    String date,title,link_id;

    public Coursenotice() {
    }

    public Coursenotice(String date, String title, String link_id) {
        this.date = date;
        this.title = title;
        this.link_id = link_id;
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

    public String getLink_id() {
        return link_id;
    }

    public void setLink_id(String link_id) {
        this.link_id = link_id;
    }
}
