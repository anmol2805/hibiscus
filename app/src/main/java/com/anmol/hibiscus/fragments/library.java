package com.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.anmol.hibiscus.Adapter.CourseNoticeAdapter;
import com.anmol.hibiscus.Adapter.LibraryAdapter;
import com.anmol.hibiscus.Courselistnotice;
import com.anmol.hibiscus.Model.Coursenotice;
import com.anmol.hibiscus.Model.Library;
import com.anmol.hibiscus.Mysingleton;
import com.anmol.hibiscus.R;
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
 * Created by anmol on 9/11/2017.
 */

public class library extends Fragment {
    ImageButton search;
    EditText books;
    ListView lv;
    List<Library> libraries;
    ProgressBar progressBar;
    String bookn;
    DatabaseReference hibdatabase;
    FirebaseAuth auth;
    String uid,pwd,dep,title,id,author,publisher,year,edition,status;
    JSONObject jsonObject;
    LibraryAdapter libraryAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.library,container,false);
        getActivity().setTitle("Library");
        search = (ImageButton)vi.findViewById(R.id.search);
        books = (EditText)vi.findViewById(R.id.books);
        lv = (ListView)vi.findViewById(R.id.list);
        progressBar = (ProgressBar)vi.findViewById(R.id.load);
        libraries = new ArrayList<>();
        jsonObject = new JSONObject();
        auth = FirebaseAuth.getInstance();
        hibdatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookn = books.getText().toString();
                if(!TextUtils.isEmpty(bookn)){
                    libraries.clear();

                    progressBar.setVisibility(View.VISIBLE);
                    hibdatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null && dataSnapshot.child("sid").getValue()!=null && dataSnapshot.child("pwd").getValue()!=null){
                                uid = dataSnapshot.child("sid").getValue().toString();
                                pwd = dataSnapshot.child("pwd").getValue().toString();
                                try {
                                    jsonObject.put("name",bookn);
                                    jsonObject.put("uid",uid);
                                    jsonObject.put("pwd",pwd);
                                    jsonObject.put("pass","encrypt");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.library_url), jsonObject, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        try {
                                            int c = 0;
                                            libraries.clear();
                                            while (c<response.getJSONArray("Notices").length()){

                                                JSONObject object = response.getJSONArray("Notices").getJSONObject(c);


                                                title = object.getString("title");
                                                id = object.getString("id");
                                                author = object.getString("author");
                                                publisher = object.getString("publiser");
                                                edition = object.getString("edition");
                                                year = object.getString("year");
                                                status = object.getString("status");
                                                Library library = new Library(id,title,author,publisher,year,edition,status);
                                                libraries.add(library);
                                                c++;
                                            }
                                            if(getActivity()!=null){
                                                libraryAdapter = new LibraryAdapter(getActivity(),R.layout.lib,libraries);
                                                libraryAdapter.notifyDataSetChanged();
                                                lv.setAdapter(libraryAdapter);
                                                progressBar.setVisibility(View.GONE);
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getActivity(),"Network Error!!!",Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                                Mysingleton.getInstance(getActivity()).addToRequestqueue(jsonObjectRequest);



                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else {
                    books.setError("Please mention some keywords related to your book!!!");
                }
            }
        });
        return vi;

    }
}