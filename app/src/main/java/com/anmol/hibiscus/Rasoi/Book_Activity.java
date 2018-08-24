package com.anmol.hibiscus.Rasoi;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.anmol.hibiscus.R;
import com.anmol.hibiscus.Rasoi.Adapter.Mess1Adapter;
import com.anmol.hibiscus.Rasoi.Adapter.Mess2Adapter;
import com.anmol.hibiscus.Rasoi.Fragments.first;
import com.anmol.hibiscus.Rasoi.Fragments.ground;
import com.anmol.hibiscus.Rasoi.Model.mess1;
import com.anmol.hibiscus.Rasoi.Model.mess2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Book_Activity extends AppCompatActivity {
    Button m1,m2;

    List<mess1> mess1s = new ArrayList<>();
    List<mess2> mess2s = new ArrayList<>();
    Mess1Adapter mess1Adapter;
    Mess2Adapter mess2Adapter;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("students").child(auth.getCurrentUser().getUid());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        m1 = (Button)findViewById(R.id.m1);
        m2 = (Button)findViewById(R.id.m2);


        m1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.large));
        m2.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.small));
        m2.setBackgroundColor(getResources().getColor(R.color.trans));
        m2.setTextColor(getResources().getColor(R.color.colorPrimary));
        m1.setTextColor(getResources().getColor(R.color.white));
        m1.setBackground(getResources().getDrawable(R.drawable.round_button));
//        Intent intent = new Intent(Book_Activity.this, MessStatusService.class);
//        startService(intent);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content,new ground()).commit();


        m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.large));
                m2.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.small));
                m2.setBackgroundColor(getResources().getColor(R.color.trans));
                m2.setTextColor(getResources().getColor(R.color.colorPrimary));
                m1.setTextColor(getResources().getColor(R.color.white));
                m1.setBackground(getResources().getDrawable(R.drawable.round_button));
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.content,new ground()).commit();


            }
        });
        m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.small));
                m2.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.large));
                m1.setBackgroundColor(getResources().getColor(R.color.trans));
                m1.setTextColor(getResources().getColor(R.color.colorPrimary));
                m2.setTextColor(getResources().getColor(R.color.white));
                m2.setBackground(getResources().getDrawable(R.drawable.round_button));
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.content,new first()).commit();

            }
        });



    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
