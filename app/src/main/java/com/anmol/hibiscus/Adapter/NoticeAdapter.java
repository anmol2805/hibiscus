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

import com.anmol.hibiscus.Model.Notice;
import com.anmol.hibiscus.R;

import java.util.List;

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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource,null);

        TextView text = (TextView)v.findViewById(R.id.title);
        text.setText(notices.get(position).getTitle());
        TextView date = (TextView)v.findViewById(R.id.date);
        date.setText(notices.get(position).getDate());
        TextView postedby = (TextView)v.findViewById(R.id.posted);
        postedby.setText(notices.get(position).getPosted_by());
        TextView attention = (TextView)v.findViewById(R.id.attention);
        attention.setText(notices.get(position).getAttention());
        return v;
    }
}
