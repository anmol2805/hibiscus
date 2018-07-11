package com.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.anmol.hibiscus.Adapter.IcoAdapter;
import com.anmol.hibiscus.Adapter.NoticeAdapter;
import com.anmol.hibiscus.Helpers.Dbhelper;
import com.anmol.hibiscus.Interfaces.ItemClickListener;
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
import java.util.List;

/**
 * Created by anmol on 2017-08-18.
 */

public class main extends Fragment {
    String url = "http://139.59.23.157/api/hibi/notice";
    ProgressBar progressBar;
    String title,date,postedby,id,attention;
    List<Notice> notices;
    NoticeAdapter adapter;
    ListView lv;
    RecyclerView rv;
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
    ItemClickListener itemClickListener;
    IcoAdapter icoAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.main,container,false);
        getActivity().setTitle("Notice Board");
        Intent intent = new Intent(getActivity(), RequestService.class);
        getActivity().startService(intent);
        notices = new ArrayList<>();
        lv = (ListView)vi.findViewById(R.id.list);
        rv = (RecyclerView)vi.findViewById(R.id.rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setItemAnimator(new DefaultItemAnimator());
        final ImageButton refresh = (ImageButton)vi.findViewById(R.id.refresh);
        retry = (Button)vi.findViewById(R.id.retry);
        head = (TextView)vi.findViewById(R.id.head);
        body = (TextView)vi.findViewById(R.id.body);
        work = (Button)vi.findViewById(R.id.work);
        margin = (View)vi.findViewById(R.id.margin);
        rotate = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate);
        auth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Notice");
        progressBar = (ProgressBar)vi.findViewById(R.id.load);
        progressBar.setVisibility(View.VISIBLE);
        final DatabaseReference data = FirebaseDatabase.getInstance().getReference().getRoot().child("banner");
        noticerefresh = (SwipeRefreshLayout)vi.findViewById(R.id.noticerefresh);
//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(getActivity(), RequestService.class);
//                getActivity().startService(intent);
//                Toast.makeText(getActivity(),"Please Wait...",Toast.LENGTH_SHORT).show();
//                refresh.startAnimation(rotate);
//            }
//        });
        itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {

            }
        };
        notices.clear();
        Dbhelper dbhelper = new Dbhelper(getActivity());
        String query = "Select * from notice_table ORDER BY notice_id DESC";
        notices = dbhelper.readData(query);
        final ArrayList<String> noticeids = new ArrayList<>();
        for(int i = 0;i<notices.size();i++){
            noticeids.add(notices.get(i).getId());
        }
        if (!notices.isEmpty()){
            progressBar.setVisibility(View.GONE);
//            adapter = new NoticeAdapter(getActivity(),R.layout.notice,notices);
//            adapter.notifyDataSetChanged();
//            lv.setAdapter(adapter);
            icoAdapter = new IcoAdapter(getActivity(),notices,itemClickListener);
            icoAdapter.notifyDataSetChanged();
            rv.setAdapter(icoAdapter);

        }
        else {
            mdatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
//                        adapter = new NoticeAdapter(getActivity(),R.layout.notice,notices);
//                        adapter.notifyDataSetChanged();
//                        lv.setAdapter(adapter);
                        icoAdapter = new IcoAdapter(getActivity(),notices,itemClickListener);
                        icoAdapter.notifyDataSetChanged();
                        rv.setAdapter(icoAdapter);

                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


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
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                            System.out.println("noticeresponse null");
                            try{
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.notice_url), jsonObject, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        noticerefresh.setRefreshing(false);
                                        try {
                                            Dbhelper dbhelper = new Dbhelper(getActivity());
                                            int c = 0;
                                            while (c<5){
                                                System.out.println("noticeresponse" + response);
                                                JSONObject object = response.getJSONArray("Notices").getJSONObject(c);


                                                title = object.getString("title");
                                                date = object.getString("date");
                                                postedby = object.getString("posted_by");
                                                attention = object.getString("attention");
                                                id = object.getString("id");
                                                Notice notice = new Notice(title,date,postedby,attention,id);
                                                int k=0;
                                                for(int j = 0;j<noticeids.size();j++){
                                                    if(noticeids.get(j).equals(id)){
                                                        k=1;
                                                    }
                                                }
                                                if(k==0){
                                                    System.out.print("noticestatus:new entry");
                                                    dbhelper.insertData(notice);
                                                }
                                                else{
                                                    System.out.print("noticestatus:already present");
                                                }
                                                //dbhelper.insertData(notice);
                                                dbhelper.updatenotice(notice);
                                                //noticedatabase.child(String.valueOf(c)).setValue(notice);
                                                c++;
                                            }
                                            notices.clear();
                                            String query = "Select * from notice_table ORDER BY notice_id DESC";
                                            notices = dbhelper.readData(query);
                                            if (!notices.isEmpty()){
                                                progressBar.setVisibility(View.GONE);
//                                                adapter = new NoticeAdapter(getActivity(),R.layout.notice,notices);
//                                                adapter.notifyDataSetChanged();
//                                                lv.setAdapter(adapter);
                                                icoAdapter = new IcoAdapter(getActivity(),notices,itemClickListener);
                                                icoAdapter.notifyDataSetChanged();
                                                rv.setAdapter(icoAdapter);

                                            }
                                            else {
                                                mdatabase.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        notices.clear();
                                                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                                                            String title = data.child("title").getValue().toString();
                                                            String attention = data.child("attention").getValue().toString();
                                                            String posted_by = data.child("posted_by").getValue().toString();
                                                            String date = data.child("date").getValue().toString();
                                                            String id = data.child("id").getValue().toString();
                                                            Notice notice = new Notice(title, date, posted_by, attention, id);
                                                            notices.add(notice);


                                                        }
                                                        if (getActivity() != null) {
                                                            progressBar.setVisibility(View.GONE);
//                                                            adapter = new NoticeAdapter(getActivity(), R.layout.notice, notices);
//                                                            adapter.notifyDataSetChanged();
//                                                            lv.setAdapter(adapter);
                                                            icoAdapter = new IcoAdapter(getActivity(),notices,itemClickListener);
                                                            icoAdapter.notifyDataSetChanged();
                                                            rv.setAdapter(icoAdapter);

                                                        }


                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                            if(getActivity()!=null){
                                                Toast.makeText(getActivity(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        catch (NullPointerException e){
                                            e.printStackTrace();
                                        }
//                                        try {
//                                            JSONObject object0 = response.getJSONArray("Notices").getJSONObject(0);
//
//                                            title = object0.getString("title");
//                                            date = object0.getString("date");
//                                            postedby = object0.getString("posted_by");
//                                            attention = object0.getString("attention");
//                                            id = object0.getString("id");
//                                            Notice notice = new Notice(title,date,postedby,attention,id);
//                                            FirebaseDatabase.getInstance().getReference().child("Notices").setValue(notice);
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
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
                                if(getActivity()!=null){
                                    Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequest);
                                }

                            }
                            catch (IllegalStateException e){
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


//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                try{
//                    Intent i = new Intent(getActivity(),NoticeDataActivity.class);
//                    i.putExtra("id",notices.get(position).getId());
//                    i.putExtra("uid",uid);
//                    i.putExtra("pwd",pwd);
//
//                    i.putExtra("title",notices.get(position).getTitle());
//                    i.putExtra("date",notices.get(position).getDate());
//                    i.putExtra("att",notices.get(position).getAttention());
//                    i.putExtra("posted",notices.get(position).getPosted_by());
//                    startActivity(i);
//                    getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.still);
//
//                }
//                catch(IndexOutOfBoundsException e){
//                    e.printStackTrace();
//                }
//
//
//            }
//        });

        return vi;


    }



}
