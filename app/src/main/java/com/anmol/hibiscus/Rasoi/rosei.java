package com.anmol.hibiscus.Rasoi;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;


import com.anmol.hibiscus.R;
import com.anmol.hibiscus.Rasoi.Adapter.GridAdapter;
import com.anmol.hibiscus.Rasoi.Adapter.ViewpageAdapter;
import com.anmol.hibiscus.Rasoi.Model.Coupon;
import com.anmol.hibiscus.Rasoi.Model.MessStatus;
import com.anmol.hibiscus.Rasoi.Services.MessStatusService;
import com.anmol.hibiscus.Rasoi.Services.MessStatusService2;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class rosei extends Fragment {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("students").child(auth.getCurrentUser().getUid()).child("rosei");
    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("students").child(auth.getCurrentUser().getUid()).child("mess1");
    DatabaseReference updb = FirebaseDatabase.getInstance().getReference().child("students").child(auth.getCurrentUser().getUid()).child("UpcomingWeek");
    DatabaseReference predb = FirebaseDatabase.getInstance().getReference().child("students").child(auth.getCurrentUser().getUid()).child("PresentWeek");
    Animation rotate;
    Button book;


    Button logout;
    private static long back_pressed;
    List<MessStatus> messStatuses = new ArrayList<>();
    RecyclerView gridview;
    GridAdapter gridAdapter;
    ViewPager viewPager;
    List<Coupon> coupons = new ArrayList<>();
    ViewpageAdapter viewpageAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.activity_rosei,container,false);
        getActivity().setTitle("IIIT Rasoi");
        book = (Button)vi.findViewById(R.id.book);


        logout = (Button)vi.findViewById(R.id.logout);
        viewPager = (ViewPager)vi.findViewById(R.id.viewpager);
        gridview = (RecyclerView)vi.findViewById(R.id.gridrecycler);
        gridview.setHasFixedSize(true);
        gridview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        // booking activity
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),Book_Activity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Logout");
                builder1.setMessage("Are you sure you want to logout?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Logout",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getActivity(),LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_down);

                            }
                        });

                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("sid").getValue(String.class)!=null){
                    String sid = dataSnapshot.child("sid").getValue(String.class);
                    sid = sid.toUpperCase();
                    stuid.setText(sid);
                    String url = "https://hib.iiit-bh.ac.in/Hibiscus/docs/iiit/Photos/"+sid+".jpg";
                    Glide.with(getActivity()).load(url).into(user);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        final String cdate = dateFormat.format(cal.getTime());
        DateFormat tmeFormat = new SimpleDateFormat("HH:mm");
        final String ctime = tmeFormat.format(cal.getTime());
        final String hr = String.valueOf(ctime.charAt(0))+String.valueOf(ctime.charAt(1));
        final int hour = Integer.parseInt(hr);
        String min = String.valueOf(ctime.charAt(3))+String.valueOf(ctime.charAt(4));
        int mn = Integer.parseInt(min);

//        db.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot data:dataSnapshot.getChildren()){
//                    if(data.child("date").getValue(String.class)!=null){
//                        String date = data.child("date").getValue(String.class);
//                        if(date.contains(cdate)){
//                            if(hour<9){
//                                String mess = data.child("bs").getValue(String.class);
//                                if(!mess.contains("NotIssued")){
//                                    String menu = data.child("brkfast").getValue(String.class);
//                                }else{
//
//                                }
//                            }
//                            else if(hour>9 && hour<14){
//                                String mess = data.child("ls").getValue(String.class);
//                                if(!mess.contains("NotIssued")){
//                                    String menu = data.child("lnch").getValue(String.class);
//                                }else{
//
//                                }
//                            }
//                            else if(hour>14 && hour<22){
//                                String mess = data.child("ds").getValue(String.class);
//                                if(!mess.contains("NotIssued")){
//                                    String menu = data.child("dinnr").getValue(String.class);
//                                }else{
//
//                                }
//                            }
//                        }
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        final Date d = new Date();
        final String dayOfTheWeek = sdf.format(d);
        if(dayOfTheWeek.contains("Sunday")){

        }
        Intent i = new Intent(getActivity(), MessStatusService.class);
        startService(i);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i2 = new Intent(getActivity(), MessStatusService2.class);
                startService(i2);
            }
        },1000);
        updb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.child(String.valueOf("0"))!=null && dataSnapshot.child(String.valueOf("0")).child("date").getValue(String.class)!=null){
                    String date = dataSnapshot.child(String.valueOf("0")).child("date").getValue(String.class);
                    date = date.substring(0,10);
                    System.out.println("coupondate:" + date);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date changedate = simpleDateFormat.parse(date);
                        System.out.println("coupondate:" + changedate);
                        Date onedaybefore = new Date(changedate.getTime() - 2);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String stringodf = sdf.format(onedaybefore);
                        stringodf = stringodf.substring(0,10);
                        stringodf = stringodf + " 21:45:00";
                        onedaybefore = sdf.parse(stringodf);
                        System.out.println("onedaybefore:" + onedaybefore);
                        Date currenttime = Calendar.getInstance().getTime();
                        if (currenttime.after(onedaybefore)){
                            System.out.println("swap database");
                            for(int i = 0;i<7;i++){
                                movePres(updb.child(String.valueOf(i)),predb.child(String.valueOf(i)));
                                updb.child(String.valueOf(i)).removeValue();
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    System.out.println("document is null");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        predb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    messStatuses.clear();
                    coupons.clear();
                    for(int i = 0;i<7;i++){
                        if(dataSnapshot.child(String.valueOf(i))!=null)  {
                            String bs = dataSnapshot.child(String.valueOf(i)).child("bs").getValue(String.class);
                            String ls = dataSnapshot.child(String.valueOf(i)).child("ls").getValue(String.class);
                            String ds = dataSnapshot.child(String.valueOf(i)).child("ds").getValue(String.class);
                            String date = dataSnapshot.child(String.valueOf(i)).child("date").getValue(String.class);
                            String day = date.substring(11,14);
                            MessStatus messStatus = new MessStatus(bs,ls,ds,date,day);
                            messStatuses.add(messStatus);
                            String daydate = date.substring(0,10);

                            String breakfastdate = daydate + " 09:45:00";
                            String lunchdate = daydate + " 14:45:00";
                            String dinnerdate = daydate + " 21:45:00";
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date bfdate = sdf.parse(breakfastdate);
                                Date lnchdate = sdf.parse(lunchdate);
                                Date dindate = sdf.parse(dinnerdate);
                                Date todaydate = Calendar.getInstance().getTime();
                                String bmess = "notissued";
                                String lmess = "notissued";
                                String dmess = "notissued";
                                if(bs.contains("1")){
                                    bmess = "Ground floor Mess";
                                }
                                else if(bs.contains("2")){
                                    bmess = "First floor Mess";
                                }
                                if(ls.contains("1")){
                                    lmess = "Ground floor Mess";
                                }
                                else if(ls.contains("2")){
                                    lmess = "First floor Mess";
                                }
                                if(ds.contains("1")){
                                    dmess = "Ground floor Mess";
                                }
                                else if(ds.contains("2")){
                                    dmess = "First floor Mess";
                                }
                                Coupon bfcoupon = new Coupon("Breakfast",bmess,day,daydate);
                                Coupon lnccoupon = new Coupon("Lunch",lmess,day,daydate);
                                Coupon dincoupon = new Coupon("Dinner",dmess,day,daydate);
                                if(todaydate.before(bfdate)){
                                    if(!bmess.contains("notissued")){
                                        coupons.add(bfcoupon);
                                    }
                                    if(!lmess.contains("notissued")){
                                        coupons.add(lnccoupon);
                                    }
                                    if(!dmess.contains("notissued")){
                                        coupons.add(dincoupon);
                                    }

                                }
                                else if(todaydate.after(bfdate) && todaydate.before(lnchdate)){
                                    if(!lmess.contains("notissued")){
                                        coupons.add(lnccoupon);
                                    }
                                    if(!dmess.contains("notissued")){
                                        coupons.add(dincoupon);
                                    }


                                }
                                else if(todaydate.after(lnchdate) && todaydate.before(dindate)){
                                    if(!dmess.contains("notissued")){
                                        coupons.add(dincoupon);
                                    }

                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (!messStatuses.isEmpty()){
                        gridAdapter = new GridAdapter(getActivity(),messStatuses);
                        gridview.setAdapter(gridAdapter);
                    }
                    if(!coupons.isEmpty()){
                        viewpageAdapter = new ViewpageAdapter(getActivity(),coupons);
                        viewpageAdapter.notifyDataSetChanged();
                        viewPager.setAdapter(viewpageAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return vi;
    }
    private void movePres(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            System.out.println("Copy failed");
                        } else {
                            System.out.println("Success");

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
