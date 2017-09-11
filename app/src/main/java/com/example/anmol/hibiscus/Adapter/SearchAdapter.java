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

import com.example.anmol.hibiscus.Model.ELibrary;
import com.example.anmol.hibiscus.Model.Search;
import com.example.anmol.hibiscus.R;

import java.util.List;

/**
 * Created by anmol on 2017-08-14.
 */

public class SearchAdapter extends ArrayAdapter<Search> {
    private Activity context;
    private int resource;
    private List<Search> searches;

    public SearchAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Search> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        searches = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource,null);
        TextView text = (TextView)v.findViewById(R.id.id);
        text.setText(searches.get(position).getId());
        TextView author = (TextView)v.findViewById(R.id.name);
        author.setText(searches.get(position).getName());
        return v;
    }
}
