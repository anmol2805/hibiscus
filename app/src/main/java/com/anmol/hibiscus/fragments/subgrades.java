package com.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anmol.hibiscus.Adapter.CourseAdapter;
import com.anmol.hibiscus.Adapter.GradesAdapter;
import com.anmol.hibiscus.Model.Mycourse;
import com.anmol.hibiscus.Model.Search;
import com.anmol.hibiscus.Mysingleton;
import com.anmol.hibiscus.R;
import com.anmol.hibiscus.services.RequestServiceCourses;
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
    ArrayAdapter<String>arrayAdapter;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ListView courselist;
    List<Mycourse> mycourses;
    GradesAdapter gradesAdapter;
    ProgressBar cl;
    String uid,pwd;
    JSONObject jsonObject;
    ArrayList<String> arrayList = new ArrayList<>();
    Button retry;
    ImageView fail;
    DatabaseReference coursedatabase;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.subgrades,container,false);
        getActivity().setTitle("Subject Grades");
        Intent intent = new Intent(getActivity(), RequestServiceCourses.class);
        getActivity().startService(intent);
        cl = (ProgressBar)vi.findViewById(R.id.load);
        retry = (Button)vi.findViewById(R.id.retry);
        mycourses = new ArrayList<>();
        courselist = (ListView)vi.findViewById(R.id.list);
        jsonObject = new JSONObject();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        spinner = (Spinner)vi.findViewById(R.id.spinner);
        fail = (ImageView)vi.findViewById(R.id.fail);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("semester")!=null){
                    int semester = Integer.parseInt(dataSnapshot.child("semester").getValue().toString());

                    for(int i = semester;i>0;i--){
                        arrayList.add("Semester " + String.valueOf(i));
                    }
                    if(getActivity()!=null){
                        arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,arrayList);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(arrayAdapter);
                    }
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                String sem = adapterView.getItemAtPosition(i).toString();
                    final String a = String.valueOf(sem.charAt(9));
                    mycourses.clear();
                    cl.setVisibility(View.VISIBLE);
                coursedatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("mycourses");
                coursedatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mycourses.clear();
                        for(DataSnapshot data:dataSnapshot.getChildren()){

                            String id = data.child("id").getValue().toString();
                            char s = id.charAt(5);
                            if(String.valueOf(s).equals(a)){
                                String subject = data.child("name").getValue().toString();
                                String credits = data.child("credits").getValue().toString();
                                String professor = data.child("professor").getValue().toString();
                                Mycourse mycourse = new Mycourse(subject,professor,credits,id);
                                mycourses.add(mycourse);
                            }

                        }
                        if(getActivity()!=null){
                            gradesAdapter = new GradesAdapter(getActivity(),R.layout.grades,mycourses);
                            gradesAdapter.notifyDataSetChanged();
                            if(!gradesAdapter.isEmpty()){
                                cl.setVisibility(View.GONE);
                                retry.setVisibility(View.GONE);
                                fail.setVisibility(View.GONE);
                                courselist.setAdapter(gradesAdapter);

                            }
                            else {
                                retry.setVisibility(View.VISIBLE);
                                cl.setVisibility(View.GONE);
                                fail.setVisibility(View.VISIBLE);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                    databaseReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if(dataSnapshot.child("sid").getValue(String.class)!=null && dataSnapshot.child("pwd").getValue(String.class)!=null) {
//                                uid = dataSnapshot.child("sid").getValue(String.class);
//                                pwd = dataSnapshot.child("pwd").getValue(String.class);
//                            }
//                            try {
//                                jsonObject.put("pass","encrypt");
//                                jsonObject.put("uid",uid);
//                                jsonObject.put("pwd",pwd);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            JsonObjectRequest jsonObjectRequestc = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.course_url), jsonObject, new Response.Listener<JSONObject>() {
//                                @Override
//                                public void onResponse(JSONObject response) {
//                                    try {
//
//                                        int c = 0;
//                                        while (c<response.getJSONArray("Notices").length()){
//                                            JSONObject object = response.getJSONArray("Notices").getJSONObject(c);
//                                            String name,professor,credits;
//                                            String id = object.getString("id");
//                                            char s = id.charAt(5);
//                                            if(String.valueOf(s).equals(a)){
//                                                name = object.getString("name");
//                                                professor = object.getString("professor");
//                                                credits = object.getString("credits");
//                                                Mycourse mycourse = new Mycourse(name,professor,credits,id);
//                                                mycourses.add(mycourse);
//                                            }
//                                            c++;
//                                        }
//                                        if(getActivity()!=null){
//                                            cl.setVisibility(View.GONE);
//                                            gradesAdapter = new GradesAdapter(getActivity(),R.layout.grades,mycourses);
//                                            if(!gradesAdapter.isEmpty()){
//                                                courselist.setAdapter(gradesAdapter);
//                                                cl.setVisibility(View.GONE);
//                                                retry.setVisibility(View.GONE);
//                                                fail.setVisibility(View.GONE);
//                                            }
//                                            else {
//                                                retry.setVisibility(View.VISIBLE);
//                                                fail.setVisibility(View.VISIBLE);
//                                                cl.setVisibility(View.GONE);
//                                                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
//                                            }
//
//                                        }
//
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//                            }, new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    if(getActivity()!=null && isAdded()){
//                                        retry.setVisibility(View.VISIBLE);
//                                        fail.setVisibility(View.VISIBLE);
//                                        cl.setVisibility(View.GONE);
//                                        Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
//                                    }
//
//                                }
//                            });
//                            Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequestc);
//                            retry.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    retry.setVisibility(View.GONE);
//                                    fail.setVisibility(View.GONE);
//                                    cl.setVisibility(View.VISIBLE);
//                                    JsonObjectRequest jsonObjectRequestc = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.course_url), jsonObject, new Response.Listener<JSONObject>() {
//                                        @Override
//                                        public void onResponse(JSONObject response) {
//                                            try {
//
//                                                int c = 0;
//                                                while (c<response.getJSONArray("Notices").length()){
//                                                    JSONObject object = response.getJSONArray("Notices").getJSONObject(c);
//                                                    String name,professor,credits;
//                                                    String id = object.getString("id");
//                                                    char s = id.charAt(5);
//                                                    if(String.valueOf(s).equals(a)){
//                                                        name = object.getString("name");
//                                                        professor = object.getString("professor");
//                                                        credits = object.getString("credits");
//                                                        Mycourse mycourse = new Mycourse(name,professor,credits,id);
//                                                        mycourses.add(mycourse);
//                                                    }
//                                                    c++;
//                                                }
//                                                if(getActivity()!=null){
//
//                                                    gradesAdapter = new GradesAdapter(getActivity(),R.layout.grades,mycourses);
//                                                    gradesAdapter.notifyDataSetChanged();
//                                                    if(!gradesAdapter.isEmpty()){
//                                                        courselist.setAdapter(gradesAdapter);
//                                                        cl.setVisibility(View.GONE);
//                                                        retry.setVisibility(View.GONE);
//                                                        fail.setVisibility(View.GONE);
//                                                    }
//                                                    else {
//                                                        retry.setVisibility(View.VISIBLE);
//                                                        fail.setVisibility(View.VISIBLE);
//                                                        cl.setVisibility(View.GONE);
//                                                        Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
//                                                    }
//
//
//                                                }
//
//
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//
//                                        }
//                                    }, new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//                                            if(getActivity()!=null && isAdded()){
//                                                retry.setVisibility(View.VISIBLE);
//                                                fail.setVisibility(View.VISIBLE);
//                                                cl.setVisibility(View.GONE);
//                                                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
//                                            }
//
//                                        }
//                                    });
//                                    Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequestc);
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RequestServiceCourses.class);
                getActivity().startService(intent);
                retry.setVisibility(View.GONE);
                fail.setVisibility(View.GONE);
                cl.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(),"Please Wait...",Toast.LENGTH_SHORT).show();
            }
        });
        return vi;
    }
}
