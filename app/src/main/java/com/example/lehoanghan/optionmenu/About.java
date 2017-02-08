package com.example.lehoanghan.optionmenu;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lehoanghan.appcalendar.R;

/**
 * Created by lehoanghan on 3/30/2016.
 */
public class About extends Fragment {

    Activity root;

    public About(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root=getActivity();
        View jview=inflater.inflate(R.layout.screen_about,container,false);
        return jview;
    }

}
