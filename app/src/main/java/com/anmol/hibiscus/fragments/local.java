package com.anmol.hibiscus.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anmol.hibiscus.Adapter.IcoAdapter;
import com.anmol.hibiscus.Adapter.IcoAdapter1;
import com.anmol.hibiscus.Adapter.NoticeAdapterl;
import com.anmol.hibiscus.Helpers.Dbstudentnoticebookshelper;
import com.anmol.hibiscus.Helpers.Dbstudentnoticefirstopenhelper;
import com.anmol.hibiscus.Interfaces.ItemClickListener;
import com.anmol.hibiscus.Model.Notice;
import com.anmol.hibiscus.Model.Noticel;
import com.anmol.hibiscus.PostingActivity;
import com.anmol.hibiscus.R;
import com.anmol.hibiscus.WebviewActivity;
import com.bumptech.glide.Glide;
import com.github.fabtransitionactivity.SheetLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anmol on 2017-08-30.
 */

public class local extends Fragment implements SheetLayout.OnFabAnimationEndListener{
    FirebaseAuth auth;
    DatabaseReference databaseReference,studentdatabase;
    FloatingActionButton post;
    SheetLayout mSheetLayout;
    private static final int REQUEST_CODE = 1;
    List<Notice> noticels;
    RecyclerView listView;
    IcoAdapter1 noticeAdapterl;
    long size = 0;
    ProgressBar pg;
    ItemClickListener itemClickListener;
    CircleImageView showstar;
    Boolean starred;
    List<Notice> searchednotices;
    Button searchbtn,cancelbtn;
    EditText searchedit;
    TextView head,body;
    Button work;
    View margin;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.local,container,false);
        getActivity().setTitle("Notice Board");
        post = (FloatingActionButton)vi.findViewById(R.id.postnotice);
        mSheetLayout = (SheetLayout)vi.findViewById(R.id.bottom_sheet);
        listView = (RecyclerView) vi.findViewById(R.id.list);
        showstar = (CircleImageView)vi.findViewById(R.id.showstar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        searchbtn = vi.findViewById(R.id.searchbtn);
        cancelbtn = vi.findViewById(R.id.cancelbtn);
        searchedit = vi.findViewById(R.id.searchedit);
        searchednotices = new ArrayList<>();
        listView.setLayoutManager(layoutManager);
        listView.setHasFixedSize(true);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        head = (TextView)vi.findViewById(R.id.head);
        body = (TextView)vi.findViewById(R.id.body);
        work = (Button)vi.findViewById(R.id.work);
        margin = (View)vi.findViewById(R.id.margin);
        pg = (ProgressBar)vi.findViewById(R.id.load);
        pg.setVisibility(View.VISIBLE);
        noticels = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        studentdatabase = FirebaseDatabase.getInstance().getReference().child("Studentnoticeboard");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Auth");
        mSheetLayout.setFab(post);
        mSheetLayout.setFabAnimationEndListener(this);
        itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {

            }
        };
        final Dbstudentnoticebookshelper dbstudentnoticebookshelper = new Dbstudentnoticebookshelper(getActivity());
        final Dbstudentnoticefirstopenhelper dbstudentnoticefirstopenhelper = new Dbstudentnoticefirstopenhelper(getActivity());
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchedit.setVisibility(View.VISIBLE);
                searchedit.requestFocus();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(searchedit, 0);
                searchbtn.setVisibility(View.GONE);
                cancelbtn.setVisibility(View.VISIBLE);
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchedit.setVisibility(View.INVISIBLE);
                searchedit.clearFocus();
                searchedit.setText(null);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getView().getWindowToken(), 0);
                cancelbtn.setVisibility(View.GONE);
                searchbtn.setVisibility(View.VISIBLE);
            }
        });
        starred = false;
        Glide.with(getActivity()).load(R.drawable.starunfillwhite).into(showstar);
        loadnotice(dbstudentnoticebookshelper,dbstudentnoticefirstopenhelper);
        showstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!starred){

                    starred = true;
                    Glide.with(getActivity()).load(R.drawable.stargolden).into(showstar);
                    loadbook(dbstudentnoticebookshelper,dbstudentnoticefirstopenhelper);
                }
                else{

                    starred = false;
                    Glide.with(getActivity()).load(R.drawable.starunfillwhite).into(showstar);
                    loadnotice(dbstudentnoticebookshelper,dbstudentnoticefirstopenhelper);
                }
            }
        });


        final DatabaseReference data = FirebaseDatabase.getInstance().getReference().getRoot().child("studentbanner");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dhead = dataSnapshot.child("head").getValue(String.class);
                String dbody = dataSnapshot.child("body").getValue(String.class);
                String dname = dataSnapshot.child("button").child("name").getValue(String.class);
                final String dlink = dataSnapshot.child("button").child("link").getValue(String.class);
                final Boolean webview = dataSnapshot.child("button").child("webview").getValue(Boolean.class);

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
                            if(webview){
                                Intent webintent = new Intent(getActivity(),WebviewActivity.class);
                                webintent.putExtra("weburl",dlink);
                                startActivity(webintent);
                            }
                            else {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dlink));
                                startActivity(browserIntent);
                            }

                        }

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSheetLayout.expandFab();

            }
        });
        return vi;
    }



    private void loadbook(final Dbstudentnoticebookshelper dbstudentnoticebookshelper, final Dbstudentnoticefirstopenhelper dbstudentnoticefirstopenhelper) {
        final List<String> boomarks = dbstudentnoticebookshelper.readbook();
        final List<String> firstopens = dbstudentnoticefirstopenhelper.readbook();
        studentdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                size = dataSnapshot.getChildrenCount();
                noticels.clear();
                for (long i=size;i>0;i--){

                    if(dataSnapshot.hasChild(String.valueOf(i))){
                        String title = dataSnapshot.child(String.valueOf(i)).child("title").getValue(String.class);
                        String description = dataSnapshot.child(String.valueOf(i)).child("description").getValue(String.class);
                        String posted_by = dataSnapshot.child(String.valueOf(i)).child("postedby").getValue(String.class);
                        String date = dataSnapshot.child(String.valueOf(i)).child("time").getValue(String.class);
                        Boolean deleted = dataSnapshot.child(String.valueOf(i)).child("deleted").getValue(Boolean.class);
                        String id = String.valueOf(i);
                        int j = 0;
                        Boolean booked = false;
                        while(j<boomarks.size()){
                            if(boomarks.get(j).equals(id)){
                                booked = true;
                            }
                            j++;
                        }
                        int k = 0;
                        Boolean firstopen = false;
                        while(k<firstopens.size()){
                            if(firstopens.get(k).equals(id)){
                                firstopen = true;
                            }
                            k++;
                        }
                        if(!deleted){
                            if(booked){
                                Notice noticel = new Notice(title,date,posted_by,description,id, true,firstopen);
                                noticels.add(noticel);
                            }
                        }


                    }

                }
                if(getActivity()!=null){
                    if(!noticels.isEmpty()){
                        pg.setVisibility(View.GONE);
                        noticeAdapterl = new IcoAdapter1(getActivity(),noticels,itemClickListener);
                        noticeAdapterl.notifyDataSetChanged();
                        listView.setAdapter(noticeAdapterl);
                        searchedit.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                searchednotices.clear();

                                for(int j=0;j<noticels.size();j++){
                                    if(noticels.get(j).getTitle().toLowerCase().contains(charSequence) ||
                                            noticels.get(j).getTitle().toUpperCase().contains(charSequence) ||
                                            noticels.get(j).getPosted_by().toLowerCase().contains(charSequence) ||
                                            noticels.get(j).getPosted_by().toUpperCase().contains(charSequence)
                                            ){
                                        searchednotices.add(noticels.get(j));
                                    }
                                }

                                noticeAdapterl = new IcoAdapter1(getActivity(),searchednotices,itemClickListener);
                                noticeAdapterl.notifyDataSetChanged();
                                listView.setAdapter(noticeAdapterl);
                            }


                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        System.out.println(noticels);
                    }
                    else{
                        Toast.makeText(getActivity(),"You don't have any starred notices yet",Toast.LENGTH_SHORT).show();
                        loadnotice(dbstudentnoticebookshelper,dbstudentnoticefirstopenhelper);
                        starred = false;
                        Glide.with(getActivity()).load(R.drawable.starunfillwhite).into(showstar);
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadnotice(Dbstudentnoticebookshelper dbstudentnoticebookshelper, Dbstudentnoticefirstopenhelper dbstudentnoticefirstopenhelper) {
        final List<String> boomarks = dbstudentnoticebookshelper.readbook();
        final List<String> firstopens = dbstudentnoticefirstopenhelper.readbook();
        studentdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                size = dataSnapshot.getChildrenCount();
                noticels.clear();
                for (long i=size;i>0;i--){

                    if(dataSnapshot.hasChild(String.valueOf(i))){
                        String title = dataSnapshot.child(String.valueOf(i)).child("title").getValue(String.class);
                        String description = dataSnapshot.child(String.valueOf(i)).child("description").getValue(String.class);
                        String posted_by = dataSnapshot.child(String.valueOf(i)).child("postedby").getValue(String.class);
                        String date = dataSnapshot.child(String.valueOf(i)).child("time").getValue(String.class);
                        Boolean deleted = dataSnapshot.child(String.valueOf(i)).child("deleted").getValue(Boolean.class);
                        String id = String.valueOf(i);
                        int j = 0;
                        Boolean booked = false;
                        while(j<boomarks.size()){
                            if(boomarks.get(j).equals(id)){
                                booked = true;
                            }
                            j++;
                        }
                        int k = 0;
                        Boolean firstopen = false;
                        while(k<firstopens.size()){
                            if(firstopens.get(k).equals(id)){
                                firstopen = true;
                            }
                            k++;
                        }
                        if(!deleted){
                            Notice noticel = new Notice(title,date,posted_by,description,id,booked,firstopen);
                            noticels.add(noticel);
                        }

                    }

                }
                if(getActivity()!=null){
                    pg.setVisibility(View.GONE);
                    noticeAdapterl = new IcoAdapter1(getActivity(),noticels,itemClickListener);
                    noticeAdapterl.notifyDataSetChanged();
                    listView.setAdapter(noticeAdapterl);
                    System.out.println(noticels);
                }
                searchedit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        searchednotices.clear();

                        for(int j=0;j<noticels.size();j++){
                            if(noticels.get(j).getTitle().toLowerCase().contains(charSequence) ||
                                    noticels.get(j).getTitle().toUpperCase().contains(charSequence) ||
                                    noticels.get(j).getPosted_by().toLowerCase().contains(charSequence) ||
                                    noticels.get(j).getPosted_by().toUpperCase().contains(charSequence)
                                    ){
                                searchednotices.add(noticels.get(j));
                            }
                        }

                        noticeAdapterl = new IcoAdapter1(getActivity(),searchednotices,itemClickListener);
                        noticeAdapterl.notifyDataSetChanged();
                        listView.setAdapter(noticeAdapterl);
                    }


                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(getActivity(), PostingActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            mSheetLayout.contractFab();
        }
    }
}
