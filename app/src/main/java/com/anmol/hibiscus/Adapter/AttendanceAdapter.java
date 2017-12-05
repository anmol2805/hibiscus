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

import com.anmol.hibiscus.Model.Attendance;

import com.anmol.hibiscus.R;

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
        String attstatus = attendances.get(position).getAttend();
        String present = attstatus.substring(0,9);
        String absent = attstatus.substring(10,19);
        String leave = attstatus.substring(20,30);
        String total = attstatus.substring(31,33);

        attendance.setText(attstatus);
        TextView teacher = (TextView)v.findViewById(R.id.teacher);
        teacher.setText(attendances.get(position).getName());
        TextView subcode = (TextView)v.findViewById(R.id.subcode);
        subcode.setText(attendances.get(position).getSubcode());
        return v;
    }
}
