package com.anmol.hibiscus.Rasoi.Model;

/**
 * Created by anmol on 10/28/2017.
 */

public class MessStatus {
    String breakfast,lunch,dinner,coupondate,day;

    public MessStatus() {
    }

    public MessStatus(String breakfast, String lunch, String dinner, String coupondate, String day) {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.coupondate = coupondate;
        this.day = day;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public String getcoupondate() {
        return coupondate;
    }

    public void setcoupondate(String coupondate) {
        this.coupondate = coupondate;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
