package com.anmol.hibiscus.Model;

/**
 * Created by anmol on 2017-08-30.
 */

public class Mycourse {
    String name,professor,credits,id;

    public Mycourse() {
    }

    public Mycourse(String name, String professor, String credits,String id) {
        this.name = name;
        this.professor = professor;
        this.credits = credits;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
