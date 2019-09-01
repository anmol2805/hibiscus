package com.anmol.hibiscus.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anmol.hibiscus.Adapter.ComplainAdapter;
import com.anmol.hibiscus.Model.Complains;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by anmol on 9/14/2017.
 */

public class complaints extends Fragment {
    List<Complains>complainses= new ArrayList<>();
    ComplainAdapter complainAdapter;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
    ProgressBar progressBar;
    ListView listView;
    String uid,pwd;
    JSONObject jsonObject = new JSONObject();
    String date,title,status;
    ImageButton post;
    Button retry;
    ImageView fail;
    String url = "https://hib.iiit-bh.ac.in/Hibiscus/complain/compProcess.php?cmd=NEW&trid=";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.complains,container,false);
        getActivity().setTitle("Complains");

        listView = (ListView)vi.findViewById(R.id.list);
        progressBar = (ProgressBar)vi.findViewById(R.id.load);
        post = (ImageButton)vi.findViewById(R.id.refresh);
        fail = (ImageView)vi.findViewById(R.id.fail);
        progressBar.setVisibility(View.VISIBLE);
        retry = (Button)vi.findViewById(R.id.retry);
        databaseReference.addValueEventListener(new ValueEventListener() {
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
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.complains_url), jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                int c = 0;
                                complainses.clear();
                                while (c<response.getJSONArray("Notices").length()){

                                    JSONObject object = response.getJSONArray("Notices").getJSONObject(c);
                                    status = object.getString("status");
                                    date = object.getString("date");
                                    title = object.getString("title");
                                    Complains complains = new Complains(date,title,status);
                                    complainses.add(complains);
                                    c++;
                                }
                                if(getActivity()!=null){
                                    complainAdapter = new ComplainAdapter(getActivity(),R.layout.comp,complainses);
                                    complainAdapter.notifyDataSetChanged();
                                    if(!complainAdapter.isEmpty()){
                                        listView.setAdapter(complainAdapter);
                                        progressBar.setVisibility(View.GONE);
                                        retry.setVisibility(View.GONE);
                                        fail.setVisibility(View.GONE);
                                    }
                                    else {
                                        progressBar.setVisibility(View.GONE);
                                        retry.setVisibility(View.VISIBLE);
                                        fail.setVisibility(View.VISIBLE);
                                        Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(getActivity()!=null && isAdded()){
                                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
                                retry.setVisibility(View.VISIBLE);
                                fail.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    });
                    Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequest);
                    retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            retry.setVisibility(View.GONE);
                            fail.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.complains_url), jsonObject, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        int c = 0;
                                        complainses.clear();
                                        while (c<response.getJSONArray("Notices").length()){

                                            JSONObject object = response.getJSONArray("Notices").getJSONObject(c);
                                            status = object.getString("status");
                                            date = object.getString("date");
                                            title = object.getString("title");
                                            Complains complains = new Complains(date,title,status);
                                            complainses.add(complains);
                                            c++;
                                        }
                                        if(getActivity()!=null){
                                            complainAdapter = new ComplainAdapter(getActivity(),R.layout.comp,complainses);
                                            complainAdapter.notifyDataSetChanged();
                                            if(!complainAdapter.isEmpty()){
                                                listView.setAdapter(complainAdapter);
                                                progressBar.setVisibility(View.GONE);
                                                retry.setVisibility(View.GONE);
                                                fail.setVisibility(View.GONE);
                                            }
                                            else {
                                                progressBar.setVisibility(View.GONE);
                                                retry.setVisibility(View.VISIBLE);
                                                fail.setVisibility(View.VISIBLE);
                                                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
                                            }

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if(getActivity()!=null && isAdded()){
                                        retry.setVisibility(View.VISIBLE);
                                        fail.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        if(getActivity()!=null){
                                            Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                }
                            });
                            Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequest);
                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("Post Complain");
                dialog.setContentView(R.layout.postcomp);
                dialog.setCancelable(false);
                final EditText prob = (EditText)dialog.findViewById(R.id.prob);
                final EditText rn = (EditText)dialog.findViewById(R.id.rn);
                final EditText dn = (EditText)dialog.findViewById(R.id.dn);
                final EditText lc = (EditText)dialog.findViewById(R.id.lc);
                final EditText at = (EditText)dialog.findViewById(R.id.at);
                final EditText ct = (EditText)dialog.findViewById(R.id.ct);
                Button postn = (Button)dialog.findViewById(R.id.postn);
                Button cancel = (Button)dialog.findViewById(R.id.canceled);
                postn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pr = prob.getText().toString();
                        String roll = rn.getText().toString();
                        String dept = dn.getText().toString();
                        String loc = lc.getText().toString();
                        String avt = at.getText().toString();
                        String con = ct.getText().toString();
                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => " + c.getTime());
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                        String formattedDate = df.format(c.getTime());
                        String formattime = tf.format(c.getTime());
                        final JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("time",formattime);
                            jsonObject.put("date",formattedDate);
                            jsonObject.put("des",pr);
                            jsonObject.put("room",roll);
                            jsonObject.put("depname",dept);
                            jsonObject.put("location",loc);
                            jsonObject.put("avail_time",avt);
                            jsonObject.put("contact",con);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null && dataSnapshot.child("sid").getValue()!=null && dataSnapshot.child("pwd").getValue()!=null){
                                    uid = dataSnapshot.child("sid").getValue().toString();
                                    pwd = dataSnapshot.child("pwd").getValue().toString();
                                    try {
                                        jsonObject.put("uid",uid);
                                        jsonObject.put("pwd",pwd);
                                        jsonObject.put("pass","encrypt");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.pc_url), jsonObject, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Toast.makeText(getActivity(),"Notice Posted Successfully",Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getActivity(),"Network Error!!!",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequest);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        return vi;
    }
}
