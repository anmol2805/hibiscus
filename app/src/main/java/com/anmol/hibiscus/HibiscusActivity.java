package com.anmol.hibiscus;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anmol.hibiscus.fragments.mainold;
import com.anmol.hibiscus.fragments.privacypolicy;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

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
    String url = "http://14.139.198.171/api/hibi/login_test";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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


        setTitle("IIITcloud");
        System.out.println("notiftoken" + FirebaseInstanceId.getInstance().getToken());
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        final CircleImageView imageView = (CircleImageView) header.findViewById(R.id.dph);
        final TextView sid = (TextView)header.findViewById(R.id.sid);
        if(auth.getCurrentUser()!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("sid").getValue(String.class)!=null){
                        String uid = dataSnapshot.child("sid").getValue(String.class);
                        String uidu = uid.toUpperCase();
                        String urlid = "https://hib.iiit-bh.ac.in/Hibiscus/docs/iiit/Photos/" + uidu + ".jpg";
                        Glide.with(getApplicationContext()).load(urlid).into(imageView);
                        sid.setText(uidu);
                        String yr = String.valueOf(uidu.charAt(2)) + String.valueOf(uidu.charAt(3));
                        try{
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
                        catch (NumberFormatException e){
                            e.printStackTrace();
                            String titleText = "user Id not compatible";

                            // Initialize a newfeature foreground color span instance
                            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));

                            // Initialize a newfeature spannable string builder instance
                            SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

                            ssBuilder.setSpan(
                                    foregroundColorSpan,
                                    0,
                                    titleText.length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            );
                            AlertDialog dialog = new AlertDialog.Builder(HibiscusActivity.this)
                                    .setTitle(ssBuilder)
                                    .setMessage("Canopy is only for students and work only with student ids. Canopy is not compatible with this id. Please logout and login as student to continue our services.")
                                    .setCancelable(false)
                                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            FirebaseAuth.getInstance().signOut();
                                            Intent intent = new Intent(HibiscusActivity.this,LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_down);


                                        }
                                    })
                                    .create();
                            dialog.show();
                        }


                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HibiscusActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_down);
        }



        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_hib,new mainold()).commitAllowingStateLoss();
        fm.executePendingTransactions();
        checkpassstatus();
        checkupdatestatus();
        TextView view = (TextView) navigationView.getMenu().findItem(R.id.nav_others).getActionView();
        view.setText("1");
    }
    private void checkpassstatus(){
        final JSONObject jsonObject = new JSONObject();
        if(auth.getCurrentUser()!=null){
            FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("sid").getValue(String.class)!=null && dataSnapshot.child("pwd").getValue(String.class)!=null){
                        final String uid = dataSnapshot.child("sid").getValue(String.class);
                        String pwd = dataSnapshot.child("pwd").getValue(String.class);
                        try {
                            jsonObject.put("uid",uid);
                            jsonObject.put("pwd",pwd);
                            jsonObject.put("method","encrypted");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    System.out.println(response);
                                    System.out.println(jsonObject);

                                    if(response.getString("result").equals("failed")){
//                                    FirebaseDatabase.getInstance().getReference().child("passwordreset").addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                            try{
//
//                                            }
//                                            catch (IllegalStateException e){
//                                                e.printStackTrace();
//                                            }
//
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
                                        String titleText = "Password change detected!";

                                        // Initialize a newfeature foreground color span instance
                                        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));

                                        // Initialize a newfeature spannable string builder instance
                                        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

                                        ssBuilder.setSpan(
                                                foregroundColorSpan,
                                                0,
                                                titleText.length(),
                                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                        );
                                        AlertDialog dialog = new AlertDialog.Builder(HibiscusActivity.this)
                                                .setTitle(ssBuilder)
                                                .setMessage("Canopy detected a password change for your Hibiscus account. We suggest resetting the Canopy password with your new Hibiscus password to continue using the service.")
                                                .setCancelable(false)
                                                .setPositiveButton("Reset password", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        FirebaseAuth.getInstance().sendPasswordResetEmail(uid + "@iiit-bh.ac.in").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                FirebaseAuth.getInstance().signOut();
                                                                Intent intent = new Intent(HibiscusActivity.this,LoginActivity.class);
                                                                intent.putExtra("type","resetpass");
                                                                intent.putExtra("email",uid);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(intent);
                                                                finish();
                                                                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_down);
                                                            }
                                                        });

                                                    }
                                                })
                                                .create();
                                        dialog.show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        Mysingleton.getInstance(HibiscusActivity.this).addToRequestqueue(jsonObjectRequest);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HibiscusActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_down);
        }

    }


        private void checkupdatestatus() {
        final DatabaseReference dtb = FirebaseDatabase.getInstance().getReference().getRoot();
        dtb.child("dynamiclocks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean upd = dataSnapshot.child("updatelock").getValue(Boolean.class);
                if(!upd){
                    dtb.child("banner").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("vname").getValue(String.class)!=null){
                                String updateversion = dataSnapshot.child("vname").getValue(String.class);
                                try {
                                    PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(),0);
                                    String version = pinfo.versionName.trim();
                                    AlertDialog dialog = new AlertDialog.Builder(HibiscusActivity.this)
                                            .setTitle("New version available")
                                            .setMessage("Please, update app to newfeature version to continue!!!")
                                            .setCancelable(false)
                                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Uri uri = Uri.parse("market://details?id=" + "com.anmol.hibiscus");
                                                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                                    // To count with Play market backstack, After pressing back button,
                                                    // to taken back to our application, we need to add following flags to intent.
                                                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                                                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                                    try{
                                                        startActivity(goToMarket);
                                                    }
                                                    catch (ActivityNotFoundException e){
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "com.anmol.hibiscus")));
                                                    }



                                                }
                                            }).create();
                                    if (version.equals(updateversion)){
                                        dialog.dismiss();
                                    }
                                    else {

                                        dialog.show();
                                    }
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
            fm.beginTransaction().replace(R.id.content_hib,new mainold()).commitAllowingStateLoss();
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
                    fm.beginTransaction().replace(R.id.content_hib,new mainold()).commitAllowingStateLoss();
                    fm.executePendingTransactions();
                }
            },175);

        }
        else if (id == R.id.nav_others) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fm.beginTransaction().replace(R.id.content_hib,new local()).commitAllowingStateLoss();
                    fm.executePendingTransactions();
                }
            },175);}

//        else if (id == R.id.nav_rasoi) {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    fm.beginTransaction().replace(R.id.content_hib,new rosei()).commitAllowingStateLoss();
//                    fm.executePendingTransactions();
//                }
//            },175);}
            else if (id == R.id.nav_myapps) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                            fm.beginTransaction().replace(R.id.content_hib,new myapps()).commitAllowingStateLoss();
                            fm.executePendingTransactions();


                    }
                },175);



        } else if (id == R.id.nav_course) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fm.beginTransaction().replace(R.id.content_hib,new courseware()).commitAllowingStateLoss();
                    fm.executePendingTransactions();
                }
            },175);

        } else if (id == R.id.nav_comm) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                            fm.beginTransaction().replace(R.id.content_hib,new commapps()).commitAllowingStateLoss();
                            fm.executePendingTransactions();


                    }
                },225);



        }else if(id == R.id.nav_lib){
            fm.beginTransaction().replace(R.id.content_hib,new library()).commitAllowingStateLoss();
            fm.executePendingTransactions();
        }else if(id == R.id.nav_viewgrades){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                        fm.beginTransaction().replace(R.id.content_hib,new subgrades()).commitAllowingStateLoss();
                        fm.executePendingTransactions();


                }
            },175);



        }
        else if(id == R.id.nav_pp){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    fm.beginTransaction().replace(R.id.content_hib,new privacypolicy()).commitAllowingStateLoss();
                    fm.executePendingTransactions();


                }
            },175);



        }
//        else if(id == R.id.nav_elib){
//            fm.beginTransaction().replace(R.id.content_hib,newfeature ebooks()).commit();
//        }
        else if(id == R.id.nav_students){
            fm.beginTransaction().replace(R.id.content_hib,new students()).commitAllowingStateLoss();
            fm.executePendingTransactions();
        } else if(id == R.id.fees){
            fm.beginTransaction().replace(R.id.content_hib,new fees()).commitAllowingStateLoss();
            fm.executePendingTransactions();
        }else if(id == R.id.nav_moocs){
            fm.beginTransaction().replace(R.id.content_hib,new moocs()).commitAllowingStateLoss();
            fm.executePendingTransactions();
        }else if(id == R.id.nav_comp){
            fm.beginTransaction().replace(R.id.content_hib,new complaints()).commitAllowingStateLoss();
            fm.executePendingTransactions();
        }else if(id == R.id.nav_con){
            fm.beginTransaction().replace(R.id.content_hib,new mypage()).commitAllowingStateLoss();
            fm.executePendingTransactions();
        }
        else if(id == R.id.nav_wn){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Dialog dialog = new Dialog(HibiscusActivity.this);
                    dialog.setTitle("What's new");
                    dialog.setContentView(R.layout.activity_whatsnew);
                    dialog.show();
                }
            },200);
        }
        else if (id == R.id.nav_help) {
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        checkupdatestatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
