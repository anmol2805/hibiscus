package com.anmol.hibiscus;

import androidx.core.widget.TextViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class CourseData extends AppCompatActivity {
    DatabaseReference cnd;
    FirebaseAuth auth;
    String uid,pwd,dep,link;
    String decrypt = "https://us-central1-iiitcloud-e9d6b.cloudfunctions.net/dcryptr?pass=";
    JSONObject jsonObject;
    String head,date,data;
    WebView cd;
    TextView heading;
    TextView ndata;
    String url1 = "http://139.59.23.157/api/hibi/course_notice_data";
    ProgressBar load;
    Button retry;
    ImageView fail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_data);

        setTitle("Course Notice");
        load = (ProgressBar)findViewById(R.id.dataload);
        load.setVisibility(View.VISIBLE);
        cd = (WebView)findViewById(R.id.cd);
        heading = (TextView)findViewById(R.id.head);
        ndata = (TextView)findViewById(R.id.data);
        jsonObject = new JSONObject();
        auth = FirebaseAuth.getInstance();
        link = getIntent().getStringExtra("link");
        retry = (Button)findViewById(R.id.retry);
        fail = (ImageView) findViewById(R.id.fail);
        cd.setFocusable(true);
        cd.setFocusableInTouchMode(true);
        cd.getSettings().setJavaScriptEnabled(true);
        cd.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        cd.getSettings().setLoadsImagesAutomatically(true);
        cd.getSettings().setSupportZoom(true);
        cd.getSettings().setBuiltInZoomControls(true);
        cd.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //cd.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        cd.getSettings().setDomStorageEnabled(true);
        cd.getSettings().setDatabaseEnabled(true);
        cd.getSettings().setBlockNetworkImage(true);
        //cd.getSettings().setAppCacheEnabled(true);
        cd.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        cd.getSettings().setUseWideViewPort(true);
        cd.getSettings().setTextZoom(175);
        cd.setInitialScale(1);
        cnd = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        cnd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.child("sid").getValue()!=null && dataSnapshot.child("pwd").getValue()!=null){
                    uid = dataSnapshot.child("sid").getValue().toString();
                    pwd = dataSnapshot.child("pwd").getValue().toString();
                    try {
                        jsonObject.put("link",link);
                        jsonObject.put("uid",uid);
                        jsonObject.put("pwd",pwd);
                        jsonObject.put("pass","encrypt");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.cnd_url), jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            load.setVisibility(View.GONE);
                            try {

                                JSONObject object = response.getJSONArray("Notices").getJSONObject(0);
                                head = object.getString("heading");
                                date = object.getString("date");
                                data = object.getString("notice_data");
                                heading.setText(head);
                                ndata.setText(data);
                                cd.loadData(date, "text/html; charset=utf-8", "UTF-8");




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            retry.setVisibility(View.VISIBLE);
                            fail.setVisibility(View.VISIBLE);
                            load.setVisibility(View.INVISIBLE);
                        }
                    });
                    Mysingleton.getInstance(getApplicationContext()).addToRequestqueue(jsonObjectRequest);
                    retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            retry.setVisibility(View.GONE);
                            fail.setVisibility(View.GONE);
                            load.setVisibility(View.VISIBLE);
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.cnd_url), jsonObject, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    load.setVisibility(View.GONE);
                                    try {

                                        JSONObject object = response.getJSONArray("Notices").getJSONObject(0);
                                        head = object.getString("heading");
                                        date = object.getString("date");
                                        data = object.getString("notice_data");
                                        heading.setText(head);
                                        ndata.setText(data);
                                        cd.loadData(date, "text/html; charset=utf-8", "UTF-8");




                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    retry.setVisibility(View.VISIBLE);
                                    fail.setVisibility(View.VISIBLE);
                                    load.setVisibility(View.INVISIBLE);
                                    Toast.makeText(CourseData.this,"Network Error",Toast.LENGTH_SHORT).show();
                                }
                            });
                            Mysingleton.getInstance(getApplicationContext()).addToRequestqueue(jsonObjectRequest);
                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.still,R.anim.slide_out_down);
    }
}
