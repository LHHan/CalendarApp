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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

/**
 * Created by lehoanghan on 3/30/2016.
 */
@EFragment(R.layout.fragment_about)
public class AboutFragment extends Fragment {
    @ViewById(R.id.fragment_about_tv_version_name_value)
    TextView getTvVersionName;

    @ViewById(R.id.fragment_about_tv_version_code_value)
    TextView getTvVersionCode;

    @ViewById(R.id.fragment_about_tv_build_type)
    TextView getTvBuildType;

    @ViewById(R.id.fragment_about_tv_flavor_value)
    TextView getTvFlavor;

    private Activity activityRoot;

    private int versionCode = BuildConfig.VERSION_CODE;

    private String versionName = BuildConfig.VERSION_NAME;

    private String buildType = BuildConfig.BUILD_TYPE;

    private String strFlavor = BuildConfig.FLAVOR;

    public AboutFragment() {
    }

    @AfterViews
    public void afterView() {
        getTvVersionName.setText(versionName);
        getTvVersionCode.setText(versionCode + "");
        getTvBuildType.setText(buildType);
        getTvFlavor.setText(strFlavor);
    }
}
