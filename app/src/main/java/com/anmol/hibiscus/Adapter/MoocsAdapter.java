package com.anmol.hibiscus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.anmol.hibiscus.Model.Moocs;
import com.anmol.hibiscus.R;

import java.util.List;

/**
 * Created by anmol on 2017-08-14.
 */

public class MoocsAdapter extends ArrayAdapter<Moocs> {
    private Activity context;
    private int resource;
    private List<Moocs> moocses;

    public MoocsAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Moocs> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        moocses = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource,null);

        TextView text = (TextView)v.findViewById(R.id.title);
        text.setText(moocses.get(position).getName());
        Button take = (Button)v.findViewById(R.id.enter);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(moocses.get(position).getLink()));
                context.startActivity(viewIntent);
            }
        });

        return v;
    }
}
