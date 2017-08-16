package com.example.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.anmol.hibiscus.Httphandler;

import com.example.anmol.hibiscus.Model.Notice;
import com.example.anmol.hibiscus.Mysingleton;
import com.example.anmol.hibiscus.NoticeDataActivity;
import com.example.anmol.hibiscus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    private List<Notice> notices;
    RecyclerView noticerecycler;
    RecyclerView.Adapter radapter;
    RecyclerView.LayoutManager layoutManager;
    Context context;
    ArrayList<Notice> arrayList = new ArrayList<>();
    String url = "http://139.59.23.157/api/hibi/notice";
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    JSONObject jsonObject;
    JSONArray jsonArray1;
    String title,date,postedby,attention,id;
    int key;
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
//        BackgroundTask backgroundTask = new BackgroundTask(getActivity());
//        noticeArrayList = backgroundTask.getList();
//        radapter = new RecyclerAdapter(noticeArrayList);
//        noticerecycler.setAdapter(radapter);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        jsonObject = new JSONObject();
        try {
            jsonObject.put("uid","b516008");
            jsonObject.put("pwd","anmol@2805");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    int c = 0;
                    while (c<response.getJSONArray("Notices").length()){
                        JSONObject object = response.getJSONArray("Notices").getJSONObject(c);
                        //Toast.makeText(context,object.getString("title"),Toast.LENGTH_SHORT).show();
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Notices").child(String.valueOf(c));
                        key = c;
                        title = object.getString("title");
                        date = object.getString("date");
                        postedby = object.getString("posted_by");
                        attention = object.getString("attention");
                        id = object.getString("id");
                        Notice notice = new Notice(title,date,key,postedby,attention,id);
                        db.setValue(notice);
                        notices.add(notice);
                        c++;

                    }
                    adapter = new NoticeAdapter(getActivity(),R.layout.notice,notices);
                    lv.setAdapter(adapter);
                    //Toast.makeText(context,"Please wait...",Toast.LENGTH_LONG).show();





                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
            }
        });
        Mysingleton.getInstance(context).addToRequestqueue(jsonObjectRequest);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),NoticeDataActivity.class);
                i.putExtra("id",notices.get(position).getId());
                startActivity(i);
            }
        });


        return vi;
    }

}
