package com.example.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.anmol.hibiscus.Adapter.CourseAdapter;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anmol on 9/13/2017.
 */

public class subgrades extends Fragment{
    Spinner spinner;
    ArrayAdapter<CharSequence>arrayAdapter;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ListView courselist;
    List<Mycourse> mycourses;
    CourseAdapter courseAdapter;
    ProgressBar cl;
    String uid,pwd;
    JSONObject jsonObject;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.subgrades,container,false);
        cl = (ProgressBar)vi.findViewById(R.id.load);

        mycourses = new ArrayList<>();
        courselist = (ListView)vi.findViewById(R.id.list);
        jsonObject = new JSONObject();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        spinner = (Spinner)vi.findViewById(R.id.spinner);
        arrayAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.semester,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String sem = adapterView.getItemAtPosition(i).toString();
                if(sem.contains("Select semester")){
                    cl.setVisibility(View.VISIBLE);
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
                            JsonObjectRequest jsonObjectRequestc = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.course_url), jsonObject, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        mycourses.clear();
                                        int c = 0;
                                        while (c<response.getJSONArray("Notices").length()){
                                            JSONObject object = response.getJSONArray("Notices").getJSONObject(c);
                                            String name = object.getString("name");
                                            String professor = object.getString("professor");
                                            String credits = object.getString("credits");
                                            String id = object.getString("id");
                                            Mycourse mycourse = new Mycourse(name,professor,credits,id);
                                            mycourses.add(mycourse);
                                            c++;
                                        }
                                        if(getActivity()!=null){
                                            cl.setVisibility(View.GONE);
                                            courseAdapter = new CourseAdapter(getActivity(),R.layout.courses,mycourses);
                                            courseAdapter.notifyDataSetChanged();
                                            courselist.setAdapter(courseAdapter);

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(),"Network Error!!!",Toast.LENGTH_SHORT).show();
                                    cl.setVisibility(View.GONE);
                                }
                            });
                            Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequestc);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else if(sem.contains("1st semester")){

                }
                else if(sem.contains("2nd semester")){

                }
                else if(sem.contains("3rd semester")){

                }
                else if(sem.contains("4th semester")){

                }
                else if(sem.contains("5th semester")){

                }
                else if(sem.contains("6th semester")){

                }
                else if(sem.contains("7th semester")){

                }
                else if(sem.contains("8th semester")){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return vi;
    }
}
