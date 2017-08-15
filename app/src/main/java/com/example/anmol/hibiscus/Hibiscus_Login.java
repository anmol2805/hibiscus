package com.example.anmol.hibiscus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.anmol.hibiscus.Model.Hibdetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class Hibiscus_Login extends AppCompatActivity {

    EditText studentid,password;
    String sid,pwd;
    Button login;
    Boolean status = false;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    JSONObject jsonObject1;
    String url = "http://139.59.23.157/api/hibi/login_test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hibiscus__login);

        login = (Button)findViewById(R.id.hiblogin);
        studentid = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sid = studentid.getText().toString().toLowerCase();
                pwd = password.getText().toString();

                if(auth.getCurrentUser().getEmail().contains(sid)){
                    jsonObject1 = new JSONObject();
                    try {
                        jsonObject1.put("uid",sid);
                        jsonObject1.put("pwd",pwd);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,jsonObject1, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getString("result").equals("success")){
                                    status = true;
                                    Hibdetails hibdetails = new Hibdetails(sid,pwd,status);
                                    databaseReference.setValue(hibdetails);
                                    startActivity(new Intent(Hibiscus_Login.this,HibiscusActivity.class));
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
                    Mysingleton.getInstance(Hibiscus_Login.this).addToRequestqueue(jsonObjectRequest);

                }
                else{
                    studentid.setText("");
                    password.setText("");
                    Toast.makeText(Hibiscus_Login.this,"Please login with your own studentId",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
