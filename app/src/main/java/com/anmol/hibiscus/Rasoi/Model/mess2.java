package com.anmol.hibiscus.Rasoi.Model;

/**
 * Created by anmol on 10/18/2017.
 */

public class mess2 {

    String date,brkfast,lnch,dinnr,bs,ls,ds;

    public mess2() {
    }

    public mess2(String date, String brkfast, String lnch, String dinnr, String bs, String ls, String ds) {
        this.date = date;
        this.brkfast = brkfast;
        this.lnch = lnch;
        this.dinnr = dinnr;
        this.bs = bs;
        this.ls = ls;
        this.ds = ds;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    public String getLs() {
        return ls;
    }

    public void setLs(String ls) {
        this.ls = ls;
    }

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }

    public String getDay() {
        return date;
    }

    public void setDay(String date) {
        this.date = date;
    }

    public String getBrkfast() {
        return brkfast;
    }

    public void setBrkfast(String brkfast) {
        this.brkfast = brkfast;
    }

    public String getLnch() {
        return lnch;
    }

    public void setLnch(String lnch) {
        this.lnch = lnch;
    }

    public String getDinnr() {
        return dinnr;
    }

    public void setDinnr(String dinnr) {
        this.dinnr = dinnr;
    }
}
