package com.anmol.hibiscus.Rasoi.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotifyService extends IntentService {

    public NotifyService() {
        super("NotifyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat adf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat ddf = new SimpleDateFormat("EEEE");
        String day = ddf.format(date);
        System.out.println("todayday:"+day);
        String todaysdate = sdf.format(date);
        String weddate = todaysdate + " 20:00:00";
        String thudate = todaysdate + " 23:00:00";
        try {
            Date wed = adf.parse(weddate);
            Date thu = adf.parse(thudate);
            if (day.contains("Wed") && date.after(wed)){
                db.child("wednesday").setValue(todaysdate);
            }
            else if(day.contains("Thu") && date.before(thu)){
                db.child("thursday").setValue(todaysdate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
