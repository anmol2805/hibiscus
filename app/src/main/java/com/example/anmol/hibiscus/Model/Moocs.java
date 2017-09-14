package com.example.anmol.hibiscus.Model;

/**
 * Created by anmol on 9/14/2017.
 */

public class Moocs {
    String name,link;

    public Moocs() {
    }

    public Moocs(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
