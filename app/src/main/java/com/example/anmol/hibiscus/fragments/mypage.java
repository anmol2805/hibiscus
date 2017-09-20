package com.example.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.anmol.hibiscus.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anmol on 9/18/2017.
 */

public class mypage extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.mypage,container,false);
        CircleImageView anmol = (CircleImageView)vi.findViewById(R.id.anmol);
        CircleImageView ankit = (CircleImageView)vi.findViewById(R.id.ankit);
        Glide.with(getActivity()).load(R.drawable.anmol).into(anmol);
        Glide.with(getActivity()).load(R.drawable.ankit).into(ankit);
        Button ganmol = (Button)vi.findViewById(R.id.ganmol);

        Button gitanmol = (Button)vi.findViewById(R.id.gitanmol);
        Button gitankit = (Button)vi.findViewById(R.id.gitankit);
        Button lanmol = (Button)vi.findViewById(R.id.lanmol);
        Button lankit = (Button)vi.findViewById(R.id.lankit);
        ganmol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("mailto:"+"hibioncloud@gmail.com"));
                getActivity().startActivity(viewIntent);
            }
        });

        gitanmol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://github.com/anmol2805"));
                getActivity().startActivity(viewIntent);
            }
        });
        gitankit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://github.com/ankit16-19"));
                getActivity().startActivity(viewIntent);
            }
        });
        lanmol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.linkedin.com/in/anmol-saxena-940ab4140/"));
                getActivity().startActivity(viewIntent);
            }
        });
        lankit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.linkedin.com/in/ankit-choudhary-60b33314b/"));
                getActivity().startActivity(viewIntent);
            }
        });
        return vi;
    }
}
