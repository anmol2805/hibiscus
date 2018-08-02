package com.anmol.hibiscus.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anmol.hibiscus.Adapter.IcoAdapter1;
import com.anmol.hibiscus.Adapter.NoticeAdapterl;
import com.anmol.hibiscus.Interfaces.ItemClickListener;
import com.anmol.hibiscus.Model.Notice;
import com.anmol.hibiscus.Model.Noticel;
import com.anmol.hibiscus.PostingActivity;
import com.anmol.hibiscus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anmol on 2017-08-30.
 */

public class local extends Fragment {
    FirebaseAuth auth;
    DatabaseReference databaseReference,studentdatabase;
    FloatingActionButton post;
    List<Notice> noticels;
    RecyclerView listView;
    IcoAdapter1 noticeAdapterl;
    long size = 0;
    ProgressBar pg;
    ItemClickListener itemClickListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.local,container,false);
        getActivity().setTitle("Notice Board");
        post = (FloatingActionButton)vi.findViewById(R.id.postnotice);
        listView = (RecyclerView) vi.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(layoutManager);
        listView.setHasFixedSize(true);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        pg = (ProgressBar)vi.findViewById(R.id.load);
        pg.setVisibility(View.VISIBLE);
        noticels = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        studentdatabase = FirebaseDatabase.getInstance().getReference().child("Studentnoticeboard");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Auth");
        itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {

            }
        };
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.hasChild(auth.getCurrentUser().getUid())){
//                    Boolean status = (Boolean) dataSnapshot.child(auth.getCurrentUser().getUid()).getValue();
//                    if(status){
//                        post.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        studentdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                size = dataSnapshot.getChildrenCount();
                noticels.clear();
                for (long i=size;i>0;i--){

                    if(dataSnapshot.hasChild(String.valueOf(i))){
                        String title = dataSnapshot.child(String.valueOf(i)).child("title").getValue().toString();
                        String description = dataSnapshot.child(String.valueOf(i)).child("description").getValue().toString();
                        String posted_by = dataSnapshot.child(String.valueOf(i)).child("postedby").getValue().toString();
                        String date = dataSnapshot.child(String.valueOf(i)).child("time").getValue().toString();
                        String id = String.valueOf(i);
                        Notice noticel = new Notice(title,date,posted_by,description,id,false,false);
                        noticels.add(noticel);
                    }

                }
                if(getActivity()!=null){
                    pg.setVisibility(View.GONE);
                    noticeAdapterl = new IcoAdapter1(getActivity(),noticels,itemClickListener);
                    noticeAdapterl.notifyDataSetChanged();
                    listView.setAdapter(noticeAdapterl);
                    System.out.println(noticels);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("Post Notice");
                dialog.setContentView(R.layout.postnotice);
                dialog.setCancelable(false);
                final EditText date = (EditText)dialog.findViewById(R.id.date);
                final EditText title = (EditText)dialog.findViewById(R.id.titlen);
                final EditText postedby = (EditText)dialog.findViewById(R.id.postedby);
                final EditText descript = (EditText)dialog.findViewById(R.id.description);
                final EditText attent = (EditText)dialog.findViewById(R.id.attention);
                Button postn = (Button)dialog.findViewById(R.id.postn);
                Button cancel = (Button)dialog.findViewById(R.id.canceled);
                postn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(),PostingActivity.class));
                        String d = date.getText().toString();
                        String t = title.getText().toString();
                        String p = postedby.getText().toString();
                        String de = descript.getText().toString();
                        String at = attent.getText().toString();
                        Noticel noticel = new Noticel(t,d,p,at,de);
                        studentdatabase.child(String.valueOf(size)).setValue(noticel);
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return vi;
    }
}
