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

import com.example.anmol.hibiscus.Model.Complains;
import com.example.anmol.hibiscus.R;

import java.util.List;

/**
 * Created by anmol on 2017-08-14.
 */

public class ComplainAdapter extends ArrayAdapter<Complains> {
    private Activity context;
    private int resource;
    private List<Complains> complainses;

    public ComplainAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Complains> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        complainses = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource,null);

        TextView text = (TextView)v.findViewById(R.id.date);
        text.setText(complainses.get(position).getDate());
        TextView credits = (TextView)v.findViewById(R.id.status);
        credits.setText(complainses.get(position).getStatus());
        TextView postedby = (TextView)v.findViewById(R.id.title);
        postedby.setText(complainses.get(position).getTitle());




        return v;
    }
}
