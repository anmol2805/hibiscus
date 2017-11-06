package com.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.anmol.hibiscus.Adapter.NoticeAdapter;
import com.anmol.hibiscus.Model.Notice;
import com.anmol.hibiscus.Mysingleton;
import com.anmol.hibiscus.NoticeDataActivity;
import com.anmol.hibiscus.R;
import com.anmol.hibiscus.services.RequestService;
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
    Button retry;
    String dep;
    String decrypt = "https://us-central1-iiitcloud-e9d6b.cloudfunctions.net/dcryptr?pass=";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.main,container,false);
        getActivity().setTitle("Notice Board");
        Intent intent = new Intent(getActivity(), RequestService.class);
        getActivity().startService(intent);
        final ImageButton refresh = (ImageButton)vi.findViewById(R.id.refresh);
        retry = (Button)vi.findViewById(R.id.retry);
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
        progressBar.setVisibility(View.VISIBLE);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.child("sid").getValue()!=null && dataSnapshot.child("pwd").getValue()!=null) {
                    uid = dataSnapshot.child("sid").getValue().toString();
                    pwd = dataSnapshot.child("pwd").getValue().toString();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lv = (ListView)vi.findViewById(R.id.list);
        notices = new ArrayList<>();

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notices.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){

                        String title = data.child("title").getValue().toString();
                        String attention = data.child("attention").getValue().toString();
                        String posted_by = data.child("posted_by").getValue().toString();
                        String date = data.child("date").getValue().toString();
                        String id = data.child("id").getValue().toString();
                        Notice notice = new Notice(title,date,posted_by,attention,id);
                        notices.add(notice);


                }
                if(getActivity()!=null){
                    progressBar.setVisibility(View.GONE);
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
