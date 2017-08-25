package com.example.anmol.hibiscus;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.bumptech.glide.Glide;
import com.example.anmol.hibiscus.fragments.commapps;
import com.example.anmol.hibiscus.fragments.courseware;
import com.example.anmol.hibiscus.fragments.help;
import com.example.anmol.hibiscus.fragments.main;
import com.example.anmol.hibiscus.fragments.myapps;

public class HibiscusActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hibiscus);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        ImageView imageView = (ImageView)header.findViewById(R.id.dph);
        Glide.with(HibiscusActivity.this).load("https://hib.iiit-bh.ac.in/Hibiscus/docs/iiit/Photos/B516008.jpg").into(imageView);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_hib,new main()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm = getFragmentManager();
        if (id == R.id.nav_hibiscus) {
            fm.beginTransaction().replace(R.id.content_hib,new main()).commit();
        } else if (id == R.id.nav_myapps) {
            fm.beginTransaction().replace(R.id.content_hib,new myapps()).commit();
        } else if (id == R.id.nav_course) {
            fm.beginTransaction().replace(R.id.content_hib,new courseware()).commit();
        } else if (id == R.id.nav_comm) {
            fm.beginTransaction().replace(R.id.content_hib,new commapps()).commit();
        } else if (id == R.id.nav_help) {
            fm.beginTransaction().replace(R.id.content_hib,new help()).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
