package com.example.anmol.hibiscus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.anmol.hibiscus.Model.Noticel;
import com.example.anmol.hibiscus.R;

import java.util.List;

/**
 * Created by anmol on 2017-08-14.
 */

public class NoticeAdapterl extends ArrayAdapter<Noticel> {
    private Activity context;
    private int resource;
    private List<Noticel> noticesl;

    public NoticeAdapterl(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Noticel> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        noticesl = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource,null);

        TextView text = (TextView)v.findViewById(R.id.title);
        text.setText(noticesl.get(position).getTitle());
        TextView date = (TextView)v.findViewById(R.id.date);
        date.setText(noticesl.get(position).getDate());
        TextView postedby = (TextView)v.findViewById(R.id.posted);
        postedby.setText(noticesl.get(position).getPosted_by());
        TextView attention = (TextView)v.findViewById(R.id.attention);
        attention.setText(noticesl.get(position).getAttention());
        return v;
    }
}
