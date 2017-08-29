package com.example.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.anmol.hibiscus.Adapter.NoticeAdapter;
import com.example.anmol.hibiscus.Model.Notice;
import com.example.anmol.hibiscus.Mysingleton;
import com.example.anmol.hibiscus.NoticeDataActivity;
import com.example.anmol.hibiscus.R;
import com.example.anmol.hibiscus.services.RequestService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anmol on 2017-08-18.
 */

public class main extends Fragment {
    String url = "http://139.59.23.157/api/hibi/notice";
    ProgressBar progressBar;
    String title,date,postedby,id,attention;
    ArrayList<Notice>notices;
    NoticeAdapter adapter;
    ListView lv;
    int key;
    FirebaseAuth auth;
    String uid,pwd;
    DatabaseReference mdatabase;
    Animation rotate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.main,container,false);
        getActivity().setTitle("Notice Board");
        final ImageButton refresh = (ImageButton)vi.findViewById(R.id.refresh);
        rotate = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), RequestService.class);
                getActivity().startService(intent);
                Toast.makeText(getActivity(),"Please Wait...",Toast.LENGTH_SHORT).show();
                refresh.startAnimation(rotate);
            }
        });

        auth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Notice");
        progressBar = (ProgressBar)vi.findViewById(R.id.load);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        lv = (ListView)vi.findViewById(R.id.list);
        notices = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
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
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        progressBar.setVisibility(View.GONE);
//
//                        try {
//                            int c = 0;
//                            while (c<response.getJSONArray("Notices").length()){
//
//                                JSONObject object = response.getJSONArray("Notices").getJSONObject(c);
//
//                                key = c;
//                                title = object.getString("title");
//                                date = object.getString("date");
//                                postedby = object.getString("posted_by");
//                                attention = object.getString("attention");
//                                id = object.getString("id");
//                                Notice notice = new Notice(title,date,key,postedby,attention,id);
//                                //notices.add(notice);
//                                //mdatabase.child(String.valueOf(c)).setValue(notice);
//                                c++;
//                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            JSONObject object0 = response.getJSONArray("Notices").getJSONObject(0);
//                            key = 0;
//                            title = object0.getString("title");
//                            date = object0.getString("date");
//                            postedby = object0.getString("posted_by");
//                            attention = object0.getString("attention");
//                            id = object0.getString("id");
//                            Notice notice = new Notice(title,date,key,postedby,attention,id);
//                            FirebaseDatabase.getInstance().getReference().child("Notices").setValue(notice);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressBar.setVisibility(View.GONE);
//                       // Toast.makeText(getActivity(),"Error refreshing Notices",Toast.LENGTH_SHORT).show();
//                    }
//                });
//                Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequest);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i=0;i<50;i++){
                    if(dataSnapshot.hasChild(String.valueOf(i))){
                        String title = dataSnapshot.child(String.valueOf(i)).child("title").getValue().toString();
                        String attention = dataSnapshot.child(String.valueOf(i)).child("attention").getValue().toString();
                        String posted_by = dataSnapshot.child(String.valueOf(i)).child("posted_by").getValue().toString();
                        String date = dataSnapshot.child(String.valueOf(i)).child("date").getValue().toString();
                        String id = dataSnapshot.child(String.valueOf(i)).child("id").getValue().toString();
                        Notice notice = new Notice(title,date,i,posted_by,attention,id);
                        notices.add(notice);
                    }

                }
                if(getActivity()!=null){
                    adapter = new NoticeAdapter(getActivity(),R.layout.notice,notices);
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),NoticeDataActivity.class);
                i.putExtra("id",notices.get(position).getId());
                i.putExtra("uid",uid);
                i.putExtra("pwd",pwd);
                i.putExtra("title",notices.get(position).getTitle());
                i.putExtra("date",notices.get(position).getDate());
                i.putExtra("att",notices.get(position).getAttention());
                i.putExtra("posted",notices.get(position).getPosted_by());
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.still);
            }
        });

        return vi;

    }
}
