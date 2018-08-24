package com.anmol.hibiscus.Rasoi.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.anmol.hibiscus.R;
import com.anmol.hibiscus.Rasoi.Model.MessStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder> {
    Context c;
    List<MessStatus> messStatuses;

    public GridAdapter(Context c, List<MessStatus> messStatuses) {
        this.c = c;
        this.messStatuses = messStatuses;
    }

    @Override
    public GridAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.gridlayout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final GridAdapter.MyViewHolder holder, int position) {
        holder.day.setText(messStatuses.get(position).getDay());
        String date = messStatuses.get(position).getcoupondate();
        date = date.substring(0,10);
        String breakfastdate = date + " 09:45:00";
        String lunchdate = date + " 14:45:00";
        String dinnerdate = date + " 21:45:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date bdate = sdf.parse(breakfastdate);
            Date ldate = sdf.parse(lunchdate);
            Date ddate = sdf.parse(dinnerdate);
            Date currentdate = Calendar.getInstance().getTime();
            if(!messStatuses.get(position).getBreakfast().contains("NotIssued")){
                if(messStatuses.get(position).getBreakfast().contains("1")){
                    holder.bst.setText("1");
                    if(messStatuses.get(position).getBreakfast().contains("N")){
                        if (currentdate.before(bdate))
                            holder.bs.setCardBackgroundColor(c.getResources().getColor(R.color.nonveg));
                        else
                            holder.bs.setCardBackgroundColor(c.getResources().getColor(R.color.inactivered));
                    }
                    else if(messStatuses.get(position).getBreakfast().contains("V")){
                        if (currentdate.before(bdate))
                        holder.bs.setCardBackgroundColor(c.getResources().getColor(R.color.veg));
                        else
                            holder.bs.setCardBackgroundColor(c.getResources().getColor(R.color.inactivegreen));
                    }
                }
                else if(messStatuses.get(position).getBreakfast().contains("2")){
                    holder.bst.setText("2");
                    if(messStatuses.get(position).getBreakfast().contains("N")){
                        if (currentdate.before(bdate))
                        holder.bs.setCardBackgroundColor(c.getResources().getColor(R.color.nonveg));
                        else
                            holder.bs.setCardBackgroundColor(c.getResources().getColor(R.color.inactivered));
                    }
                    else if(messStatuses.get(position).getBreakfast().contains("V")){
                        if (currentdate.before(bdate))
                        holder.bs.setCardBackgroundColor(c.getResources().getColor(R.color.veg));
                        else
                            holder.bs.setCardBackgroundColor(c.getResources().getColor(R.color.inactivegreen));
                    }
                }
            }
            else{
                holder.bst.setText("");
                holder.bs.setCardBackgroundColor(c.getResources().getColor(R.color.dull));
            }
            if(!messStatuses.get(position).getLunch().contains("NotIssued")){
                if(messStatuses.get(position).getLunch().contains("1")){
                    holder.lst.setText("1");
                    if(messStatuses.get(position).getLunch().contains("N")){
                        if (currentdate.before(ldate))
                        holder.ls.setCardBackgroundColor(c.getResources().getColor(R.color.nonveg));
                        else
                            holder.ls.setCardBackgroundColor(c.getResources().getColor(R.color.inactivered));

                    }
                    else if(messStatuses.get(position).getLunch().contains("V")){
                        if (currentdate.before(ldate))
                        holder.ls.setCardBackgroundColor(c.getResources().getColor(R.color.veg));
                        else
                            holder.ls.setCardBackgroundColor(c.getResources().getColor(R.color.inactivegreen));
                    }
                }
                else if(messStatuses.get(position).getLunch().contains("2")){
                    holder.lst.setText("2");
                    if(messStatuses.get(position).getLunch().contains("N")){
                        if (currentdate.before(ldate))
                        holder.ls.setCardBackgroundColor(c.getResources().getColor(R.color.nonveg));
                        else
                            holder.ls.setCardBackgroundColor(c.getResources().getColor(R.color.inactivered));
                    }
                    else if(messStatuses.get(position).getLunch().contains("V")){
                        if (currentdate.before(ldate))
                        holder.ls.setCardBackgroundColor(c.getResources().getColor(R.color.veg));
                        else
                            holder.ls.setCardBackgroundColor(c.getResources().getColor(R.color.inactivegreen));
                    }
                }
            }
            else{
                holder.lst.setText("");
                holder.ls.setCardBackgroundColor(c.getResources().getColor(R.color.dull));
            }
            if(!messStatuses.get(position).getDinner().contains("NotIssued")){
                if(messStatuses.get(position).getDinner().contains("1")){
                    holder.dst.setText("1");
                    if(messStatuses.get(position).getDinner().contains("N")){
                        if (currentdate.before(ddate))
                        holder.ds.setCardBackgroundColor(c.getResources().getColor(R.color.nonveg));
                        else
                            holder.ds.setCardBackgroundColor(c.getResources().getColor(R.color.inactivered));
                    }
                    else if(messStatuses.get(position).getDinner().contains("V")){
                        if (currentdate.before(ddate))
                        holder.ds.setCardBackgroundColor(c.getResources().getColor(R.color.veg));
                        else
                            holder.ds.setCardBackgroundColor(c.getResources().getColor(R.color.inactivegreen));
                    }
                }
                else if(messStatuses.get(position).getDinner().contains("2")){
                    holder.dst.setText("2");
                    if(messStatuses.get(position).getDinner().contains("N")){
                        if (currentdate.before(ddate))
                        holder.ds.setCardBackgroundColor(c.getResources().getColor(R.color.nonveg));
                        else
                            holder.ds.setCardBackgroundColor(c.getResources().getColor(R.color.inactivered));
                    }
                    else if(messStatuses.get(position).getDinner().contains("V")){
                        if (currentdate.before(ddate))
                        holder.ds.setCardBackgroundColor(c.getResources().getColor(R.color.veg));
                        else
                            holder.ds.setCardBackgroundColor(c.getResources().getColor(R.color.inactivegreen));
                    }
                }
            }
            else{
                holder.dst.setText("");
                holder.ds.setCardBackgroundColor(c.getResources().getColor(R.color.dull));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return messStatuses.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        CardView bs,ls,ds;
        TextView bst,lst,dst,day;
        public MyViewHolder(View itemView) {
            super(itemView);
            day = (TextView)itemView.findViewById(R.id.day);
            bs = (CardView) itemView.findViewById(R.id.bs);
            ls = (CardView) itemView.findViewById(R.id.ls);
            ds = (CardView)itemView.findViewById(R.id.ds);
            bst = (TextView) itemView.findViewById(R.id.bst);
            lst = (TextView) itemView.findViewById(R.id.lst);
            dst = (TextView) itemView.findViewById(R.id.dst);
        }


    }
}

