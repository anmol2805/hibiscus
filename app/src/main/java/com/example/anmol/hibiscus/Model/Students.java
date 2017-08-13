package com.example.anmol.hibiscus.Model;

import java.io.Serializable;

/**
 * Created by anmol on 2017-08-13.
 */

public class Students implements Serializable {
    String sid,email,password;

    public Students(String sid, String email,String password) {
        this.sid = sid;
        this.email = email;
        this.password = password;
    }

    public Students() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
