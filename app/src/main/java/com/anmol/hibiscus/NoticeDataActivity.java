package com.anmol.hibiscus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anmol.hibiscus.Model.Noticedata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import org.w3c.dom.Text;

public class NoticeDataActivity extends AppCompatActivity {
    String id,uid,pwd,title,date,att,post;
    JSONObject object;
    String url = "http://139.59.23.157/api/hibi/notice_data";
    TextView data;
    Context context;
    WebView nd;
    TextView d,a,p,t;
    ProgressBar pn;
    Button retry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_data);
        setTitle("Notices");
        retry = (Button)findViewById(R.id.retry);
        retry.setVisibility(View.GONE);
        //data = (TextView)findViewById(R.id.data);
        nd = (WebView)findViewById(R.id.nd);
        d = (TextView)findViewById(R.id.date);
        a = (TextView)findViewById(R.id.attention);
        p = (TextView)findViewById(R.id.posted);
        t = (TextView)findViewById(R.id.title);
        pn = (ProgressBar)findViewById(R.id.pn);
        pn.setVisibility(View.VISIBLE);
        nd.setFocusable(true);
        nd.setFocusableInTouchMode(true);
        nd.getSettings().setJavaScriptEnabled(true);
        nd.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        nd.getSettings().setLoadsImagesAutomatically(true);
        nd.getSettings().setSupportZoom(true);
        nd.getSettings().setBuiltInZoomControls(true);
        nd.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        nd.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        nd.getSettings().setDomStorageEnabled(true);
        nd.getSettings().setDatabaseEnabled(true);
        nd.getSettings().setAppCacheEnabled(true);
        nd.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        nd.getSettings().setUseWideViewPort(true);
        nd.getSettings().setTextZoom(175);
        nd.setInitialScale(1);
        id = getIntent().getStringExtra("id");
        uid = getIntent().getStringExtra("uid");
        pwd = getIntent().getStringExtra("pwd");
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        att = getIntent().getStringExtra("att");
        post = getIntent().getStringExtra("posted");
        d.setText(date);
        p.setText(post);
        a.setText(att);
        t.setText(title);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("NoticeData").child(id);

        object = new JSONObject();
        try {
            object.put("uid",uid);
            object.put("pwd",pwd);
            object.put("id",id);
            object.put("pass","encrypt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.noticedata_url), object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String notice = response.getJSONArray("Notices").getJSONObject(0).getString("notice_data");
                    if(!notice.contains("null") && !notice.isEmpty()){
                        nd.loadData(notice, "text/html; charset=utf-8", "UTF-8");
                        pn.setVisibility(View.GONE);
                    }
                    else{
                        pn.setVisibility(View.GONE);
                        retry.setVisibility(View.VISIBLE);
                        Toast.makeText(NoticeDataActivity.this,"Network Error",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pn.setVisibility(View.GONE);
                retry.setVisibility(View.VISIBLE);
                Toast.makeText(NoticeDataActivity.this,"Network Error!!!",Toast.LENGTH_SHORT).show();
            }
        });
        Mysingleton.getInstance(NoticeDataActivity.this).addToRequestqueue(jsonObjectRequest);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retry.setVisibility(View.GONE);
                pn.setVisibility(View.VISIBLE);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.noticedata_url), object, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String notice = response.getJSONArray("Notices").getJSONObject(0).getString("notice_data");
                            if(!notice.contains("null") && !notice.isEmpty()){
                                nd.loadData(notice, "text/html; charset=utf-8", "UTF-8");
                                pn.setVisibility(View.GONE);
                            }
                            else{
                                pn.setVisibility(View.GONE);
                                retry.setVisibility(View.VISIBLE);
                                Toast.makeText(NoticeDataActivity.this,"Network Error",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pn.setVisibility(View.GONE);
                        retry.setVisibility(View.VISIBLE);
                        Toast.makeText(NoticeDataActivity.this,"Network Error!!!",Toast.LENGTH_SHORT).show();
                    }
                });
                Mysingleton.getInstance(NoticeDataActivity.this).addToRequestqueue(jsonObjectRequest);
            }
        });
//        FirebaseDatabase.getInstance().getReference().child("NoticeData").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.hasChild(id)&&!dataSnapshot.child(id).getValue().toString().contains("null")){
//                    if(dataSnapshot.child(id).getValue()!=null){
//                        Log.i("NoticeDataActivity",dataSnapshot.child(id).child("notice").getValue().toString());
//                        nd.loadData(dataSnapshot.child(id).child("notice").getValue().toString(), "text/html; charset=utf-8", "UTF-8");
//                        pn.setVisibility(View.GONE);
//                    }
//
//
//                }
//                else{
//                    pn.setVisibility(View.GONE);
//
//                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.noticedata_url), object, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                String notice = response.getJSONArray("Notices").getJSONObject(0).getString("notice_data");
//                                Noticedata noticedata = new Noticedata(notice);
//                                databaseReference.setValue(noticedata);
//                                //nd.loadData(response.getJSONArray("Notices").getJSONObject(0).getString("notice_data"), "text/html; charset=utf-8", "UTF-8");
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            pn.setVisibility(View.GONE);
//
//                        }
//                    });
//                    Mysingleton.getInstance(NoticeDataActivity.this).addToRequestqueue(jsonObjectRequest);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.still,R.anim.slide_out_down);
    }
}