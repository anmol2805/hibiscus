package com.anmol.hibiscus.Rasoi.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.anmol.hibiscus.Mysingleton;
import com.anmol.hibiscus.R;
import com.anmol.hibiscus.Rasoi.Adapter.Mess1Adapter;
import com.anmol.hibiscus.Rasoi.Model.mess1;
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
 * Created by anmol on 10/20/2017.
 */

public class ground extends Fragment {
    Button load;
    ListView list;
    Mess1Adapter mess1Adapter;
    List<mess1> mess1s = new ArrayList<>();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    TextView amt1,total;
    Button bookm1;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("students").child(auth.getCurrentUser().getUid());
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ground,container,false);
        load = (Button)v.findViewById(R.id.load);
        list = (ListView)v.findViewById(R.id.menu);
        amt1 = (TextView)v.findViewById(R.id.amt1);
        total = (TextView)v.findViewById(R.id.total);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        ViewGroup footer = (ViewGroup)layoutInflater.inflate(R.layout.footer,list,false);
        list.addFooterView(footer,null,false);
        bookm1 = (Button)footer.findViewById(R.id.bookm1);
        Intent intent = new Intent(getActivity(), MessStatusService.class);
        getActivity().startService(intent);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(getActivity(), MessStatusService2.class);
                getActivity().startService(intent1);
            }
        },1000);
        db.child("messStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("amount1").getValue(String.class)!=null){
                    amt1.setText(dataSnapshot.child("amount1").getValue(String.class));
                }
                if(dataSnapshot.child("total").getValue(String.class)!=null){
                    total.setText(dataSnapshot.child("total").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), MessStatusService.class);
                getActivity().startService(intent);
            }
        });
        db.child("messStatus").child("mess1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mess1s.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String day = data.child("date").getValue().toString();
                    String brkfast = data.child("brkfast").getValue().toString();
                    String lnch = data.child("lnch").getValue().toString();
                    String dinnr = data.child("dinnr").getValue().toString();
                    String bs = data.child("bs").getValue().toString();
                    String ls = data.child("ls").getValue().toString();
                    String ds = data.child("ds").getValue().toString();
                    mess1 mess1 = new mess1(day,brkfast,lnch,dinnr,bs,ls,ds);
                    mess1s.add(mess1);
                }
                if(getActivity()!=null){
                    mess1Adapter = new Mess1Adapter(getActivity(),R.layout.menu,mess1s);
                    mess1Adapter.notifyDataSetChanged();
                    if(!mess1Adapter.isEmpty()){
                        load.setVisibility(View.GONE);
                        list.setAdapter(mess1Adapter);
                        bookm1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final JSONObject jsonObject = mess1Adapter.getJsonObject();
                                System.out.println("jsonobj:" + jsonObject);
                                db.child("rosei").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot!=null && dataSnapshot.child("sid").getValue(String.class)!=null && dataSnapshot.child("pwd").getValue(String.class)!=null) {
                                            String sid = dataSnapshot.child("sid").getValue(String.class);
                                            String pwd = dataSnapshot.child("pwd").getValue(String.class);
                                            try {
                                                jsonObject.put("un",sid);
                                                jsonObject.put("pw",pwd);
                                                jsonObject.put("pass","encrypt");
                                                jsonObject.put("check",1);
                                                System.out.println("jsonobj:" + jsonObject);
                                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://14.139.198.171/api/rosei/booking", jsonObject, new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Intent intent = new Intent(getActivity(),UpcomingWeekService.class);
                                                        getActivity().startService(intent);
                                                        Intent intent1 = new Intent(getActivity(), MessStatusService2.class);
                                                        getActivity().startService(intent1);
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(getActivity(),"Error occured",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequest);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                    }
                    else {
                        load.setVisibility(View.VISIBLE);
                    }
                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }
}
