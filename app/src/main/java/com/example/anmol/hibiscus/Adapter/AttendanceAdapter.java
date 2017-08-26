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

import com.example.anmol.hibiscus.Model.Attendance;
import com.example.anmol.hibiscus.Model.Notice;
import com.example.anmol.hibiscus.R;

import java.util.List;

/**
 * Created by anmol on 2017-08-26.
 */

public class AttendanceAdapter extends ArrayAdapter<Attendance> {
    private Activity context;
    private int resource;
    private List<Attendance> attendances;

    public AttendanceAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Attendance> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        attendances = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource,null);

        TextView submame = (TextView)v.findViewById(R.id.subname);
        submame.setText(attendances.get(position).getSub());
        TextView attendance = (TextView)v.findViewById(R.id.att);
        attendance.setText(attendances.get(position).getAttend());
        TextView teacher = (TextView)v.findViewById(R.id.teacher);
        teacher.setText(attendances.get(position).getName());
        TextView subcode = (TextView)v.findViewById(R.id.subcode);
        subcode.setText(attendances.get(position).getSubcode());
        return v;
    }
}
