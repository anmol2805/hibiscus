package com.example.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.anmol.hibiscus.services.RequestService;
import com.example.anmol.hibiscus.services.RequestServiceAttendance;
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
    Animation rotate;
    Button retry;
    String url = "http://139.59.23.157/api/hibi/attendence";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.myapps,container,false);
        getActivity().setTitle("Attendance");
        Intent intent = new Intent(getActivity(), RequestServiceAttendance.class);
        getActivity().startService(intent);
        progressBar = (ProgressBar)vi.findViewById(R.id.loadatt);
        progressBar.setVisibility(View.VISIBLE);
        listView = (ListView)vi.findViewById(R.id.listatt);
        retry = (Button)vi.findViewById(R.id.retry);
        attendances = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        final ImageButton refresh = (ImageButton)vi.findViewById(R.id.refresh);
        rotate = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), RequestServiceAttendance.class);
                getActivity().startService(intent);
                Toast.makeText(getActivity(),"Please Wait...",Toast.LENGTH_SHORT).show();
                refresh.startAnimation(rotate);
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RequestServiceAttendance.class);
                getActivity().startService(intent);
                Toast.makeText(getActivity(),"Please Wait...",Toast.LENGTH_SHORT).show();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("attendance");
        hibdatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                attendances.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){

                        String attend = data.child("attend").getValue().toString();
                        String subcode = data.child("subcode").getValue().toString();
                        String name = data.child("name").getValue().toString();
                        String sub = data.child("sub").getValue().toString();
                        Attendance attendance = new Attendance(subcode,sub,name,attend);
                        attendances.add(attendance);


                }
                if(getActivity()!=null){
                    attendanceAdapter = new AttendanceAdapter(getActivity(),R.layout.attendance,attendances);
                    attendanceAdapter.notifyDataSetChanged();
                    if(!attendanceAdapter.isEmpty()){
                        progressBar.setVisibility(View.GONE);
                        retry.setVisibility(View.GONE);
                        listView.setAdapter(attendanceAdapter);

                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                        retry.setVisibility(View.VISIBLE);

                    }

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return vi;
    }
}
