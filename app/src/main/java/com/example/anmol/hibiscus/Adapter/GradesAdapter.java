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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anmol.hibiscus.Model.Mycourse;
import com.example.anmol.hibiscus.R;

import java.util.List;

/**
 * Created by anmol on 2017-08-14.
 */

public class GradesAdapter extends ArrayAdapter<Mycourse> {
    private Activity context;
    private int resource;
    private List<Mycourse> mycourses;

    public GradesAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Mycourse> objects){
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

        LinearLayout l1 = (LinearLayout)v.findViewById(R.id.l1);
        LinearLayout l2 = (LinearLayout)v.findViewById(R.id.l2);
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);
        Button load = (Button)v.findViewById(R.id.pg);
        load.setVisibility(View.VISIBLE);
        ProgressBar lg = (ProgressBar)v.findViewById(R.id.lg);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        


        return v;
    }
}
