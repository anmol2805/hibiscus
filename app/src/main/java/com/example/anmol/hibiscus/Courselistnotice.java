package com.example.anmol.hibiscus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.anmol.hibiscus.Adapter.CourseNoticeAdapter;
import com.example.anmol.hibiscus.Model.Coursenotice;
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

public class Courselistnotice extends AppCompatActivity {
    String id;
    FirebaseAuth auth;
    String uid,pwd,dep;
    DatabaseReference databaseReference,cnd;
    JSONObject jsonObject = new JSONObject();
    String url1 = "http://139.59.23.157/api/hibi/course_notice";
    String decrypt = "https://us-central1-iiitcloud-e9d6b.cloudfunctions.net/dcryptr?pass=";
    String title,date,link;
    List<Coursenotice>coursenotices;
    ListView listView;
    CourseNoticeAdapter courseNoticeAdapter;
    ProgressBar ld;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coursenotice);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        cnd = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        id = getIntent().getStringExtra("id");
        coursenotices = new ArrayList<>();
        ld = (ProgressBar)findViewById(R.id.loadnd);
        ld.setVisibility(View.VISIBLE);
        listView = (ListView)findViewById(R.id.list);
        cnd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.child("sid").getValue()!=null && dataSnapshot.child("pwd").getValue()!=null){
                    uid = dataSnapshot.child("sid").getValue().toString();
                    pwd = dataSnapshot.child("pwd").getValue().toString();
                    try {
                        jsonObject.put("id",id);
                        jsonObject.put("uid",uid);
                        jsonObject.put("pwd",pwd);
                        jsonObject.put("pass","encrypt");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.cn_url), jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ld.setVisibility(View.GONE);
                            try {
                                int c = 1;
                                coursenotices.clear();
                                while (c<response.getJSONArray("Notices").length()){

                                    JSONObject object = response.getJSONArray("Notices").getJSONObject(c);


                                    title = object.getString("title");
                                    date = object.getString("date");
                                    link = object.getString("link_id");
                                    Coursenotice coursenotice = new Coursenotice(date,title,link);
                                    coursenotices.add(coursenotice);
                                    c++;
                                }
                                if(getApplicationContext()!=null){
                                    courseNoticeAdapter = new CourseNoticeAdapter(Courselistnotice.this,R.layout.cn,coursenotices);
                                    courseNoticeAdapter.notifyDataSetChanged();
                                    listView.setAdapter(courseNoticeAdapter);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ld.setVisibility(View.GONE);
                            Toast.makeText(Courselistnotice.this,"Network Error!!!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    Mysingleton.getInstance(getApplicationContext()).addToRequestqueue(jsonObjectRequest);



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Courselistnotice.this,CourseData.class);
                intent.putExtra("link",coursenotices.get(i).getLink_id());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up,R.anim.still);
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.still,R.anim.slide_out_down);
    }
}
