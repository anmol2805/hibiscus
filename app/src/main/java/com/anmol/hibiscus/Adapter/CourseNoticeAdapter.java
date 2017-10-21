package com.anmol.hibiscus.Adapter;

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

import com.anmol.hibiscus.Model.Coursenotice;

import com.anmol.hibiscus.R;

import java.util.List;

/**
 * Created by anmol on 2017-08-14.
 */

public class CourseNoticeAdapter extends ArrayAdapter<Coursenotice> {
    private Activity context;
    private int resource;
    private List<Coursenotice> coursenotices;

    public CourseNoticeAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Coursenotice> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        coursenotices = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource,null);

        TextView text = (TextView)v.findViewById(R.id.title);
        text.setText(coursenotices.get(position).getTitle());
        TextView date = (TextView)v.findViewById(R.id.date);
        date.setText(coursenotices.get(position).getDate());

        return v;
    }
}
