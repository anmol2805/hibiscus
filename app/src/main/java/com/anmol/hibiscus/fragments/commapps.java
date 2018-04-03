package com.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anmol.hibiscus.Adapter.Grades;
import com.anmol.hibiscus.HibiscusActivity;
import com.anmol.hibiscus.Mysingleton;
import com.anmol.hibiscus.NoticeDataActivity;
import com.anmol.hibiscus.R;
import com.anmol.hibiscus.SplashActivity;
import com.anmol.hibiscus.services.RequestServiceGrades;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anmol on 2017-07-11.
 */

public class commapps extends Fragment {
    WebView grd;
    String uid,pwd;
    JSONObject object;
    DatabaseReference mdatabase,gradesdatabse;
    String url = "http://139.59.23.157/api/hibi/view_grades";
    ProgressBar progressBar;
    Button retry;
    ImageView fail;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.commapps,container,false);
        Intent intent = new Intent(getActivity(), RequestServiceGrades.class);
        getActivity().startService(intent);
        getActivity().setTitle("Grades");
        grd = (WebView)vi.findViewById(R.id.grd);
        progressBar = (ProgressBar)vi.findViewById(R.id.webl);
        progressBar.setVisibility(View.VISIBLE);
        fail = (ImageView)vi.findViewById(R.id.fail);
        grd.setFocusable(true);
        grd.setFocusableInTouchMode(true);
        grd.getSettings().setJavaScriptEnabled(true);
        grd.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        grd.getSettings().setLoadsImagesAutomatically(true);
        grd.getSettings().setSupportZoom(true);
        grd.getSettings().setBuiltInZoomControls(true);
        grd.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        grd.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        grd.getSettings().setDomStorageEnabled(true);
        grd.getSettings().setDatabaseEnabled(true);
        grd.getSettings().setAppCacheEnabled(true);
        grd.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        grd.getSettings().setUseWideViewPort(true);
        grd.getSettings().setTextZoom(175);
        grd.setInitialScale(1);
        retry = (Button)vi.findViewById(R.id.retry);
        retry.setVisibility(View.GONE);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("grades");
        gradesdatabse = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();
        databaseReference.child("Students").child(auth.getCurrentUser().getUid()).child("grades").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists() || !dataSnapshot.hasChildren()){
                    progressBar.setVisibility(View.GONE);
                    retry.setVisibility(View.VISIBLE);
                    fail.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
                }
                else{
                    String html = dataSnapshot.child("html").getValue(String.class);
                    progressBar.setVisibility(View.GONE);
                    grd.loadData(html, "text/html; charset=utf-8", "UTF-8");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retry.setVisibility(View.GONE);
                fail.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getActivity(), RequestServiceGrades.class);
                getActivity().startService(intent);
            }
        });
//        final JSONObject jsonObject = new JSONObject();
//        databaseReference.child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child("sid").getValue(String.class)!=null && dataSnapshot.child("pwd").getValue(String.class)!=null){
//                    uid = dataSnapshot.child("sid").getValue(String.class);
//                    pwd = dataSnapshot.child("pwd").getValue(String.class);
//                    try {
//                        jsonObject.put("uid",uid);
//                        jsonObject.put("pwd",pwd);
//                        jsonObject.put("pass","encrypt");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    JsonObjectRequest jsonObjectRequestg = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.view_grades_url), jsonObject, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                String html = response.getJSONArray("Notices").getJSONObject(0).getString("html");
//                                if(!html.isEmpty()){
//                                    progressBar.setVisibility(View.GONE);
//                                    grd.loadData(html, "text/html; charset=utf-8", "UTF-8");
//                                }
//                                else {
//                                    progressBar.setVisibility(View.GONE);
//                                    retry.setVisibility(View.VISIBLE);
//                                    fail.setVisibility(View.VISIBLE);
//                                    Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            if(getActivity()!=null && isAdded()){
//                                progressBar.setVisibility(View.GONE);
//                                retry.setVisibility(View.VISIBLE);
//                                fail.setVisibility(View.VISIBLE);
//                                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    });
//                    Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequestg);
//                    retry.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            retry.setVisibility(View.GONE);
//                            fail.setVisibility(View.GONE);
//                            progressBar.setVisibility(View.VISIBLE);
//                            JsonObjectRequest jsonObjectRequestg = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.view_grades_url), jsonObject, new Response.Listener<JSONObject>() {
//                                @Override
//                                public void onResponse(JSONObject response) {
//                                    try {
//                                        String html = response.getJSONArray("Notices").getJSONObject(0).getString("html");
//                                        if(!html.isEmpty()){
//                                            progressBar.setVisibility(View.GONE);
//                                            grd.loadData(html, "text/html; charset=utf-8", "UTF-8");
//                                        }
//                                        else {
//                                            progressBar.setVisibility(View.GONE);
//                                            retry.setVisibility(View.VISIBLE);
//                                            fail.setVisibility(View.VISIBLE);
//                                            Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
//                                        }
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }, new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    if(getActivity()!=null && isAdded()){
//                                        progressBar.setVisibility(View.GONE);
//                                        retry.setVisibility(View.VISIBLE);
//                                        fail.setVisibility(View.VISIBLE);
//                                        Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
//                                    }
//
//                                }
//                            });
//                            Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequestg);
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        return vi;
    }
}
