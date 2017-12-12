package com.anmol.hibiscus;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.anmol.hibiscus.fragments.complaints;
import com.anmol.hibiscus.fragments.ebooks;
import com.anmol.hibiscus.fragments.fees;
import com.anmol.hibiscus.fragments.library;
import com.anmol.hibiscus.fragments.commapps;
import com.anmol.hibiscus.fragments.courseware;
import com.anmol.hibiscus.fragments.local;
import com.anmol.hibiscus.fragments.main;
import com.anmol.hibiscus.fragments.moocs;
import com.anmol.hibiscus.fragments.myapps;
import com.anmol.hibiscus.fragments.mypage;
import com.anmol.hibiscus.fragments.students;
import com.anmol.hibiscus.fragments.subgrades;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HibiscusActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String sid;
    private static long back_pressed;
    DrawerLayout drawer;
    FirebaseAuth auth;
    int sem;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hibiscus);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");

        setTitle("IIITcloud");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        final CircleImageView imageView = (CircleImageView) header.findViewById(R.id.dph);
        final TextView sid = (TextView)header.findViewById(R.id.sid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("sid").getValue(String.class)!=null){
                    String uid = dataSnapshot.child("sid").getValue(String.class);
                    String uidu = uid.toUpperCase();
                    String urlid = "https://hib.iiit-bh.ac.in/Hibiscus/docs/iiit/Photos/" + uidu + ".jpg";
                    Glide.with(HibiscusActivity.this).load(urlid).into(imageView);
                    sid.setText(uidu);
                    String yr = String.valueOf(uidu.charAt(2)) + String.valueOf(uidu.charAt(3));
                    int y = Integer.parseInt(yr);
                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    String cr = String.valueOf(year);
                    int month = c.get(Calendar.MONTH);
                    String cyr = String.valueOf(cr.charAt(2)) + String.valueOf(cr.charAt(3));
                    year = Integer.parseInt(cyr);
                    if(month>6){
                        sem = 2*(year - y) + 1;
                    }
                    else{
                        sem = 2*(year - y);
                    }
                    if(sem<9){
                        Map<String,Object> map = new HashMap<>();
                        map.put("semester",sem);
                        FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus").updateChildren(map);
                    }


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_hib,new main()).commit();
        fm.executePendingTransactions();
    }
    private class versionCheck extends AsyncTask< Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            HttpHandler sh = new HttpHandler();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else if(back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.still,R.anim.slide_out_down);
        }else {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_hib,new main()).commit();
            Toast.makeText(getBaseContext(), "Double tap to exit!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hibiscus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(drawer.isDrawerOpen(Gravity.RIGHT)){
                drawer.closeDrawer(Gravity.RIGHT);
            }
            else{
                drawer.openDrawer(Gravity.RIGHT);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Handler handler = new Handler();
        int id = item.getItemId();
        final FragmentManager fm = getFragmentManager();
        if (id == R.id.nav_hibiscus) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fm.beginTransaction().replace(R.id.content_hib,new main()).commit();
                    fm.executePendingTransactions();
                }
            },175);

        }
        else if (id == R.id.nav_others) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fm.beginTransaction().replace(R.id.content_hib,new local()).commit();
                    fm.executePendingTransactions();
                }
            },175);} else if (id == R.id.nav_myapps) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fm.beginTransaction().replace(R.id.content_hib,new myapps()).commit();
                    fm.executePendingTransactions();
                }
            },175);

        } else if (id == R.id.nav_course) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fm.beginTransaction().replace(R.id.content_hib,new courseware()).commit();
                    fm.executePendingTransactions();
                }
            },175);

        } else if (id == R.id.nav_comm) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fm.beginTransaction().replace(R.id.content_hib,new commapps()).commit();
                    fm.executePendingTransactions();
                }
            },225);

        }else if(id == R.id.nav_lib){
            fm.beginTransaction().replace(R.id.content_hib,new library()).commit();
            fm.executePendingTransactions();
        }else if(id == R.id.nav_viewgrades){
            fm.beginTransaction().replace(R.id.content_hib,new subgrades()).commit();
            fm.executePendingTransactions();
        }
//        else if(id == R.id.nav_elib){
//            fm.beginTransaction().replace(R.id.content_hib,new ebooks()).commit();
//        }
        else if(id == R.id.nav_students){
            fm.beginTransaction().replace(R.id.content_hib,new students()).commit();
            fm.executePendingTransactions();
        } else if(id == R.id.fees){
            fm.beginTransaction().replace(R.id.content_hib,new fees()).commit();
            fm.executePendingTransactions();
        }else if(id == R.id.nav_moocs){
            fm.beginTransaction().replace(R.id.content_hib,new moocs()).commit();
            fm.executePendingTransactions();
        }else if(id == R.id.nav_comp){
            fm.beginTransaction().replace(R.id.content_hib,new complaints()).commit();
            fm.executePendingTransactions();
        }else if(id == R.id.nav_con){
            fm.beginTransaction().replace(R.id.content_hib,new mypage()).commit();
            fm.executePendingTransactions();
        } else if (id == R.id.nav_help) {
            FirebaseAuth.getInstance().signOut();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(HibiscusActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_down);
                }
            },100);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(HibiscusActivity.this,"Email sent...",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(HibiscusActivity.this,"Failed to send E-mail!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




}
