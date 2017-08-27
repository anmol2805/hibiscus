package com.example.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.anmol.hibiscus.Adapter.AttendanceAdapter;
import com.example.anmol.hibiscus.Model.Attendance;
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
 * Created by anmol on 2017-07-11.
 */

public class myapps extends Fragment {
    ProgressBar progressBar;
    ListView listView;
    List<Attendance> attendances;
    AttendanceAdapter attendanceAdapter;
    DatabaseReference databaseReference,hibdatabase;
    FirebaseAuth auth;
    String uid,pwd;
    String url = "http://139.59.23.157/api/hibi/attendence";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.myapps,container,false);
        getActivity().setTitle("Attendance");
        progressBar = (ProgressBar)vi.findViewById(R.id.loadatt);
        listView = (ListView)vi.findViewById(R.id.listatt);
        attendances = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("attendance");
        hibdatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        progressBar.setVisibility(View.VISIBLE);
        hibdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uid = dataSnapshot.child("sid").getValue().toString();
                pwd = dataSnapshot.child("pwd").getValue().toString();
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("uid",uid);
                    jsonObject.put("pwd",pwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            int c = 0;
                            while (c<response.getJSONArray("Notices").length()){

                                JSONObject object = response.getJSONArray("Notices").getJSONObject(c);
                                String subcode = object.getString("subcode");
                                String sub = object.getString("sub");
                                String name = object.getString("name");
                                String attend = object.getString("attendance");
                                Attendance attendance = new Attendance(subcode,sub,name,attend);
                                databaseReference.child(String.valueOf(c)).setValue(attendance);
                                c++;
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),"No updates available",Toast.LENGTH_SHORT).show();
                    }
                });
                Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequest);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i=0;i<80;i++){
                    if(dataSnapshot.hasChild(String.valueOf(i))){
                        String attend = dataSnapshot.child(String.valueOf(i)).child("attend").getValue().toString();
                        String subcode = dataSnapshot.child(String.valueOf(i)).child("subcode").getValue().toString();
                        String name = dataSnapshot.child(String.valueOf(i)).child("name").getValue().toString();
                        String sub = dataSnapshot.child(String.valueOf(i)).child("sub").getValue().toString();
                        Attendance attendance = new Attendance(subcode,sub,name,attend);
                        attendances.add(attendance);
                    }

                }
                if(getActivity()!=null){
                    attendanceAdapter = new AttendanceAdapter(getActivity(),R.layout.attendance,attendances);
                    attendanceAdapter.notifyDataSetChanged();
                    listView.setAdapter(attendanceAdapter);

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return vi;
    }
}
