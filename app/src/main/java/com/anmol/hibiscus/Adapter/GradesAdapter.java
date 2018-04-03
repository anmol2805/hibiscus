package com.anmol.hibiscus.Adapter;

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
import com.anmol.hibiscus.Model.Mycourse;
import com.anmol.hibiscus.Model.Subjectgrd;
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
    public int getViewTypeCount() {
        return getCount();
    }

    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView!=null){
            return convertView;
        }
        else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(resource,null);
            final ProgressBar subjectpgr = (ProgressBar)v.findViewById(R.id.subjectpgr);
            final Button refreshgrd = (Button)v.findViewById(R.id.refreshgrd);
            subjectpgr.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.pgrcolor),android.graphics.PorterDuff.Mode.MULTIPLY);
            refreshgrd.setVisibility(View.VISIBLE);
            subjectpgr.setVisibility(View.GONE);
            TextView text = (TextView)v.findViewById(R.id.subject);
            text.setText(mycourses.get(position).getName());
            final TextView q1 = (TextView)v.findViewById(R.id.q1);
            final TextView q2 = (TextView)v.findViewById(R.id.q2);
            final TextView ms = (TextView)v.findViewById(R.id.ms);
            final TextView es = (TextView)v.findViewById(R.id.es);
            final TextView fa = (TextView)v.findViewById(R.id.fa);
            final TextView cgpa = (TextView)v.findViewById(R.id.gp);
            final TextView tot = (TextView)v.findViewById(R.id.tot);
            final LinearLayout l1 = (LinearLayout)v.findViewById(R.id.l1);
            final LinearLayout l2 = (LinearLayout)v.findViewById(R.id.l2);
            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.VISIBLE);
            final Button load = (Button)v.findViewById(R.id.pg);
            final ProgressBar lg = (ProgressBar)v.findViewById(R.id.lg);
            load.setVisibility(View.GONE);
            lg.setVisibility(View.VISIBLE);
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("subject_grades");
            db.child(mycourses.get(position).getId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists() || !dataSnapshot.hasChildren()){
                        load.setVisibility(View.VISIBLE);
                        l1.setVisibility(View.GONE);
                        l2.setVisibility(View.GONE);
                        lg.setVisibility(View.GONE);
                    }
                    else{
                        lg.setVisibility(View.GONE);
                        load.setVisibility(View.GONE);
                        l1.setVisibility(View.VISIBLE);
                        l2.setVisibility(View.VISIBLE);
                        String quiz1 = dataSnapshot.child("quiz1").getValue(String.class);
                        String quiz2 = dataSnapshot.child("quiz2").getValue(String.class);
                        String midsem = dataSnapshot.child("midsem").getValue(String.class);
                        String endsem = dataSnapshot.child("endsem").getValue(String.class);
                        String faculty = dataSnapshot.child("faculty_assessment").getValue(String.class);
                        String gpa = dataSnapshot.child("grade_point").getValue(String.class);
                        String total = dataSnapshot.child("subtotal").getValue(String.class);
                        q1.setText(quiz1);
                        q2.setText(quiz2);
                        ms.setText(midsem);
                        es.setText(endsem);
                        fa.setText(faculty);
                        cgpa.setText(gpa);
                        tot.setText(total);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            refreshgrd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refreshgrd.setVisibility(View.GONE);
                    subjectpgr.setVisibility(View.VISIBLE);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null && dataSnapshot.child("sid").getValue()!=null && dataSnapshot.child("pwd").getValue()!=null) {
                                uid = dataSnapshot.child("sid").getValue().toString();
                                pwd = dataSnapshot.child("pwd").getValue().toString();
                            }
                            try {
                                jsonObject.put("sub_code",mycourses.get(position).getId());
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
                                        float m1 = 0,m2 = 0,m3 = 0,m4 = 0,m5 = 0;
                                        JSONObject object = response.getJSONArray("Notices").getJSONObject(c);
                                        subjectpgr.setVisibility(View.GONE);
                                        refreshgrd.setVisibility(View.VISIBLE);
                                        String q1s = object.getString("quiz1");
                                        String q2s = object.getString("quiz2");
                                        String q3s = object.getString("midsem");
                                        String q4s = object.getString("endsem");
                                        String q5s = object.getString("faculty_assessment");
                                        String fq1s,fq2s,fq3s,fq4s,fq5s,fgpa,ftot;
                                        fgpa = object.getString("grade_point");

                                        if(!q1s.isEmpty() && !Character.isLetter(q1s.charAt(0))){
                                            m1 = Float.parseFloat(q1s);
                                            fq1s = q1s;

                                        }
                                        else{
                                            fq1s = "";
                                            m1 = 0;
                                        }
                                        if(!q2s.isEmpty() && !Character.isLetter(q2s.charAt(0))){
                                            m2 = Float.parseFloat(q2s);
                                            fq2s = q2s;

                                        }
                                        else{
                                            fq2s = "";
                                            m2 = 0;
                                        }
                                        if(!q3s.isEmpty()&& !Character.isLetter(q3s.charAt(0))){
                                            m3 = Float.parseFloat(q3s);
                                            fq3s = q3s;

                                        }
                                        else{
                                            fq3s = "";
                                            m3 = 0;
                                        }
                                        if(!q4s.isEmpty()&& !Character.isLetter(q4s.charAt(0))){
                                            m4 = Float.parseFloat(q4s);
                                            fq4s = q4s;

                                        }
                                        else{
                                            fq4s = "";
                                            m4 = 0;
                                        }
                                        if(!q5s.isEmpty()&& !Character.isLetter(q5s.charAt(0))){
                                            m5 = Float.parseFloat(q5s);
                                            fq5s = q5s;

                                        }
                                        else{
                                            fq5s = "";
                                            m5 = 0;
                                        }

                                        float total = m1+m2+m3+m4+m5;
                                        ftot = String.format("%.2f",total);
                                        Subjectgrd subjectgrd = new Subjectgrd(fq1s,fq2s,fq3s,fq4s,fq5s,fgpa,ftot);
                                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("subject_grades");
                                        db.child(mycourses.get(position).getId()).setValue(subjectgrd);



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    subjectpgr.setVisibility(View.GONE);
                                    refreshgrd.setVisibility(View.VISIBLE);
                                }
                            });
                            Mysingleton.getInstance(context).addToRequestqueue(jsonObjectRequestc);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
            load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                                jsonObject.put("sub_code",mycourses.get(position).getId());
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
                                        float m1 = 0,m2 = 0,m3 = 0,m4 = 0,m5 = 0;
                                        JSONObject object = response.getJSONArray("Notices").getJSONObject(c);
                                        lg.setVisibility(View.GONE);
                                        l1.setVisibility(View.VISIBLE);
                                        l2.setVisibility(View.VISIBLE);
                                        String q1s = object.getString("quiz1");
                                        String q2s = object.getString("quiz2");
                                        String q3s = object.getString("midsem");
                                        String q4s = object.getString("endsem");
                                        String q5s = object.getString("faculty_assessment");
                                        String fq1s,fq2s,fq3s,fq4s,fq5s,fgpa,ftot;
                                        fgpa = object.getString("grade_point");

                                        if(!q1s.isEmpty() && !Character.isLetter(q1s.charAt(0))){
                                            m1 = Float.parseFloat(q1s);
                                            fq1s = q1s;

                                        }
                                        else{
                                            fq1s = "";
                                            m1 = 0;
                                        }
                                        if(!q2s.isEmpty() && !Character.isLetter(q2s.charAt(0))){
                                            m2 = Float.parseFloat(q2s);
                                            fq2s = q2s;

                                        }
                                        else{
                                            fq2s = "";
                                            m2 = 0;
                                        }
                                        if(!q3s.isEmpty()&& !Character.isLetter(q3s.charAt(0))){
                                            m3 = Float.parseFloat(q3s);
                                            fq3s = q3s;

                                        }
                                        else{
                                            fq3s = "";
                                            m3 = 0;
                                        }
                                        if(!q4s.isEmpty()&& !Character.isLetter(q4s.charAt(0))){
                                            m4 = Float.parseFloat(q4s);
                                            fq4s = q4s;

                                        }
                                        else{
                                            fq4s = "";
                                            m4 = 0;
                                        }
                                        if(!q5s.isEmpty()&& !Character.isLetter(q5s.charAt(0))){
                                            m5 = Float.parseFloat(q5s);
                                            fq5s = q5s;

                                        }
                                        else{
                                            fq5s = "";
                                            m5 = 0;
                                        }

                                        float total = m1+m2+m3+m4+m5;
                                        ftot = String.format("%.2f",total);
                                        Subjectgrd subjectgrd = new Subjectgrd(fq1s,fq2s,fq3s,fq4s,fq5s,fgpa,ftot);
                                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("subject_grades");
                                        db.child(mycourses.get(position).getId()).setValue(subjectgrd);

                                        if(response.getJSONArray("Notices")==null){
                                            Toast.makeText(context,"null",Toast.LENGTH_SHORT).show();
                                            lg.setVisibility(View.GONE);
                                            load.setVisibility(View.VISIBLE);
                                            l1.setVisibility(View.GONE);
                                            l2.setVisibility(View.GONE);
                                        }

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

                }
            });



            return v;
        }

    }
}
