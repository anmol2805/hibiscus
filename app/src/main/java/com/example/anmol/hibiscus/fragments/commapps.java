package com.example.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.anmol.hibiscus.Adapter.Grades;
import com.example.anmol.hibiscus.HibiscusActivity;
import com.example.anmol.hibiscus.Mysingleton;
import com.example.anmol.hibiscus.R;
import com.example.anmol.hibiscus.SplashActivity;
import com.example.anmol.hibiscus.services.RequestServiceGrades;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.commapps,container,false);
        Intent intent = new Intent(getActivity(), RequestServiceGrades.class);
        getActivity().startService(intent);
        getActivity().setTitle("Grades");
        grd = (WebView)vi.findViewById(R.id.grd);

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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("grades");
        gradesdatabse = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");

        gradesdatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("grades")){
                    String html = dataSnapshot.child("grades").child("html").getValue().toString();
                    grd.loadData(html, "text/html; charset=utf-8", "UTF-8");
                }else{
                    Intent intent = new Intent(getActivity(), RequestServiceGrades.class);
                    getActivity().startService(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return vi;
    }
}
