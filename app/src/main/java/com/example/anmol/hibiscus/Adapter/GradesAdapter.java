package com.example.anmol.hibiscus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.anmol.hibiscus.Model.Mycourse;
import com.example.anmol.hibiscus.Mysingleton;
import com.example.anmol.hibiscus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by anmol on 2017-08-14.
 */

public class GradesAdapter extends ArrayAdapter<Mycourse> {
    private Activity context;
    private int resource;
    private List<Mycourse> mycourses;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
    JSONObject jsonObject = new JSONObject();
    String uid,pwd;
    public GradesAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Mycourse> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        mycourses = objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource,null);

        TextView text = (TextView)v.findViewById(R.id.subject);
        text.setText(mycourses.get(position).getName());

        final LinearLayout l1 = (LinearLayout)v.findViewById(R.id.l1);
        final LinearLayout l2 = (LinearLayout)v.findViewById(R.id.l2);
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);
        final Button load = (Button)v.findViewById(R.id.pg);
        load.setVisibility(View.VISIBLE);
        final ProgressBar lg = (ProgressBar)v.findViewById(R.id.lg);
        load.setVisibility(View.GONE);
        lg.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.child("sid").getValue()!=null && dataSnapshot.child("pwd").getValue()!=null) {
                    uid = dataSnapshot.child("sid").getValue().toString();
                    pwd = dataSnapshot.child("pwd").getValue().toString();
                }
                try {
                    jsonObject.put("pass","encrypt");
                    jsonObject.put("uid",uid);
                    jsonObject.put("pwd",pwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final JsonObjectRequest jsonObjectRequestc = new JsonObjectRequest(Request.Method.POST, context.getResources().getString(R.string.subgrades_url), jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int c = 0;
                            JSONObject object = response.getJSONArray("Notices").getJSONObject(c);
                            lg.setVisibility(View.GONE);
                            l1.setVisibility(View.VISIBLE);
                            l2.setVisibility(View.VISIBLE);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Network Error!!!",Toast.LENGTH_SHORT).show();
                        lg.setVisibility(View.GONE);
                        load.setVisibility(View.VISIBLE);
                        l1.setVisibility(View.GONE);
                        l2.setVisibility(View.GONE);
                    }
                });
                Mysingleton.getInstance(context).addToRequestqueue(jsonObjectRequestc);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                load.setVisibility(View.GONE);
//                lg.setVisibility(View.VISIBLE);
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if(dataSnapshot!=null && dataSnapshot.child("sid").getValue()!=null && dataSnapshot.child("pwd").getValue()!=null) {
//                            uid = dataSnapshot.child("sid").getValue().toString();
//                            pwd = dataSnapshot.child("pwd").getValue().toString();
//                        }
//                        try {
//                            jsonObject.put("pass","encrypt");
//                            jsonObject.put("uid",uid);
//                            jsonObject.put("pwd",pwd);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        final JsonObjectRequest jsonObjectRequestc = new JsonObjectRequest(Request.Method.POST, context.getResources().getString(R.string.subgrades_url), jsonObject, new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                try {
//
//                                    int c = 0;
//                                    JSONObject object = response.getJSONArray("Notices").getJSONObject(c);
//                                    lg.setVisibility(View.GONE);
//                                    l1.setVisibility(View.VISIBLE);
//                                    l2.setVisibility(View.VISIBLE);
//
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(context,"Network Error!!!",Toast.LENGTH_SHORT).show();
//                                lg.setVisibility(View.GONE);
//                                load.setVisibility(View.VISIBLE);
//                                l1.setVisibility(View.GONE);
//                                l2.setVisibility(View.GONE);
//                            }
//                        });
//                        Mysingleton.getInstance(context).addToRequestqueue(jsonObjectRequestc);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
            }
        });
        


        return v;
    }
}
