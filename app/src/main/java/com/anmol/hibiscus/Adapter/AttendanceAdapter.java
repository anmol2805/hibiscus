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

import com.anmol.hibiscus.Model.Attendance;

import com.anmol.hibiscus.R;
import com.github.lzyzsd.circleprogress.ArcProgress;

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
    public int getViewTypeCount() {
        return getCount();
    }

    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView!=null){
            return convertView;
        }
        else{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(resource,null);
            TextView submame = (TextView)v.findViewById(R.id.subname);
            TextView attendance = (TextView)v.findViewById(R.id.att);
            TextView tpresent = (TextView)v.findViewById(R.id.present);
            TextView tabsent = (TextView)v.findViewById(R.id.absent);
            TextView tleave = (TextView)v.findViewById(R.id.leave);
            TextView ttotal = (TextView)v.findViewById(R.id.total);
            final ArcProgress arcProgress = (ArcProgress)v.findViewById(R.id.presentp);
            TextView subcode = (TextView)v.findViewById(R.id.subcode);
            TextView teacher = (TextView)v.findViewById(R.id.teacher);
            String attstatus = attendances.get(position).getAttend();
            String present = attstatus.substring(0,3);
            String absent = attstatus.substring(11,14);
            String leave = attstatus.substring(21,24);
            String total = attstatus.substring(31,34);
            tpresent.setText(present);
            tabsent.setText(absent);
            tleave.setText(leave);
            ttotal.setText(total);
            String pp = attstatus.substring(4,7);
            pp = pp.replaceAll("\\s","0");
            final int ppi = Integer.parseInt(pp);

            arcProgress.setProgress(ppi);
            if(ppi>= 75 ){
                arcProgress.setFinishedStrokeColor(context.getResources().getColor(R.color.good));
            }
            else if(ppi >= 65){
                arcProgress.setFinishedStrokeColor(context.getResources().getColor(R.color.warn));
            }
            else {
                arcProgress.setFinishedStrokeColor(context.getResources().getColor(R.color.absent));
            }
            attendance.setText(attstatus);

            teacher.setText(attendances.get(position).getName());
            submame.setText(attendances.get(position).getSub());
            subcode.setText(attendances.get(position).getSubcode());
            return v;
        }

    }
}
