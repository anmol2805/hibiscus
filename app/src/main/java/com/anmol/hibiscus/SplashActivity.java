package com.anmol.hibiscus;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.anmol.hibiscus.Model.Notice;
import com.anmol.hibiscus.services.RequestService;
import com.anmol.hibiscus.services.RequestServiceAttendance;
import com.anmol.hibiscus.services.RequestServiceCourses;
import com.anmol.hibiscus.services.RequestServiceGrades;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
//            Intent intent = new Intent(this, RequestService.class);
//            startService(intent);
            mdatabase = FirebaseDatabase.getInstance().getReference().child("Notice");
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
            progressBar = (ProgressBar)findViewById(R.id.load);
            progressBar.setVisibility(View.VISIBLE);
            notices = new ArrayList<>();

            img = (ImageView)findViewById(R.id.imageView2);
            animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.fade_in);
            zoomin = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
            img.startAnimation(zoomin);
            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(SplashActivity.this, RequestServiceAttendance.class);
//                    startService(intent);
//
//
//                }
//            },1000);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(SplashActivity.this, RequestServiceCourses.class);
//                    startService(intent);
//
//
//                }
//            },2000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(SplashActivity.this, HibiscusActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.still,R.anim.slide_in_up);
//                    databaseReference.addValueEventListener(new ValueEventListener() {
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
//                    Intent intent = new Intent(SplashActivity.this, RequestServiceGrades.class);
//                    startService(intent);
                }
            },3000);

        }


    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
        overridePendingTransition(R.anim.still,R.anim.slide_in_up);
    }
}
