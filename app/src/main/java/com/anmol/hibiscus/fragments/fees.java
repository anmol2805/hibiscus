package com.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

/**
 * Created by anmol on 9/11/2017.
 */

public class fees extends Fragment {
    WebView grd;
    ProgressBar progressBar;
    JSONObject jsonObject;
    FirebaseAuth auth;
    DatabaseReference hibdatabase;
    String uid,pwd,dep;
    Button retry;
    ImageView fail;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.fee,container,false);
        getActivity().setTitle("Fee-Ledger");
        grd = (WebView)vi.findViewById(R.id.grd);
        retry = (Button)vi.findViewById(R.id.retry);
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
        //grd.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //grd.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        grd.getSettings().setDomStorageEnabled(true);
        grd.getSettings().setDatabaseEnabled(true);
        //grd.getSettings().setAppCacheEnabled(true);
        grd.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        grd.getSettings().setUseWideViewPort(true);
        grd.getSettings().setTextZoom(175);
        grd.setInitialScale(1);
        jsonObject = new JSONObject();
        auth = FirebaseAuth.getInstance();
        hibdatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        hibdatabase.addValueEventListener(new ValueEventListener() {
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
                    try{
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.fees_url), jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    JSONObject object = response.getJSONArray("Notices").getJSONObject(0);

                                    if(!object.getString("html").isEmpty()){
                                        progressBar.setVisibility(View.GONE);
                                        grd.loadDataWithBaseURL(null,object.getString("html"), "text/html; charset=utf-8", "UTF-8",null);
                                        //grd.loadData(object.getString("html"), "text/html; charset=utf-8", "UTF-8");
                                    }
                                    else {
                                        if(getActivity()!=null && isAdded()){
                                            progressBar.setVisibility(View.GONE);
                                            retry.setVisibility(View.VISIBLE);
                                            fail.setVisibility(View.VISIBLE);
                                            Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    //Toast.makeText(getActivity(),object.getString("html"),Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(getActivity()!=null && isAdded()){
                                    progressBar.setVisibility(View.GONE);
                                    retry.setVisibility(View.VISIBLE);
                                    fail.setVisibility(View.VISIBLE);
                                    if(getActivity()!=null){
                                        Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }
                        });
                        if(getActivity()!=null && isAdded()){
                            Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequest);
                        }

                    }
                    catch (IllegalStateException e){
                        e.printStackTrace();
                    }

                    retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            retry.setVisibility(View.GONE);
                            fail.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.fees_url), jsonObject, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        JSONObject object = response.getJSONArray("Notices").getJSONObject(0);

                                        if(!object.getString("html").isEmpty()){
                                            progressBar.setVisibility(View.GONE);
                                            grd.loadDataWithBaseURL(null,object.getString("html"), "text/html; charset=utf-8", "UTF-8",null);
                                            //grd.loadData(object.getString("html"), "text/html; charset=utf-8", "UTF-8");
                                        }
                                        else {
                                            progressBar.setVisibility(View.GONE);
                                            retry.setVisibility(View.VISIBLE);
                                            fail.setVisibility(View.VISIBLE);
                                            Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
                                        }

                                        //Toast.makeText(getActivity(),object.getString("html"),Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if(getActivity()!=null && isAdded()){
                                        progressBar.setVisibility(View.GONE);
                                        retry.setVisibility(View.VISIBLE);
                                        fail.setVisibility(View.VISIBLE);
                                        Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            if(getActivity()!=null && isAdded()){
                                Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequest);
                            }                       
                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return vi;
    }
}
