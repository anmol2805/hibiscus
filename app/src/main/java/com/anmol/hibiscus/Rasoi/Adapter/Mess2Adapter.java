package com.anmol.hibiscus.Rasoi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by anmol on 10/19/2017.
 */

public class Mess2Adapter extends ArrayAdapter<mess2> {
    private Activity context;
    private int resource;
    private List<mess2> mess2s;
    JSONObject jsonObject2 = new JSONObject();

    public Mess2Adapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<mess2> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        mess2s = objects;
    }
    public int getViewTypeCount() {
        return getCount();
    }

    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView!=null){
            return convertView;
        }
        else {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(resource,null);
            TextView foodb = (TextView)v.findViewById(R.id.foodb);
            TextView foodl = (TextView)v.findViewById(R.id.foodl);
            TextView foodd = (TextView)v.findViewById(R.id.foodd);
            TextView day = (TextView)v.findViewById(R.id.day);
            TextView bstatus = (TextView)v.findViewById(R.id.bstatus);
            TextView lstatus = (TextView)v.findViewById(R.id.lstatus);
            TextView dstatus = (TextView)v.findViewById(R.id.dstatus);
            foodb.setText(mess2s.get(position).getBrkfast());
            foodl.setText(mess2s.get(position).getLnch());
            foodd.setText(mess2s.get(position).getDinnr());
            final String weekday = mess2s.get(position).getDay();
            day.setText(weekday);
            final String couponcode = "m002" + weekday;
            final CheckBox b = (CheckBox)v.findViewById(R.id.b);
            final CheckBox l = (CheckBox)v.findViewById(R.id.l);
            final CheckBox d = (CheckBox)v.findViewById(R.id.d);
            final RadioButton bnv = (RadioButton)v.findViewById(R.id.bnv);
            final RadioButton bv = (RadioButton)v.findViewById(R.id.bv);
            final RadioButton lnv = (RadioButton)v.findViewById(R.id.lnv);
            final RadioButton lv = (RadioButton)v.findViewById(R.id.lv);
            final RadioButton dnv = (RadioButton)v.findViewById(R.id.dnv);
            final RadioButton dv = (RadioButton)v.findViewById(R.id.dv);
            bv.setChecked(true);
            lv.setChecked(true);
            dv.setChecked(true);
            final String couponday = weekday.substring(weekday.length() - 3).toLowerCase();
            System.out.println("couponday:" + couponday);
            try {
                jsonObject2.put(couponday + "bfmt","veg");
                jsonObject2.put(couponday + "lunmt","veg");
                jsonObject2.put(couponday + "dinmt","veg");
                jsonObject2.put(couponday + "bf",1);
                jsonObject2.put(couponday + "lun",1);
                jsonObject2.put(couponday + "din",1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            bnv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isChecked()){
                        bv.setChecked(false);
                        try {
                            jsonObject2.remove(couponday + "bfmt");
                            jsonObject2.put(couponday + "bfmt","nonveg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            bv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isChecked()){
                        bnv.setChecked(false);
                        try {
                            jsonObject2.remove(couponday + "bfmt");
                            jsonObject2.put(couponday + "bfmt","veg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            lnv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isChecked()){
                        lv.setChecked(false);
                        try {
                            jsonObject2.remove(couponday + "lunmt");
                            jsonObject2.put(couponday + "lunmt","nonveg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            lv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isChecked()){
                        lnv.setChecked(false);
                        try {
                            jsonObject2.remove(couponday + "lunmt");
                            jsonObject2.put(couponday + "lunmt","veg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            dnv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isChecked()){
                        dv.setChecked(false);
                        try {
                            jsonObject2.remove(couponday + "dinmt");
                            jsonObject2.put(couponday + "dinmt","nonveg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            dv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isChecked()){
                        dnv.setChecked(false);
                        try {
                            jsonObject2.remove(couponday + "dinmt");
                            jsonObject2.put(couponday + "dinmt","veg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isChecked()){
                        bnv.setVisibility(View.VISIBLE);
                        bv.setVisibility(View.VISIBLE);
                        try {
                            jsonObject2.remove(couponday + "bfmt");
                            jsonObject2.remove(couponday + "bf");
                            jsonObject2.put(couponday + "bfmt","veg");
                            jsonObject2.put(couponday + "bf",couponcode);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(!compoundButton.isChecked()){
                        bnv.setVisibility(View.INVISIBLE);
                        bv.setVisibility(View.INVISIBLE);
                        try {
                            jsonObject2.remove(couponday + "bfmt");
                            jsonObject2.remove(couponday + "bf");
                            jsonObject2.put(couponday + "bfmt","veg");
                            jsonObject2.put(couponday + "bf",1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            l.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isChecked()){
                        lnv.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.VISIBLE);
                        try {
                            jsonObject2.remove(couponday + "lunmt");
                            jsonObject2.remove(couponday + "lun");
                            jsonObject2.put(couponday + "lunmt","veg");
                            jsonObject2.put(couponday + "lun",couponcode);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(!compoundButton.isChecked()){
                        lnv.setVisibility(View.INVISIBLE);
                        lv.setVisibility(View.INVISIBLE);
                        try {
                            jsonObject2.remove(couponday + "lunmt");
                            jsonObject2.remove(couponday + "lun");
                            jsonObject2.put(couponday + "lunmt","veg");
                            jsonObject2.put(couponday + "lun",1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isChecked()){
                        dnv.setVisibility(View.VISIBLE);
                        dv.setVisibility(View.VISIBLE);
                        try {
                            jsonObject2.remove(couponday + "dinmt");
                            jsonObject2.remove(couponday + "din");
                            jsonObject2.put(couponday + "dinmt","veg");
                            jsonObject2.put(couponday + "din",couponcode);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(!compoundButton.isChecked()){
                        dnv.setVisibility(View.INVISIBLE);
                        dv.setVisibility(View.INVISIBLE);
                        try {
                            jsonObject2.remove(couponday + "dinmt");
                            jsonObject2.remove(couponday + "din");
                            jsonObject2.put(couponday + "dinmt","veg");
                            jsonObject2.put(couponday + "din",1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            CardView brk = (CardView)v.findViewById(R.id.brk);
            CardView lnch = (CardView)v.findViewById(R.id.lnch);
            CardView dnnr = (CardView)v.findViewById(R.id.dnnr);
            if(!mess2s.get(position).getBs().contains("NotIssued")){
                b.setVisibility(View.INVISIBLE);
                b.setChecked(false);

                if(mess2s.get(position).getBs().contains("1")){
                    bstatus.setText("Ground Floor");
                    if(mess2s.get(position).getBs().contains("N")){
                        bstatus.setTextColor(getContext().getResources().getColor(R.color.nonveg));
                    }
                    else if(mess2s.get(position).getBs().contains("V")){
                        bstatus.setTextColor(getContext().getResources().getColor(R.color.veg));
                    }
                }
                else if(mess2s.get(position).getBs().contains("2")){
                    bstatus.setText("First Floor");
                    if(mess2s.get(position).getBs().contains("N")){
                        bstatus.setTextColor(getContext().getResources().getColor(R.color.nonveg));
                    }
                    else if(mess2s.get(position).getBs().contains("V")){
                        bstatus.setTextColor(getContext().getResources().getColor(R.color.veg));
                    }
                }
                bstatus.setVisibility(View.VISIBLE);
            }
            else {
                bstatus.setVisibility(View.INVISIBLE);
                b.setVisibility(View.VISIBLE);
                brk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(b.isChecked()){
                            b.setChecked(false);
                        }
                        else if(!b.isChecked()){
                            b.setChecked(true);
                        }
                    }
                });
            }
            if(!mess2s.get(position).getLs().contains("NotIssued")){
                l.setVisibility(View.INVISIBLE);
                l.setChecked(false);

                if(mess2s.get(position).getLs().contains("1")){
                    lstatus.setText("Ground Floor");
                    if(mess2s.get(position).getLs().contains("N")){
                        lstatus.setTextColor(getContext().getResources().getColor(R.color.nonveg));
                    }
                    else if(mess2s.get(position).getLs().contains("V")){
                        lstatus.setTextColor(getContext().getResources().getColor(R.color.veg));
                    }
                }
                else if(mess2s.get(position).getLs().contains("2")){
                    lstatus.setText("First Floor");
                    if(mess2s.get(position).getLs().contains("N")){
                        lstatus.setTextColor(getContext().getResources().getColor(R.color.nonveg));
                    }
                    else if(mess2s.get(position).getLs().contains("V")){
                        lstatus.setTextColor(getContext().getResources().getColor(R.color.veg));
                    }
                }
                lstatus.setVisibility(View.VISIBLE);
            }
            else {
                lstatus.setVisibility(View.INVISIBLE);
                l.setVisibility(View.VISIBLE);
                lnch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(l.isChecked()){
                            l.setChecked(false);
                        }
                        else if(!l.isChecked()){
                            l.setChecked(true);
                        }
                    }
                });

            }
            if(!mess2s.get(position).getDs().contains("NotIssued")){
                d.setVisibility(View.INVISIBLE);
                d.setChecked(false);

                if(mess2s.get(position).getDs().contains("1")){
                    dstatus.setText("Ground Floor");
                    if(mess2s.get(position).getDs().contains("N")){
                        dstatus.setTextColor(getContext().getResources().getColor(R.color.nonveg));
                    }
                    else if(mess2s.get(position).getDs().contains("V")){
                        dstatus.setTextColor(getContext().getResources().getColor(R.color.veg));
                    }
                }
                else if(mess2s.get(position).getDs().contains("2")){
                    dstatus.setText("First Floor");
                    if(mess2s.get(position).getDs().contains("N")){
                        dstatus.setTextColor(getContext().getResources().getColor(R.color.nonveg));
                    }
                    else if(mess2s.get(position).getDs().contains("V")){
                        dstatus.setTextColor(getContext().getResources().getColor(R.color.veg));
                    }
                }
                dstatus.setVisibility(View.VISIBLE);
            }
            else {
                dstatus.setVisibility(View.INVISIBLE);
                d.setVisibility(View.VISIBLE);
                dnnr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(d.isChecked()){
                            d.setChecked(false);
                        }
                        else if(!d.isChecked()){
                            d.setChecked(true);
                        }
                    }
                });
            }


            return v;
        }

    }
    public JSONObject getJsonObject() {
        return jsonObject2;
    }
}
