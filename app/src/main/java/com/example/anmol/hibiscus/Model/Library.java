package com.example.anmol.hibiscus.Model;

/**
 * Created by anmol on 9/11/2017.
 */

public class Library {
    String id,title,author,publiser,year,edition,status;

    public Library() {
    }

    public Library(String id, String title, String author, String publiser, String year, String edition, String status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publiser = publiser;
        this.year = year;
        this.edition = edition;
        this.status = status;
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

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
