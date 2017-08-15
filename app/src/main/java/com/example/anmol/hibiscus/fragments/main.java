package com.example.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.anmol.hibiscus.Adapter.NoticeAdapter;
import com.example.anmol.hibiscus.Adapter.RecyclerAdapter;
import com.example.anmol.hibiscus.BackgroundTask;
import com.example.anmol.hibiscus.Httphandler;

import com.example.anmol.hibiscus.Model.Notice;
import com.example.anmol.hibiscus.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by anmol on 2017-07-11.
 */

public class main extends Fragment {
    private String TAG = main.class.getSimpleName();
    private ProgressDialog pdialog;
    private ListView lv;
    ArrayList<Notice> noticeArrayList;
    NoticeAdapter adapter;
    DatabaseReference databaseReference;
    private List<Notice> notices;
    RecyclerView noticerecycler;
    RecyclerView.Adapter radapter;
    RecyclerView.LayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.main,container,false);
        notices = new ArrayList<>();
        lv = (ListView) vi.findViewById(R.id.list);
        noticerecycler = (RecyclerView)vi.findViewById(R.id.noticerecycler);
        layoutManager = new LinearLayoutManager(getActivity());
        noticerecycler.setLayoutManager(layoutManager);
        noticerecycler.setHasFixedSize(true);
        BackgroundTask backgroundTask = new BackgroundTask(getActivity());
        noticeArrayList = backgroundTask.getList();
        radapter = new RecyclerAdapter(noticeArrayList);
        noticerecycler.setAdapter(radapter);

//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notices");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Iterator iterator = dataSnapshot.getChildren().iterator();
//                Set<Notice> set = new HashSet<Notice>();
//                while (iterator.hasNext()){
//                    set.add((Notice) ((DataSnapshot) iterator.next()).getValue(Notice.class));
//                }
//                notices.clear();
//                notices.addAll(set);
//                adapter = new NoticeAdapter(getActivity(),R.layout.notice,notices);
//                lv.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });



        return vi;
    }

}
