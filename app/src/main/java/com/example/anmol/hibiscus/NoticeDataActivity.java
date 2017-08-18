package com.example.anmol.hibiscus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NoticeDataActivity extends AppCompatActivity {
    String id;
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
        nd.getSettings().setTextZoom(50);

        id = getIntent().getStringExtra("id");
        object = new JSONObject();
        try {
            object.put("uid","b516008");
            object.put("pwd","anmol@2805");
            object.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

//                    Document document = Jsoup.parse(response.getJSONArray("Notices").getJSONObject(0).getString("notice_data"));
//                    Elements elements = document.getAllElements();
                    nd.loadData(response.getJSONArray("Notices").getJSONObject(0).getString("notice_data"), "text/html; charset=utf-8", "UTF-8");
                    //data.setText(response.getJSONArray("Notices").getJSONObject(0).getString("notice_data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NoticeDataActivity.this,"error",Toast.LENGTH_SHORT).show();
            }
        });
        Mysingleton.getInstance(context).addToRequestqueue(jsonObjectRequest);

    }
}
