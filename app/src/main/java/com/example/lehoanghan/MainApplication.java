package com.example.lehoanghan;

import android.support.multidex.MultiDexApplication;
import com.example.lehoanghan.appcalendar.R;
import net.danlew.android.joda.JodaTimeAndroid;

import org.androidannotations.annotations.EApplication;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * ->  Created by LeHoangHan on 3/29/2017.
 */
@EApplication
public class MainApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
