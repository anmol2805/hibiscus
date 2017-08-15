package com.example.anmol.hibiscus;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.anmol.hibiscus.Model.Notice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anmol on 2017-08-14.
 */

public class BackgroundTask {
    Context context;
    ArrayList<Notice> arrayList = new ArrayList<>();
    String url = "http://139.59.23.157/api/hibi/notice";
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    JSONObject jsonObject;
    JSONArray jsonArray1;
    String title;
    public BackgroundTask(Context context) {
        this.context = context;
    }
    public ArrayList<Notice> getList(){

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
        jsonObject = new JSONObject();
        try {
            jsonObject.put("uid","b516008");
            jsonObject.put("pwd","anmol@2805");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                        int c = 0;
                        while (c<response.getJSONArray("Notices").length()){
                            JSONObject object = response.getJSONArray("Notices").getJSONObject(c);
                            //Toast.makeText(context,object.getString("title"),Toast.LENGTH_SHORT).show();
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Notices").child(String.valueOf(c));
                            title = object.getString("title");
                            Notice notice = new Notice(title);
                            db.setValue(notice);
//                            Map<String,Object> map = new HashMap<>();
//                            map.put(String.valueOf(c),object.getString("title"));
//                            db.updateChildren(map);
                            arrayList.add(notice);
                            c++;


                        }







                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
            }
        });
        Mysingleton.getInstance(context).addToRequestqueue(jsonObjectRequest);
        return arrayList;
    }
}
