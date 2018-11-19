package com.anmol.hibiscus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anmol.hibiscus.Helpers.Dbbookshelper;
import com.anmol.hibiscus.Helpers.Dbhelper;
import com.anmol.hibiscus.Model.Notice;
import com.anmol.hibiscus.Model.Noticedata;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NoticeDataActivity extends AppCompatActivity {
    String id,uid,pwd,title,date,att,post;
    JSONObject object;
    String url = "http://139.59.23.157/api/hibi/notice_data";
    TextView data;
    Context context;
    WebView nd;
    TextView d,a,p,t;
    ProgressBar pn;
    Button retry;
    ImageView fail;
    Boolean booked;
    Dbbookshelper dbb;
    Dbhelper db;
    MenuItem bookmarksmenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
        setContentView(R.layout.activity_notice_data);
        setTitle("Notices");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        retry = (Button) findViewById(R.id.retry);
        //data = (TextView)findViewById(R.id.data);
        nd = (WebView) findViewById(R.id.nd);
        d = (TextView) findViewById(R.id.date);
        a = (TextView) findViewById(R.id.attention);
        p = (TextView) findViewById(R.id.posted);
        t = (TextView) findViewById(R.id.title);
        pn = (ProgressBar) findViewById(R.id.pn);
        fail = (ImageView) findViewById(R.id.fail);
        dbb = new Dbbookshelper(this);
        db = new Dbhelper(this);



            loadview();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.noticedata, menu);
        bookmarksmenu = menu.findItem(R.id.action_book);
        bookmarksmenu.setIcon(R.drawable.starwhite);
        List<Notice> notices = new ArrayList<>();
//        List<String> data = newfeature ArrayList<String>();
//        data.clear();
//        data = dbb.readbook();
        notices.clear();
        String query = "Select * from notice_table WHERE bookmark="+1;
        notices = db.readData(query);
        int i = 0;
        booked = false;
        while(i<notices.size()){
            if(notices.get(i).getId().equals(id)){
                bookmarksmenu.setIcon(R.drawable.stargolden);
                booked = true;
            }
            i++;
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_book:
                if(!booked){
                    db.updatebooknotice(true,id);
                    bookmarksmenu.setIcon(R.drawable.stargolden);
                }
                else{
                    db.updatebooknotice(false,id);
                    bookmarksmenu.setIcon(R.drawable.starwhite);
                }
                break;
            case R.id.action_share:
                Intent shareintent = new Intent();
                shareintent.setAction(Intent.ACTION_SEND);
                shareintent.setType("text/plain");
                shareintent.putExtra(Intent.EXTRA_TEXT,title + " :\nhttps://canopydevelopers.com/sharednotice/" + id);
                startActivity(Intent.createChooser(shareintent,"Share notice"));
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return true;
    }
    private void loadview() {
        nd.setFocusable(true);
        nd.setFocusableInTouchMode(true);
        nd.getSettings().setJavaScriptEnabled(true);
        nd.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        nd.getSettings().setLoadsImagesAutomatically(true);
        nd.getSettings().setSupportZoom(true);
        nd.getSettings().setBuiltInZoomControls(true);
        nd.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //nd.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        nd.getSettings().setDomStorageEnabled(true);
        nd.getSettings().setDatabaseEnabled(true);
        nd.getSettings().setLoadsImagesAutomatically(true);
        //nd.getSettings().setAppCacheEnabled(true);
        nd.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        nd.getSettings().setUseWideViewPort(true);
        nd.getSettings().setTextZoom(175);
        nd.setInitialScale(1);

        handleIntent(getIntent());


        d.setText(date);
        p.setText(post);
        a.setText(att);
        t.setText(title);
        FirebaseAuth auth =FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("sid").getValue(String.class)!=null && dataSnapshot.child("pwd").getValue(String.class)!=null) {
                        uid = dataSnapshot.child("sid").getValue(String.class);
                        pwd = dataSnapshot.child("pwd").getValue(String.class);
                        object = new JSONObject();
                        try {
                            object.put("uid",uid);
                            object.put("pwd",pwd);
                            object.put("id",id);
                            object.put("pass","encrypt");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        request(object, 0);
                        retry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                request(object, 1);

                            }
                        });
                        pn.setVisibility(View.VISIBLE);
                        FirebaseDatabase.getInstance().getReference().child("NoticeData").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(id)
                                        && !dataSnapshot.child(id).child("notice").getValue(String.class).contains("null")
                                        && dataSnapshot.child(id).child("notice").getValue()!=null
                                        && dataSnapshot.child(id).child("notice").getValue(String.class)!=null
                                        && dataSnapshot.child(id).child("notice")!=null
                                        && dataSnapshot.child(id).child("notice").exists()
                                        ){
                                    System.out.println("notice:" + "firebase");
                                    nd.loadData(dataSnapshot.child(id).child("notice").getValue(String.class), "text/html; charset=utf-8", "UTF-8");
                                    pn.setVisibility(View.GONE);
                                }
                                else {
                                    request(object,1);
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
        else{
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(NoticeDataActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_down);
        }


    }

    private void handleIntent(Intent intent) {
        // ATTENTION: This was auto-generated to handle app links.
        Uri appLinkData = intent.getData();
        if(appLinkData!=null){
            id = appLinkData.getLastPathSegment();
            String query = "Select * from notice_table WHERE notice_id="+id;
            Dbhelper dbhelper = new Dbhelper(this);
            List<Notice> notices = new ArrayList<>();
            notices.clear();
            notices = dbhelper.readData(query);
            try{
                title = notices.get(0).getTitle();
                date = notices.get(0).getDate();
                att = notices.get(0).getAttention();
                post = notices.get(0).getPosted_by();
                System.out.println(notices);
            }
            catch (IndexOutOfBoundsException e){
                e.printStackTrace();
                List<Notice> notices1 = dbhelper.readData("Select * from notice_table ORDER BY notice_id DESC");
                ArrayList<String> noticeids = new ArrayList<>();
                for(int i = 0;i<notices1.size();i++){
                    noticeids.add(notices1.get(i).getId());
                }
                loaddata(noticeids,dbhelper,id);

            }


        }
        else{
            id = intent.getStringExtra("id");
            title = getIntent().getStringExtra("title");
            date = getIntent().getStringExtra("date");
            att = getIntent().getStringExtra("att");
            post = getIntent().getStringExtra("posted");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void request(JSONObject object, final int i) {
        System.out.println(object);
        retry.setVisibility(View.GONE);
        fail.setVisibility(View.GONE);
        pn.setVisibility(View.VISIBLE);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("NoticeData").child(id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.noticedata_url), object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String notice = response.getJSONArray("Notices").getJSONObject(0).getString("notice_data");
                    if(!notice.contains("null") && !notice.isEmpty()){
                        Noticedata noticedata = new Noticedata(notice);
                        databaseReference.setValue(noticedata).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                System.out.println("added to firebase");
                            }
                        });
                        System.out.println("notice:" + "api");
                        nd.loadData(notice, "text/html; charset=utf-8", "UTF-8");
                        pn.setVisibility(View.GONE);
                    }
                    else{
                        if(i == 1){
                            pn.setVisibility(View.GONE);
                            retry.setVisibility(View.VISIBLE);
                            fail.setVisibility(View.VISIBLE);
                        }
                        else{
                            pn.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(i == 1){
                    pn.setVisibility(View.GONE);
                    retry.setVisibility(View.VISIBLE);
                    fail.setVisibility(View.VISIBLE);
                }
                else{
                    pn.setVisibility(View.GONE);
                }

            }
        });
        Mysingleton.getInstance(NoticeDataActivity.this).addToRequestqueue(jsonObjectRequest);
    }
    private void loaddata(final ArrayList<String> noticeids, final Dbhelper dbhelper, final String id) {
        final JSONObject jsonObject = new JSONObject();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();
        if(auth.getCurrentUser()!=null){
            databaseReference.child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("sid").getValue(String.class)!=null && dataSnapshot.child("pwd").getValue(String.class)!=null){
                        uid = dataSnapshot.child("sid").getValue(String.class);
                        pwd = dataSnapshot.child("pwd").getValue(String.class);
                        try {
                            jsonObject.put("uid",uid);
                            jsonObject.put("pwd",pwd);
                            jsonObject.put("pass","encrypt");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.notice_url), jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    int c = 0;
                                    while (c<response.getJSONArray("Notices").length()){

                                        JSONObject object = response.getJSONArray("Notices").getJSONObject(c);


                                        String mtitle = object.getString("title");
                                        String mdate = object.getString("date");
                                        String postedby = object.getString("posted_by");
                                        String attention = object.getString("attention");
                                        String mid = object.getString("id");
                                        Notice notice = new Notice(mtitle,mdate,postedby,attention,mid,false,false);
                                        int k=0;
                                        for(int j = 0;j<noticeids.size();j++){
                                            if(noticeids.get(j).equals(NoticeDataActivity.this.id)){
                                                k=1;
                                            }
                                        }
                                        if(k==0){
                                            System.out.print("noticestatus:newfeature entry");
                                            dbhelper.insertData(notice);
                                        }
                                        else{
                                            System.out.print("noticestatus:already present");
                                        }
                                        dbhelper.updatenotice(notice);
                                        c++;
                                    }
                                    String query = "Select * from notice_table WHERE notice_id="+id;
                                    List<Notice> notices = new ArrayList<>();
                                    notices.clear();
                                    notices = dbhelper.readData(query);
                                    try{
                                        title = notices.get(0).getTitle();
                                        date = notices.get(0).getDate();
                                        att = notices.get(0).getAttention();
                                        post = notices.get(0).getPosted_by();
                                        System.out.println(notices);
                                    }
                                    catch (IndexOutOfBoundsException e){
                                        e.printStackTrace();

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
                        Mysingleton.getInstance(getApplicationContext()).addToRequestqueue(jsonObjectRequest);



                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(NoticeDataActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_down);
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
        //overridePendingTransition(R.anim.still,R.anim.slide_out_down);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
