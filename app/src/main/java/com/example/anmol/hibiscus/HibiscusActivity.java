package com.example.anmol.hibiscus;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anmol.hibiscus.fragments.commapps;
import com.example.anmol.hibiscus.fragments.courseware;
import com.example.anmol.hibiscus.fragments.help;
import com.example.anmol.hibiscus.fragments.local;
import com.example.anmol.hibiscus.fragments.main;
import com.example.anmol.hibiscus.fragments.myapps;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class HibiscusActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String sid;
    private static long back_pressed;
    DrawerLayout drawer;
    FirebaseAuth auth;
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

        String url = getIntent().getStringExtra("url");
        String uidu = getIntent().getStringExtra("uidu");
        if(!auth.getCurrentUser().isEmailVerified()){
            alert();
        }
        setTitle("IIITcloud");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        CircleImageView imageView = (CircleImageView) header.findViewById(R.id.dph);
        if(url!=null){
            Glide.with(HibiscusActivity.this).load(url).into(imageView);
        }

        TextView sid = (TextView)header.findViewById(R.id.sid);
        if(uidu!=null){
            sid.setText(uidu);
        }

        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_hib,new main()).commit();
    }

    private void alert() {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Email Verification")
                .setMessage("We have sent you an E-mail to "+auth.getCurrentUser().getEmail()+".Click on the received link and verify your E-mail to continue")
                .setPositiveButton("Already Verified", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(!auth.getCurrentUser().isEmailVerified()){
                            alert();
                        }
                    }
                })
                .setNegativeButton("Resend E-mail", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendVerificationEmail();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();
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
                }
            },175);

        }
        else if (id == R.id.nav_others) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fm.beginTransaction().replace(R.id.content_hib,new local()).commit();
                }
            },175);} else if (id == R.id.nav_myapps) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fm.beginTransaction().replace(R.id.content_hib,new myapps()).commit();
                }
            },175);

        } else if (id == R.id.nav_course) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fm.beginTransaction().replace(R.id.content_hib,new courseware()).commit();
                }
            },175);

        } else if (id == R.id.nav_comm) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fm.beginTransaction().replace(R.id.content_hib,new commapps()).commit();
                }
            },225);

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
