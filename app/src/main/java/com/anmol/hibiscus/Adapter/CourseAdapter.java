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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anmol.hibiscus.Model.Mycourse;
import com.anmol.hibiscus.R;

import java.util.List;

/**
 * Created by anmol on 2017-08-14.
 */

public class CourseAdapter extends ArrayAdapter<Mycourse> {
    private Activity context;
    private int resource;
    private List<Mycourse> mycourses;

    public CourseAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Mycourse> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        mycourses = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource,null);

        TextView text = (TextView)v.findViewById(R.id.subject);
        text.setText(mycourses.get(position).getName());
        TextView credits = (TextView)v.findViewById(R.id.credits);
        credits.setText(mycourses.get(position).getCredits());
        TextView postedby = (TextView)v.findViewById(R.id.prof);
        postedby.setText(mycourses.get(position).getProfessor());
        TextView id = (TextView)v.findViewById(R.id.id);
        id.setText(mycourses.get(position).getId());



        return v;
    }
}
