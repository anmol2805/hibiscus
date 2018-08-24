package com.anmol.hibiscus.Rasoi.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anmol.rosei.Model.Coupon;
import com.anmol.rosei.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ViewpageAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Coupon> coupons;

    public ViewpageAdapter(Context context, List<Coupon> coupons) {
        this.context = context;
        this.coupons = coupons;
    }

    @Override
    public int getCount() {
        return coupons.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = layoutInflater.inflate(R.layout.menulayout,null);
        TextView meal = (TextView)vi.findViewById(R.id.meal);
        TextView mess = (TextView)vi.findViewById(R.id.mess);
        TextView day = (TextView)vi.findViewById(R.id.day);
        TextView date = (TextView)vi.findViewById(R.id.date);
        final TextView item = (TextView)vi.findViewById(R.id.item);
        meal.setText(coupons.get(position).getMeal());
        date.setText(coupons.get(position).getDate());
        mess.setText(coupons.get(position).getMess());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String index = "0";
        String dayname = "Monday";
        if(coupons.get(position).getDay().contains("Mon")){
            dayname = "Monday";
            index = "0";
        }
        else if(coupons.get(position).getDay().contains("Tue")){
            dayname = "Tuesday";
            index = "1";
        }
        else if(coupons.get(position).getDay().contains("Wed")){
            dayname = "Wednesday";
            index = "2";
        }
        else if(coupons.get(position).getDay().contains("Thu")){
            dayname = "Thursday";
            index = "3";
        }
        else if(coupons.get(position).getDay().contains("Fri")){
            dayname = "Friday";
            index = "4";
        }
        else if(coupons.get(position).getDay().contains("Sat")){
            dayname = "Saturday";
            index = "5";
        }
        else if(coupons.get(position).getDay().contains("Sun")){
            dayname = "Sunday";
            index = "6";
        }
        day.setText(dayname);
        if(coupons.get(position).getMess().contains("Ground")){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("students").child(auth.getCurrentUser().getUid()).child("messStatus").child("mess1");
            db.child(index).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                 if (dataSnapshot!=null){
                                     if (coupons.get(position).getMeal().contains("Breakfast")){
                                         item.setText(dataSnapshot.child("brkfast").getValue(String.class));
                                     }
                                     else if(coupons.get(position).getMeal().contains("Lunch")){
                                         item.setText(dataSnapshot.child("lnch").getValue(String.class));
                                     }
                                     else if(coupons.get(position).getMeal().contains("Dinner")){
                                         item.setText(dataSnapshot.child("dinnr").getValue(String.class));
                                     }
                                 }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
        else if(coupons.get(position).getMess().contains("First")){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("students").child(auth.getCurrentUser().getUid()).child("messStatus").child("mess2");
            db.child(index).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot!=null){
                        if (coupons.get(position).getMeal().contains("Breakfast")){
                            item.setText(dataSnapshot.child("brkfast").getValue(String.class));
                        }
                        else if(coupons.get(position).getMeal().contains("Lunch")){
                            item.setText(dataSnapshot.child("lnch").getValue(String.class));
                        }
                        else if(coupons.get(position).getMeal().contains("Dinner")){
                            item.setText(dataSnapshot.child("dinnr").getValue(String.class));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        ViewPager viewPager = (ViewPager)container;
        viewPager.addView(vi,0);
        return vi;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager)container;
        View view = (View)object;
        viewPager.removeView(view);
    }
}
