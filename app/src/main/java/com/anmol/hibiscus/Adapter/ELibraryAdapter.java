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

import com.anmol.hibiscus.Model.ELibrary;

import com.anmol.hibiscus.R;

import java.util.List;

/**
 * Created by anmol on 2017-08-14.
 */

public class ELibraryAdapter extends ArrayAdapter<ELibrary> {
    private Activity context;
    private int resource;
    private List<ELibrary> eLibraries;

    public ELibraryAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<ELibrary> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        eLibraries = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource,null);

        TextView text = (TextView)v.findViewById(R.id.title);
        text.setText(eLibraries.get(position).getTitle());
        TextView author = (TextView)v.findViewById(R.id.author);
        author.setText(eLibraries.get(position).getAuthor());
        TextView publish = (TextView)v.findViewById(R.id.publish);
        publish.setText(eLibraries.get(position).getPubliser());
        TextView year = (TextView)v.findViewById(R.id.year);
        year.setText(eLibraries.get(position).getYear());
        TextView id = (TextView)v.findViewById(R.id.id);
        id.setText(eLibraries.get(position).getId());


        return v;
    }
}
