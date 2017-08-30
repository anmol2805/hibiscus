package com.example.anmol.hibiscus.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.anmol.hibiscus.Adapter.Grades;
import com.example.anmol.hibiscus.Model.Attendance;
import com.example.anmol.hibiscus.Model.Notice;
import com.example.anmol.hibiscus.Mysingleton;
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

public class RequestService extends IntentService {
    FirebaseAuth auth;
    DatabaseReference databaseReference,noticedatabase,attendancedatabase,gradesdatabase;
    String url1 = "http://139.59.23.157/api/hibi/notice";
    String url2 = "http://139.59.23.157/api/hibi/notice_data";
    String url3 = "http://139.59.23.157/api/hibi/view_grades";
    String url4 = "http://139.59.23.157/api/hibi/attendence";
    String uid,pwd;
    int key;

    String title,postedby,attention,date,id;
    public RequestService() {
        super("RequestService");
    }
    List<Notice> notices;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        notices = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();
        noticedatabase = FirebaseDatabase.getInstance().getReference().child("Notice");
        gradesdatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("grades");
        attendancedatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("attendance");
        databaseReference.child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uid = dataSnapshot.child("sid").getValue().toString();
                pwd = dataSnapshot.child("pwd").getValue().toString();
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("uid",uid);
                    jsonObject.put("pwd",pwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1, jsonObject, new Response.Listener<JSONObject>() {
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
                                Notice notice = new Notice(title,date,postedby,attention,id);
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
                            Notice notice = new Notice(title,date,postedby,attention,id);
                            FirebaseDatabase.getInstance().getReference().child("Notices").setValue(notice);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"No updates available",Toast.LENGTH_SHORT).show();
                    }
                });
                Mysingleton.getInstance(getApplicationContext()).addToRequestqueue(jsonObjectRequest);
//                JsonObjectRequest jsonObjectRequesta = new JsonObjectRequest(Request.Method.POST, url4, jsonObject, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            int c = 0;
//                            while (c<response.getJSONArray("Notices").length()){
//
//                                JSONObject object = response.getJSONArray("Notices").getJSONObject(c);
//                                String subcode = object.getString("subcode");
//                                String sub = object.getString("sub");
//                                String name = object.getString("name");
//                                String attend = object.getString("attendance");
//                                Attendance attendance = new Attendance(subcode,sub,name,attend);
//                                attendancedatabase.child(String.valueOf(c)).setValue(attendance);
//                                c++;
//                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),"No updates available",Toast.LENGTH_SHORT).show();
//                    }
//                });
//                Mysingleton.getInstance(getApplicationContext()).addToRequestqueue(jsonObjectRequesta);
//                JsonObjectRequest jsonObjectRequestg = new JsonObjectRequest(Request.Method.POST, url3, jsonObject, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            String html = response.getJSONArray("Notices").getJSONObject(0).getString("html");
//                            Grades grades = new Grades(html);
//                            gradesdatabase.setValue(grades);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
//                    }
//                });
//                Mysingleton.getInstance(getApplicationContext()).addToRequestqueue(jsonObjectRequestg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
