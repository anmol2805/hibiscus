package com.anmol.hibiscus.Rasoi.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anmol on 10/18/2017.
 */

public class MessStatusService2 extends IntentService {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("students").child(auth.getCurrentUser().getUid()).child("rosei");
    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("students").child(auth.getCurrentUser().getUid()).child("messStatus");

    public MessStatusService2() {
        super("MessStatusService2");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final JSONObject jsonObject = new JSONObject();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.child("sid").getValue(String.class)!=null && dataSnapshot.child("pwd").getValue(String.class)!=null){
                    String sid = dataSnapshot.child("sid").getValue(String.class);
                    String pwd = dataSnapshot.child("pwd").getValue(String.class);

                    try {
                        jsonObject.put("un",sid);
                        jsonObject.put("pw",pwd);
                        jsonObject.put("pass","encrypt");
                        jsonObject.put("mess","m001");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.status), jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int c = 0;
                                while (c<response.getJSONArray("messStatus").length()){
                                    JSONObject object = response.getJSONArray("messStatus").getJSONObject(c);
                                    String brk = object.getString("breakfast");
                                    String lnch = object.getString("lunch");
                                    String dnnr = object.getString("dinner");
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("bs",brk);
                                    map.put("ls",lnch);
                                    map.put("ds",dnnr);
                                    db.child("mess1").child(String.valueOf(c)).updateChildren(map);
                                    c++;
                                }
                                int d = 0;
                                while (d<response.getJSONArray("messStatus").length()){
                                    JSONObject object = response.getJSONArray("messStatus").getJSONObject(d);
                                    String brkfast = object.getString("breakfast");
                                    String lnch = object.getString("lunch");
                                    String dinnr = object.getString("dinner");
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("bs",brkfast);
                                    map.put("ls",lnch);
                                    map.put("ds",dinnr);

                                    db.child("mess2").child(String.valueOf(d)).updateChildren(map);
                                    d++;
                                }
                                int m = 0;
                                while (m<response.getJSONArray("extraData").length()){
                                    JSONObject object = response.getJSONArray("extraData").getJSONObject(m);
                                    String date = object.getString("date");
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("date",date);
                                    db.child("mess1").child(String.valueOf(m)).updateChildren(map);
                                    m++;
                                }
                                int n = 0;
                                while (n<response.getJSONArray("extraData").length()){
                                    JSONObject object = response.getJSONArray("extraData").getJSONObject(n);
                                    String date = object.getString("date");
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("date",date);
                                    db.child("mess2").child(String.valueOf(n)).updateChildren(map);
                                    n++;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
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
}
