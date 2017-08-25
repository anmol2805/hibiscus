package com.example.anmol.hibiscus;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.anmol.hibiscus.Model.Notice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    Animation animFadein,zoomin;
    ImageView img;
    TextView text;
    String url = "http://139.59.23.157/api/hibi/notice";
    ProgressBar progressBar;
    String title,date,postedby,id,attention;
    ArrayList<Notice> notices;
    int key;
    FirebaseAuth auth;
    String uid,pwd;
    DatabaseReference mdatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        auth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Notice");
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        progressBar = (ProgressBar)findViewById(R.id.load);
        progressBar.setVisibility(View.VISIBLE);
        notices = new ArrayList<>();

        text = (TextView)findViewById(R.id.textView5);
        text.setVisibility(View.INVISIBLE);
        img = (ImageView)findViewById(R.id.imageView2);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        zoomin = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        img.startAnimation(zoomin);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text.startAnimation(animFadein);
            }
        },1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                databaseReference.addValueEventListener(new ValueEventListener() {
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
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    int c = 0;
                                    while (c<response.getJSONArray("Notices").length()){

                                        JSONObject object = response.getJSONArray("Notices").getJSONObject(c);

                                        key = c;
                                        title = object.getString("title");
                                        date = object.getString("date");
                                        postedby = object.getString("posted_by");
                                        attention = object.getString("attention");
                                        id = object.getString("id");
                                        Notice notice = new Notice(title,date,key,postedby,attention,id);
                                        //notices.add(notice);
                                        mdatabase.child(String.valueOf(c)).setValue(notice);
                                        c++;
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    JSONObject object0 = response.getJSONArray("Notices").getJSONObject(0);
                                    key = 0;
                                    title = object0.getString("title");
                                    date = object0.getString("date");
                                    postedby = object0.getString("posted_by");
                                    attention = object0.getString("attention");
                                    id = object0.getString("id");
                                    Notice notice = new Notice(title,date,key,postedby,attention,id);
                                    FirebaseDatabase.getInstance().getReference().child("Notices").setValue(notice);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(new Intent(SplashActivity.this,HibiscusActivity.class));
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(SplashActivity.this,HibiscusActivity.class));
                                Toast.makeText(SplashActivity.this,"Error refreshing Notices",Toast.LENGTH_SHORT).show();
                            }
                        });
                        Mysingleton.getInstance(SplashActivity.this).addToRequestqueue(jsonObjectRequest);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        },2000);


    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
