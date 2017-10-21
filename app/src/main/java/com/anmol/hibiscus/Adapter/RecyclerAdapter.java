package com.anmol.hibiscus.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anmol.hibiscus.Model.Notice;
import com.anmol.hibiscus.R;

import java.util.ArrayList;

/**
 * Created by anmol on 2017-08-15.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    ArrayList<Notice> noticeArrayList = new ArrayList<>();

    public RecyclerAdapter(ArrayList<Notice> noticeArrayList) {
        this.noticeArrayList = noticeArrayList;
    }

    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MyViewHolder holder, int position) {
        holder.title.setText(noticeArrayList.get(position).getTitle());
        holder.date.setText(noticeArrayList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return noticeArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,date;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            date = (TextView)itemView.findViewById(R.id.date);
        }
    }
}
