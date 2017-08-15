package com.example.anmol.hibiscus.Adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.anmol.hibiscus.Model.Notice;
import com.example.anmol.hibiscus.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by anmol on 2017-08-14.
 */

public class NoticeAdapter extends ArrayAdapter<Notice> {
    private Activity context;
    private int resource;
    private List<Notice> notices;

    public NoticeAdapter(@NonNull Activity context,@LayoutRes int resource,@NonNull List<Notice> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        notices = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource,null);
        TextView text = (TextView)v.findViewById(R.id.title);
        text.setText(notices.get(position).getTitle());
        return v;
    }
}
