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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anmol.hibiscus.Adapter.CourseAdapter;
import com.anmol.hibiscus.Adapter.GradesAdapter;
import com.anmol.hibiscus.Model.Mycourse;
import com.anmol.hibiscus.Model.Mysubjectgrade;
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
    List<Mysubjectgrade> mysubjectgrades;
    GradesAdapter gradesAdapter;
    ProgressBar cl;
    String uid,pwd;
    JSONObject jsonObject;
    ArrayList<String> arrayList = new ArrayList<>();
    Button retry;
    ImageView fail;
    DatabaseReference coursedatabase;
    TextView q1,q2,ms,es,fa,tot;
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
        mysubjectgrades = new ArrayList<>();
        courselist = (ListView)vi.findViewById(R.id.list);
        
        

        q1 = (TextView)vi.findViewById(R.id.q1);
        q2 = (TextView)vi.findViewById(R.id.q2);
        ms = (TextView)vi.findViewById(R.id.ms);
        es = (TextView)vi.findViewById(R.id.es);
        fa = (TextView)vi.findViewById(R.id.fa);
        tot = (TextView)vi.findViewById(R.id.tot);
        jsonObject = new JSONObject();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        spinner = (Spinner)vi.findViewById(R.id.spinner);
        fail = (ImageView)vi.findViewById(R.id.fail);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("semester")!=null && dataSnapshot.child("semester").getValue(Integer.class)!=null){
                    int semester = dataSnapshot.child("semester").getValue(Integer.class);

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
                    mysubjectgrades.clear();
                    cl.setVisibility(View.VISIBLE);

                coursedatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("mycourses");
                coursedatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mycourses.clear();
                        mysubjectgrades.clear();
                        for(final DataSnapshot data:dataSnapshot.getChildren()){

                            final String id = data.child("id").getValue().toString();
                            char s = id.charAt(5);
                            if(String.valueOf(s).equals(a)){
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("subject_grades");
                                db.child(id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String subject = data.child("name").getValue().toString();
                                        String credits = data.child("credits").getValue().toString();
                                        String professor = data.child("professor").getValue().toString();
                                        if(!dataSnapshot.exists() || !dataSnapshot.hasChildren()){
                                            Mysubjectgrade mysubjectgrade = new Mysubjectgrade(subject,
                                                    professor,
                                                    credits,
                                                    id,
                                                    "",
                                                    "",
                                                    "",
                                                    "",
                                                    "",
                                                    "",
                                                    "",
                                                    false);
                                            for(int i = 0;i<mysubjectgrades.size();i++){
                                                if(mysubjectgrades.get(i).getId().equals(id)){
                                                    mysubjectgrades.remove(i);
                                                }
                                            }
                                            mysubjectgrades.add(mysubjectgrade);
//                                            Mycourse mycourse = new Mycourse(subject,professor,credits,id);
//                                            mycourses.add(mycourse);
                                        }
                                        else{
                                            String quiz1 = dataSnapshot.child("quiz1").getValue(String.class);
                                            String quiz2 = dataSnapshot.child("quiz2").getValue(String.class);
                                            String midsem = dataSnapshot.child("midsem").getValue(String.class);
                                            String endsem = dataSnapshot.child("endsem").getValue(String.class);
                                            String faculty = dataSnapshot.child("faculty_assessment").getValue(String.class);
                                            String gpa = dataSnapshot.child("grade_point").getValue(String.class);
                                            String total = dataSnapshot.child("subtotal").getValue(String.class);
                                            Mysubjectgrade mysubjectgrade = new Mysubjectgrade(subject,
                                                    professor,
                                                    credits,
                                                    id,
                                                    quiz1,
                                                    quiz2,
                                                    midsem,
                                                    endsem,
                                                    faculty,
                                                    gpa,
                                                    total,
                                                    true);
                                            for(int i = 0;i<mysubjectgrades.size();i++){
                                                if(mysubjectgrades.get(i).getId().equals(id)){
                                                    mysubjectgrades.remove(i);
                                                }
                                            }
                                            mysubjectgrades.add(mysubjectgrade);
//                                            Mycourse mycourse = new Mycourse(subject,professor,credits,id);
//                                            mycourses.add(mycourse);
                                        }
                                        if(getActivity()!=null){
                                            gradesAdapter = new GradesAdapter(getActivity(),R.layout.grades,mysubjectgrades);


                                            if(!gradesAdapter.isEmpty()){
                                                cl.setVisibility(View.GONE);
                                                retry.setVisibility(View.GONE);
                                                fail.setVisibility(View.GONE);
                                                gradesAdapter.notifyDataSetChanged();
                                                courselist.setAdapter(gradesAdapter);
                                                getTotal(mysubjectgrades);


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
        return vi;
    }

    private void getTotal(List<Mysubjectgrade> mysubjectgrades) {
        float quiz1total = 0;
        float quiz2total = 0;
        float midsemtotal = 0;
        float endsemtotal = 0;
        float facultytotal = 0;
        float subtotal = 0;
        for(int i = 0;i< mysubjectgrades.size();i++){
            String quiz1 = mysubjectgrades.get(i).getQuiz1();
            if(!quiz1.isEmpty() || !quiz1.equals("")){
                quiz1total = quiz1total + Float.parseFloat(quiz1);
            }
            String quiz2 = mysubjectgrades.get(i).getQuiz2();
            if(!quiz2.isEmpty() || !quiz2.equals("")){
                quiz2total = quiz2total + Float.parseFloat(quiz2);
            }
            String midsem = mysubjectgrades.get(i).getMidsem();
            if(!midsem.isEmpty() || !midsem.equals("")){
                midsemtotal = midsemtotal + Float.parseFloat(midsem);
            }
            String endsem = mysubjectgrades.get(i).getEndsem();
            if(!endsem.isEmpty() || !endsem.equals("")){
                endsemtotal = endsemtotal + Float.parseFloat(endsem);
            }
            String faculty = mysubjectgrades.get(i).getFaculty_assessment();
            if(!faculty.isEmpty() || !faculty.equals("")){
                facultytotal = facultytotal + Float.parseFloat(faculty);
            }
            String total = mysubjectgrades.get(i).getSubtotal();
            if(!total.isEmpty() || !total.equals("")){
                subtotal = subtotal + Float.parseFloat(total);
            }
        }
        q1.setText(String.format("%.2f",quiz1total));
        q2.setText(String.format("%.2f",quiz2total));
        ms.setText(String.format("%.2f",midsemtotal));
        es.setText(String.format("%.2f",endsemtotal));
        fa.setText(String.format("%.2f",facultytotal));
        tot.setText(String.format("%.2f",subtotal));
        System.out.println("quiz1tot" + quiz1total + "," + quiz2total + "," + midsemtotal + "," + endsemtotal + "," + facultytotal + "," + subtotal);
    }
}
