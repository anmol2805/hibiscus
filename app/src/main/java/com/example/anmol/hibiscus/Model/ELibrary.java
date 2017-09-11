package com.example.anmol.hibiscus.Model;

/**
 * Created by anmol on 9/11/2017.
 */

public class ELibrary {
    String id,title,author,publiser,year;

    public ELibrary() {
    }

    public ELibrary(String id, String title, String author, String publiser, String year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publiser = publiser;
        this.year = year;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubliser() {
        return publiser;
    }

    public void setPubliser(String publiser) {
        this.publiser = publiser;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


}
