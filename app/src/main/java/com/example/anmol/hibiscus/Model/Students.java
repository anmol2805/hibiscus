package com.example.anmol.hibiscus.Model;

import java.io.Serializable;

/**
 * Created by anmol on 2017-08-13.
 */

public class Students implements Serializable {
    String sid,email;

    public Students(String sid, String email) {
        this.sid = sid;
        this.email = email;

    }

    public Students() {
    }



    public String getsid() {
        return sid;
    }

    public void setsid(String sid) {
        this.sid = sid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
