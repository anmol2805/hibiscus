package com.example.anmol.hibiscus.Model;

/**
 * Created by anmol on 9/12/2017.
 */

public class Search {
    String id,name;

    public Search() {
    }

    public Search(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
