package com.example.anmol.hibiscus.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.anmol.hibiscus.Adapter.ELibraryAdapter;
import com.example.anmol.hibiscus.Adapter.SearchAdapter;
import com.example.anmol.hibiscus.Model.ELibrary;
import com.example.anmol.hibiscus.Model.Search;
import com.example.anmol.hibiscus.Mysingleton;
import com.example.anmol.hibiscus.R;
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
 * Created by anmol on 9/12/2017.
 */

public class students extends Fragment {
    List<Search>searches;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ListView listView;
    RadioButton rid,rname;
    ImageButton search;
    EditText student;
    ProgressBar progressBar;
    String method,studentdet,uid,pwd,dep;
    JSONObject jsonObject;
    String title,id;
    SearchAdapter searchAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.students,container,false);
        getActivity().setTitle("Students");
        searches = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        listView = (ListView)vi.findViewById(R.id.list);
        rid = (RadioButton)vi.findViewById(R.id.id);
        rname = (RadioButton)vi.findViewById(R.id.name);
        search = (ImageButton)vi.findViewById(R.id.search);
        student = (EditText)vi.findViewById(R.id.books);
        progressBar = (ProgressBar)vi.findViewById(R.id.load);
        jsonObject = new JSONObject();
        rid.setChecked(true);
        method = "ID";
        rid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rname.setChecked(false);
                rid.setChecked(true);
                method = "ID";
            }
        });
        rname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rid.setChecked(false);
                rname.setChecked(true);
                method = "NAME";
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentdet = student.getText().toString();
                if(!TextUtils.isEmpty(studentdet)){
                    searches.clear();
                    studentdet = studentdet.toUpperCase();

                    progressBar.setVisibility(View.VISIBLE);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null && dataSnapshot.child("sid").getValue()!=null && dataSnapshot.child("pwd").getValue()!=null){
                                uid = dataSnapshot.child("sid").getValue().toString();
                                pwd = dataSnapshot.child("pwd").getValue().toString();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.dcrypter) + pwd, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        dep = response;

                                        try {
                                            jsonObject.put("method",method);
                                            jsonObject.put("uid",uid);
                                            jsonObject.put("pwd",dep);
                                            jsonObject.put("query",studentdet);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.search_url), jsonObject, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                                try {
                                                    int c = 0;
                                                    searches.clear();
                                                    while (c<response.getJSONArray("Notices").length()){

                                                        JSONObject object = response.getJSONArray("Notices").getJSONObject(c);


                                                        title = object.getString("name");
                                                        id = object.getString("id");

                                                        Search search = new Search(id,title);
                                                        searches.add(search);
                                                        c++;
                                                    }
                                                    if(getActivity()!=null){
                                                        searchAdapter = new SearchAdapter(getActivity(),R.layout.search,searches);
                                                        searchAdapter.notifyDataSetChanged();
                                                        listView.setAdapter(searchAdapter);
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
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                Mysingleton.getInstance(getActivity()).addToRequestqueue(stringRequest);



                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else {
                    student.setError("Please mention some valid studentID or name!!!");
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = searches.get(i).getId();
                String url = getResources().getString(R.string.photo_url) + id + ".jpg";
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.stimage);
                ImageView img = (ImageView)dialog.findViewById(R.id.img);
                Glide.with(getActivity()).load(url).into(img);
                dialog.show();
            }
        });
        return vi;
    }
}
