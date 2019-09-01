package com.anmol.hibiscus;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anmol.hibiscus.Helpers.Dbhelper;
import com.bumptech.glide.Glide;
import com.anmol.hibiscus.Model.Notice;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class SplashActivity extends AppCompatActivity {

        Animation animFadein,zoomin;
        ImageView img;

        String url = "http://139.59.23.157/api/hibi/notice";
        ProgressBar progressBar;
        String title,date,postedby,id,attention;
        ArrayList<Notice> notices;
        int key;
        FirebaseAuth auth;
        String uid,pwd,urlid,uidu;
        DatabaseReference mdatabase;
        CircleImageView sanmol,sankit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sanmol = (CircleImageView)findViewById(R.id.sanmol);
        sankit = (CircleImageView)findViewById(R.id.sankit);
        Glide.with(this).load(R.drawable.anmol).into(sanmol);
        Glide.with(this).load(R.drawable.ankitnew).into(sankit);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()==null){
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
        }
        else{
//            Intent intent = newfeature Intent(this, RequestService.class);
//            startService(intent);
//            mdatabase = FirebaseDatabase.getInstance().getReference().child("Notice");
//            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
            progressBar = (ProgressBar)findViewById(R.id.load);
            progressBar.setVisibility(View.VISIBLE);
            //notices = newfeature ArrayList<>();

            img = (ImageView)findViewById(R.id.imageView2);
            animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.fade_in);
            zoomin = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
            img.startAnimation(zoomin);
            Dbhelper helper = new Dbhelper(this);
            List<Notice> notices1 = helper.readData("Select * from notice_table ORDER BY notice_id DESC");
            ArrayList<String> noticeids = new ArrayList<>();
            for(int i = 0;i<notices1.size();i++){
                noticeids.add(notices1.get(i).getId());
            }
            loaddata(noticeids);
//            Handler handler = newfeature Handler();
//
//
//            handler.postDelayed(newfeature Runnable() {
//                @Override
//                public void run() {
//                    progressBar.setVisibility(View.INVISIBLE);
//                    Intent intent = newfeature Intent(SplashActivity.this, HibiscusActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.still,R.anim.slide_in_up);
//                    databaseReference.addValueEventListener(newfeature ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if(dataSnapshot!=null && dataSnapshot.child("sid").getValue()!=null && dataSnapshot.child("pwd").getValue()!=null){
//                                uid = dataSnapshot.child("sid").getValue().toString();
//                                pwd = dataSnapshot.child("pwd").getValue().toString();
//                                uidu = uid.toUpperCase();
//                                urlid = "https://hib.iiit-bh.ac.in/Hibiscus/docs/iiit/Photos/" + uidu + ".jpg";
//
//                            }
//
//
//                            intent.putExtra("url", urlid);
//                            intent.putExtra("uidu", uidu);
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                    Intent intent = newfeature Intent(SplashActivity.this, RequestServiceGrades.class);
//                    startService(intent);
//                }
//            },3000);

        }


    }

    private void loaddata(final ArrayList<String> noticeids) {
        final JSONObject jsonObject = new JSONObject();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();
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
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.notice_url), jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                Dbhelper dbhelper = new Dbhelper(SplashActivity.this);
                                int c = 0;
                                while (c<response.getJSONArray("Notices").length()){

                                    JSONObject object = response.getJSONArray("Notices").getJSONObject(c);


                                    title = object.getString("title");
                                    date = object.getString("date");
                                    postedby = object.getString("posted_by");
                                    attention = object.getString("attention");
                                    id = object.getString("id");
                                    Notice notice = new Notice(title,date,postedby,attention,id,false,false);
                                    int k=0;
                                    for(int j = 0;j<noticeids.size();j++){
                                        if(noticeids.get(j).equals(id)){
                                            k=1;
                                        }
                                    }
                                    if(k==0){
                                        System.out.print("noticestatus:newfeature entry");
                                        dbhelper.insertData(notice);
                                    }
                                    else{
                                        System.out.print("noticestatus:already present");
                                    }
                                    dbhelper.updatenotice(notice);
                                    c++;
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(SplashActivity.this, HibiscusActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.still,R.anim.slide_in_up);

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
                                Notice notice = new Notice(title,date,postedby,attention,id,false,false);
                                FirebaseDatabase.getInstance().getReference().child("Notices").setValue(notice);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(SplashActivity.this, HibiscusActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.still,R.anim.slide_in_up);
                        }
                    });
                    Mysingleton.getInstance(getApplicationContext()).addToRequestqueue(jsonObjectRequest);



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
        overridePendingTransition(R.anim.still,R.anim.slide_in_up);
    }
}
