package com.example.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.anmol.hibiscus.Adapter.CourseAdapter;
import com.example.anmol.hibiscus.Model.Mycourse;
import com.example.anmol.hibiscus.R;
import com.example.anmol.hibiscus.services.RequestService;
import com.example.anmol.hibiscus.services.RequestServiceCourses;
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
    FirebaseAuth auth;
    DatabaseReference coursedatabase;
    ListView courselist;
    List<Mycourse> mycourses;
    CourseAdapter courseAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.courseware,container,false);
        getActivity().setTitle("My Courses");
        Intent intent = new Intent(getActivity(), RequestServiceCourses.class);
        getActivity().startService(intent);
        mycourses = new ArrayList<>();
        courselist = (ListView)vi.findViewById(R.id.listcourses);
        auth = FirebaseAuth.getInstance();
        coursedatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("mycourses");
        coursedatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String subject = data.child("name").getValue().toString();
                    String credits = data.child("credits").getValue().toString();
                    String professor = data.child("professor").getValue().toString();
                    String id = data.child("id").getValue().toString();
                    Mycourse mycourse = new Mycourse(subject,professor,credits,id);
                    mycourses.add(mycourse);
                }
                if(getActivity()!=null){
                    courseAdapter = new CourseAdapter(getActivity(),R.layout.courses,mycourses);
                    courseAdapter.notifyDataSetChanged();
                    courselist.setAdapter(courseAdapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return vi;

    }
}
