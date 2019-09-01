package com.anmol.hibiscus.Adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anmol.hibiscus.Model.Library;
import com.anmol.hibiscus.R;

import java.util.List;

/**
 * Created by anmol on 2017-08-14.
 */

public class LibraryAdapter extends ArrayAdapter<Library> {
    private Activity context;
    private int resource;
    private List<Library> libraries;

    public LibraryAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Library> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        libraries = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource,null);

        TextView text = (TextView)v.findViewById(R.id.title);
        text.setText(libraries.get(position).getTitle());
        TextView author = (TextView)v.findViewById(R.id.author);
        author.setText(libraries.get(position).getAuthor());
        TextView publish = (TextView)v.findViewById(R.id.publish);
        publish.setText(libraries.get(position).getPubliser());
        TextView edit = (TextView)v.findViewById(R.id.edition);
        edit.setText(libraries.get(position).getEdition());
        TextView year = (TextView)v.findViewById(R.id.year);
        year.setText(libraries.get(position).getYear());
        TextView status = (TextView)v.findViewById(R.id.status);
        if(libraries.get(position).getStatus().contains("A")){
            status.setText("Available");
            status.setTextColor(context.getResources().getColor(R.color.available));
        }
        else {
            status.setText("Issued");
            status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        TextView id = (TextView)v.findViewById(R.id.id);
        id.setText(libraries.get(position).getId());


        return v;
    }
}
