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

import com.anmol.hibiscus.Adapter.CourseAdapter;
import com.anmol.hibiscus.Courselistnotice;
import com.anmol.hibiscus.Model.Mycourse;
import com.anmol.hibiscus.R;
import com.anmol.hibiscus.services.RequestServiceAttendance;
import com.anmol.hibiscus.services.RequestServiceCourses;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anmol on 2017-07-11.
 */

public class courseware extends Fragment {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference coursedatabase;
    ListView courselist;
    List<Mycourse> mycourses;
    CourseAdapter courseAdapter;
    ProgressBar cl;
    Spinner spinner;
    ImageView fail;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    Button retry;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.courseware,container,false);
        getActivity().setTitle("My Courses");
        Intent intent = new Intent(getActivity(), RequestServiceCourses.class);
        getActivity().startService(intent);
        cl = (ProgressBar)vi.findViewById(R.id.cl);
        cl.setVisibility(View.VISIBLE);
        fail = (ImageView)vi.findViewById(R.id.fail);
        mycourses = new ArrayList<>();
        courselist = (ListView)vi.findViewById(R.id.listcourses);
        retry = (Button)vi.findViewById(R.id.retry);
        spinner = (Spinner)vi.findViewById(R.id.spinner);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("semester").getValue(String.class)!=null){
                    int semester = Integer.parseInt(dataSnapshot.child("semester").getValue(String.class));


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
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
                            courseAdapter = new CourseAdapter(getActivity(),R.layout.courses,mycourses);
                            courseAdapter.notifyDataSetChanged();
                            if(!courseAdapter.isEmpty()){
                                cl.setVisibility(View.GONE);
                                retry.setVisibility(View.GONE);
                                fail.setVisibility(View.GONE);
                                courselist.setAdapter(courseAdapter);

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
        courselist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = mycourses.get(i).getId();
                Intent intent1 = new Intent(getActivity(),Courselistnotice.class);
                intent1.putExtra("id",id);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.still);
            }
        });
        return vi;

    }
}
