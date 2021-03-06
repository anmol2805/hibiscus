package com.anmol.hibiscus.services;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anmol.hibiscus.LoginActivity;
import com.anmol.hibiscus.Model.Notice;
import com.anmol.hibiscus.Mysingleton;
import com.anmol.hibiscus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anmol on 2017-08-28.
 */

public class RequestService extends JobIntentService {
    FirebaseAuth auth;
    DatabaseReference databaseReference,noticedatabase,attendancedatabase,gradesdatabase;
    String url1 = "http://139.59.23.157/api/hibi/notice";
    String url2 = "http://139.59.23.157/api/hibi/notice_data";
    String url3 = "http://139.59.23.157/api/hibi/view_grades";
    String url4 = "http://139.59.23.157/api/hibi/attendence";
    String uid,pwd;

    int key;
    String dep;
    String decrypt = "https://us-central1-iiitcloud-e9d6b.cloudfunctions.net/dcryptr?pass=";
    String title,postedby,attention,date,id;
    List<Notice> notices;
    public static final int JOB_ID = 1;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, RequestService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        System.out.println("RequestService started");
        notices = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();
        noticedatabase = FirebaseDatabase.getInstance().getReference().child("Notice");
        if(auth.getCurrentUser()!=null){
            gradesdatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("grades");
            attendancedatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("attendance");
            final JSONObject jsonObject = new JSONObject();
            databaseReference.child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("sid").getValue(String.class)!=null && dataSnapshot.child("pwd").getValue(String.class)!=null){
                        uid = dataSnapshot.child("sid").getValue(String.class);
                        pwd = dataSnapshot.child("pwd").getValue(String.class);
                        try {
                            jsonObject.put("uid",uid);
                            jsonObject.put("pwd",pwd);
                            jsonObject.put("pass","encrypt");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.notice_url), jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    int c = 0;
                                    while (c<response.getJSONArray("Notices").length()){

                                        JSONObject object = response.getJSONArray("Notices").getJSONObject(c);


                                        title = object.getString("title");
                                        date = object.getString("date");
                                        postedby = object.getString("posted_by");
                                        attention = object.getString("attention");
                                        id = object.getString("id");
                                        Notice notice = new Notice(title,date,postedby,attention,id,false,false);
                                        //notices.add(notice);
                                        noticedatabase.child(String.valueOf(c)).setValue(notice);
                                        c++;
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    JSONObject object0 = response.getJSONArray("Notices").getJSONObject(0);

                                    title = object0.getString("title");
                                    date = object0.getString("date");
                                    postedby = object0.getString("posted_by");
                                    attention = object0.getString("attention");
                                    id = object0.getString("id");
                                    Notice notice = new Notice(title,date,postedby,attention,id,false,false);
                                    FirebaseDatabase.getInstance().getReference().child("Notices").setValue(notice);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        Mysingleton.getInstance(getApplicationContext()).addToRequestqueue(jsonObjectRequest);



                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{
            FirebaseAuth.getInstance().signOut();
            Intent intent2 = new Intent(getApplicationContext(),LoginActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent2);
        }
    }




}
