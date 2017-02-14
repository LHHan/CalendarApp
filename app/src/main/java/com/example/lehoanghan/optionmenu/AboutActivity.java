package com.example.lehoanghan.optionmenu;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lehoanghan.appcalendar.BuildConfig;
import com.example.lehoanghan.appcalendar.R;

/**
 * Created by lehoanghan on 3/30/2016.
 */
public class AboutActivity extends Fragment {

    Activity root;
    public AboutActivity(){}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root=getActivity();
        View contentView=inflater.inflate(R.layout.activity_about,container,false);
        return contentView;
    }

}
