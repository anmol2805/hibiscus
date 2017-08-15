package com.example.anmol.hibiscus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button hibiscusb,rosei,its;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hibiscusb = (Button)findViewById(R.id.hibiscusb);
        rosei = (Button)findViewById(R.id.rosei);
        its = (Button)findViewById(R.id.its);
        hibiscusb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Hibiscus_Login.class));
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
