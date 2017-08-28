package com.example.anmol.hibiscus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.anmol.hibiscus.Model.Noticedata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NoticeDataActivity extends AppCompatActivity {
    String id,uid,pwd;
    JSONObject object;
    String url = "http://139.59.23.157/api/hibi/notice_data";
    TextView data;
    Context context;
    WebView nd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_data);
        //data = (TextView)findViewById(R.id.data);
        nd = (WebView)findViewById(R.id.nd);

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
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("NoticeData").child(id);

        object = new JSONObject();
        try {
            object.put("uid",uid);
            object.put("pwd",pwd);
            object.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String notice = response.getJSONArray("Notices").getJSONObject(0).getString("notice_data");
                    Noticedata noticedata = new Noticedata(notice);
                    databaseReference.setValue(noticedata);
                    //nd.loadData(response.getJSONArray("Notices").getJSONObject(0).getString("notice_data"), "text/html; charset=utf-8", "UTF-8");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Mysingleton.getInstance(context).addToRequestqueue(jsonObjectRequest);
        FirebaseDatabase.getInstance().getReference().child("NoticeData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id)){

                    Log.i("NoticeDataActivity",dataSnapshot.child(id).child("notice").getValue().toString());
                    nd.loadData(dataSnapshot.child(id).child("notice").getValue().toString(), "text/html; charset=utf-8", "UTF-8");
                }
                else{
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String notice = response.getJSONArray("Notices").getJSONObject(0).getString("notice_data");
                                Noticedata noticedata = new Noticedata(notice);
                                databaseReference.setValue(noticedata);
                                //nd.loadData(response.getJSONArray("Notices").getJSONObject(0).getString("notice_data"), "text/html; charset=utf-8", "UTF-8");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    Mysingleton.getInstance(context).addToRequestqueue(jsonObjectRequest);
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
