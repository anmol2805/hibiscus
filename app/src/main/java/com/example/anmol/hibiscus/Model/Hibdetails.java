package com.example.anmol.hibiscus.Model;

import java.io.Serializable;

/**
 * Created by anmol on 2017-08-14.
 */

public class Hibdetails implements Serializable {
    String sid,pwd;
    Boolean status;

    public Hibdetails(String sid, String pwd, Boolean status) {
        this.sid = sid;
        this.pwd = pwd;
        this.status = status;
    }

    public Hibdetails() {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
