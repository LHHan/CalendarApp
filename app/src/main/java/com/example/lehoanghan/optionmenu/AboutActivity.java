package com.example.lehoanghan.optionmenu;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lehoanghan.appcalendar.BuildConfig;
import com.example.lehoanghan.appcalendar.R;

/**
 * Created by lehoanghan on 3/30/2016.
 */
public class AboutActivity extends Fragment {

    Activity root;
    private int versionCode = BuildConfig.VERSION_CODE;
    private String versionName = BuildConfig.VERSION_NAME;
    private String buildType = BuildConfig.BUILD_TYPE;
    private String flavor = BuildConfig.FLAVOR;
    private TextView tvVersionCode, tvVersionName, tvBuildType, tvFlavor;

    public AboutActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = getActivity();
        View contentView = inflater.inflate(R.layout.activity_about, container, false);
        tvVersionName = (TextView) contentView.findViewById(R.id.activity_about_tv_version_name_value);
        tvVersionCode = (TextView) contentView.findViewById(R.id.activity_about_tv_version_code_value);
        tvBuildType = (TextView) contentView.findViewById(R.id.activity_about_tv_build_type_value);
        tvFlavor = (TextView) contentView.findViewById(R.id.activity_about_tv_flavor_value);
        tvVersionName.setText(versionName);
        tvVersionCode.setText(versionCode + "");
        tvBuildType.setText(buildType);
        tvFlavor.setText(flavor);
        return contentView;
    }


}
