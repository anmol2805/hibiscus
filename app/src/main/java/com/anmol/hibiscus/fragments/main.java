package com.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
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
    TextView head,body;
    Button work;
    ImageView back;
    View margin;
    SwipeRefreshLayout noticerefresh;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.main,container,false);
        getActivity().setTitle("Notice Board");
        Intent intent = new Intent(getActivity(), RequestService.class);
        getActivity().startService(intent);
        final ImageButton refresh = (ImageButton)vi.findViewById(R.id.refresh);
        retry = (Button)vi.findViewById(R.id.retry);
        head = (TextView)vi.findViewById(R.id.head);
        body = (TextView)vi.findViewById(R.id.body);
        work = (Button)vi.findViewById(R.id.work);
        margin = (View)vi.findViewById(R.id.margin);
        rotate = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate);
        noticerefresh = (SwipeRefreshLayout)vi.findViewById(R.id.noticerefresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), RequestService.class);
                getActivity().startService(intent);
                Toast.makeText(getActivity(),"Please Wait...",Toast.LENGTH_SHORT).show();
                refresh.startAnimation(rotate);
            }
        });
        noticerefresh.setColorSchemeColors(
                getActivity().getResources().getColor(R.color.colorAccent)
        );
        noticerefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notices = new ArrayList<>();
                auth = FirebaseAuth.getInstance();
                final DatabaseReference databaseReference,noticedatabase,attendancedatabase,gradesdatabase;
                databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();
                noticedatabase = FirebaseDatabase.getInstance().getReference().child("Notice");
                gradesdatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("grades");
                attendancedatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("attendance");
                final JSONObject jsonObject = new JSONObject();
                databaseReference.child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("sid").getValue(String.class)!=null && dataSnapshot.child("pwd").getValue(String.class)!=null){
                            uid = dataSnapshot.child("sid").getValue(String.class);
                            pwd = dataSnapshot.child("pwd").getValue(String.class);
                            try {
                                jsonObject.put("uid",uid);
                                jsonObject.put("pwd",pwd);
                                jsonObject.put("pass","encrypt");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.notice_url), jsonObject, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        int c = 0;
                                        while (c<response.getJSONArray("Notices").length()){

                                            JSONObject object = response.getJSONArray("Notices").getJSONObject(c);


                                            title = object.getString("title");
                                            date = object.getString("date");
                                            postedby = object.getString("posted_by");
                                            attention = object.getString("attention");
                                            id = object.getString("id");
                                            Notice notice = new Notice(title,date,postedby,attention,id);
                                            //notices.add(notice);
                                            noticedatabase.child(String.valueOf(c)).setValue(notice);
                                            c++;
                                        }
                                        noticerefresh.setRefreshing(false);
                                        Toast.makeText(getActivity(),"Updated Successfully",Toast.LENGTH_SHORT).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        JSONObject object0 = response.getJSONArray("Notices").getJSONObject(0);

                                        title = object0.getString("title");
                                        date = object0.getString("date");
                                        postedby = object0.getString("posted_by");
                                        attention = object0.getString("attention");
                                        id = object0.getString("id");
                                        Notice notice = new Notice(title,date,postedby,attention,id);
                                        FirebaseDatabase.getInstance().getReference().child("Notices").setValue(notice);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    noticerefresh.setRefreshing(false);
                                    if(getActivity()!=null){
                                        Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequest);


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        auth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Notice");
        progressBar = (ProgressBar)vi.findViewById(R.id.load);
        progressBar.setVisibility(View.VISIBLE);
        final DatabaseReference data = FirebaseDatabase.getInstance().getReference().getRoot().child("banner");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dhead = dataSnapshot.child("head").getValue(String.class);
                String dbody = dataSnapshot.child("body").getValue(String.class);
                String dname = dataSnapshot.child("button").child("name").getValue(String.class);
                final String dlink = dataSnapshot.child("button").child("link").getValue(String.class);

                if(dhead!=null && !dhead.isEmpty()){
                    head.setText(dhead);
                    head.setVisibility(View.VISIBLE);

                }
                else {
                    head.setVisibility(View.GONE);
                }
                if(dbody!=null && !dbody.isEmpty()){
                    body.setText(dbody);
                    body.setVisibility(View.VISIBLE);

                }
                else {
                    body.setVisibility(View.GONE);
                }
                if(dname!=null && !dname.isEmpty()){
                    work.setText(dname);
                    work.setVisibility(View.VISIBLE);

                }
                else {
                    work.setVisibility(View.GONE);
                }
                if((dhead==null || dhead.isEmpty())&&(dbody == null || dbody.isEmpty())&&(dname==null || dname.isEmpty())){
                    margin.setVisibility(View.GONE);
                }
                else {
                    margin.setVisibility(View.VISIBLE);
                }
                work.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dlink!=null){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dlink));
                            startActivity(browserIntent);
                        }

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("sid").getValue(String.class)!=null && dataSnapshot.child("pwd").getValue(String.class)!=null) {
                    uid = dataSnapshot.child("sid").getValue(String.class);
                    pwd = dataSnapshot.child("pwd").getValue(String.class);

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
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
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
