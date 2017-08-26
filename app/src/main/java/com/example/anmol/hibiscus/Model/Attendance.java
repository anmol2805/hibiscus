package com.example.anmol.hibiscus.Model;

import java.io.Serializable;

/**
 * Created by anmol on 2017-08-26.
 */

public class Attendance implements Serializable {
    String subcode,sub,name,attend;

    public Attendance() {
    }

    public Attendance(String subcode, String sub, String name, String attend) {
        this.subcode = subcode;
        this.sub = sub;
        this.name = name;
        this.attend = attend;
    }

    public String getSubcode() {
        return subcode;
    }

    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttend() {
        return attend;
    }

    public void setAttend(String attend) {
        this.attend = attend;
    }
}
