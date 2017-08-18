package com.example.anmol.hibiscus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button hibiscusb,rosei,its;
    TextView ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hibiscusb = (Button)findViewById(R.id.hibiscusb);
        rosei = (Button)findViewById(R.id.rosei);
        its = (Button)findViewById(R.id.its);
        ids = (TextView)findViewById(R.id.ids);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sid = dataSnapshot.child("sid").getValue().toString();
                if(sid!=null){
                    ids.setText(sid);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        hibiscusb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(ids.getText().toString())){
                    startActivity(new Intent(MainActivity.this,HibiscusActivity.class));
                }
                else{
                    startActivity(new Intent(MainActivity.this,Hibiscus_Login.class));
                }


            }
        });
        rosei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RoseiActivity.class));
            }
        });
        its.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ItsActivity.class));
            }
        });
    }
}
