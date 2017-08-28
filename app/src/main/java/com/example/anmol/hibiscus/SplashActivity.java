package com.example.anmol.hibiscus;

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
import com.example.anmol.hibiscus.Model.Notice;
import com.example.anmol.hibiscus.services.RequestService;
import com.example.anmol.hibiscus.services.RequestServiceAttendance;
import com.example.anmol.hibiscus.services.RequestServiceGrades;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    Animation animFadein,zoomin;
    ImageView img;
    TextView text;
    String url = "http://139.59.23.157/api/hibi/notice";
    ProgressBar progressBar;
    String title,date,postedby,id,attention;
    ArrayList<Notice> notices;
    int key;
    FirebaseAuth auth;
    String uid,pwd;
    DatabaseReference mdatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()==null){
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
        }
        else{
            Intent intent = new Intent(this, RequestService.class);
            startService(intent);
            mdatabase = FirebaseDatabase.getInstance().getReference().child("Notice");
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
            progressBar = (ProgressBar)findViewById(R.id.load);
            progressBar.setVisibility(View.VISIBLE);
            notices = new ArrayList<>();

            text = (TextView)findViewById(R.id.textView5);
            text.setVisibility(View.INVISIBLE);
            img = (ImageView)findViewById(R.id.imageView2);
            animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.fade_in);
            zoomin = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
            img.startAnimation(zoomin);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, RequestServiceAttendance.class);
                    startService(intent);
                    text.startAnimation(animFadein);

                }
            },1000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            uid = dataSnapshot.child("sid").getValue().toString();
                            pwd = dataSnapshot.child("pwd").getValue().toString();
                            final String uidu = uid.toUpperCase();
                            final String url = "https://hib.iiit-bh.ac.in/Hibiscus/docs/iiit/Photos/" + uidu + ".jpg";

                            Intent intent = new Intent(SplashActivity.this, HibiscusActivity.class);
                            intent.putExtra("url", url);
                            intent.putExtra("uidu", uidu);
                            startActivity(intent);
                            overridePendingTransition(R.anim.still,R.anim.slide_in_up);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Intent intent = new Intent(SplashActivity.this, RequestServiceGrades.class);
                    startService(intent);
                }
            },2000);

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
