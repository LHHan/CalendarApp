package com.example.lehoanghan.optionmenu;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.widgets.DayViewDecoratorDotSpan;
import com.firebase.client.Firebase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lehoanghan on 3/16/2016.
 */
@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment {

    private static String sMailUser;

    private static String sNameUser;

    @ViewById(R.id.fragment_home_calendar)
    MaterialCalendarView mcvCalendar;

    @ViewById(R.id.fragment_home_iv_main)
    ImageView ivCover;

    private Fragment aFragment = null;

    private Intent intentSetDate;

    private StringBuilder toDay;

    private Firebase gFirebase;

    private MenuInflater menuInflater;
    private Menu menu;
    private StringBuilder txtDate;

    public HomeFragment() {
    }

    @AfterViews
    void afterView() {
        //using google firebase
        Firebase.setAndroidContext(getActivity());
        gFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        initView();
        setImageCover();
        setCalendar();

    }

    void initView() {
        Bundle bundle = this.getArguments();
        sNameUser = bundle.getString("NameforHome");
        sMailUser = bundle.getString("MailforHome");
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_home, menu);
        getActivity().setTitle("Home");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
//            case R.id.menu_home_ab_add_event:
//                passDataDatetoAddEvent();
//                break;
            case R.id.menu_home_ab_today:
                btnTodayClick();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void btnTodayClick() {
        mcvCalendar.setSelectedDate(Calendar.getInstance());
        mcvCalendar.setFocusable(false);

        mcvCalendar.setCurrentDate(CalendarDay.today());
    }

    void setCalendar() {
        List<CalendarDay> dates = new ArrayList<>();
        dates.add(CalendarDay.today());
        mcvCalendar.addDecorator(new DayViewDecoratorDotSpan(Color.RED, dates));

        mcvCalendar.setSelectedDate(Calendar.getInstance());
        Calendar cal = Calendar.getInstance();
        toDay = txtDate = parseDate(Calendar.getInstance().getTime().getDate(),
                mcvCalendar.getCurrentDate().getMonth(), mcvCalendar.getCurrentDate().getYear());
        mcvCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date,
                                       boolean selected) {
                mcvCalendar.setSelectionColor(R.color.dateSelection);
                if (mcvCalendar.getCurrentDate() != mcvCalendar.getSelectedDate()) {
                    txtDate = parseDate(mcvCalendar.getSelectedDate().getDay(),
                            mcvCalendar.getSelectedDate().getMonth(),
                            mcvCalendar.getSelectedDate().getYear());
                }
            }
        });
    }

    void setImageCover() {
        int month = mcvCalendar.getCurrentDate().getMonth();
        // month in system have value from 0 to 11
        if (month == 10 | month == 11 | month == 0) {
            // ivCover will set mountain winter image if month is Nov, Dec, Jan
            ivCover.setImageResource(R.drawable.bg_mountain_range_winter);
        } else if (month == 1 | month == 2 | month == 3) {
            //ivCover will set dirt_road if month is Feb, Mar, Apr
            ivCover.setImageResource(R.drawable.bg_countryside_dirt_road);
        } else if (month == 4 | month == 5 | month == 6) {
            //ivCover will set misty forest image if month is May, June, July
            ivCover.setImageResource(R.drawable.bg_misty_forest);
        } else if (month == 7 | month == 8 | month == 9) {
            // ivCover will set autumn forest mountain if month is Aug, Sep, Oct
            ivCover.setImageResource(R.drawable.bg_autumn_forest_mountain);
        }
    }

    public StringBuilder parseDate(int day, int month, int year) {
        StringBuilder textDate;
        if ((day < 10) && (month + 1 < 10)) {
            textDate = new StringBuilder().append("0" + (day))
                    .append("-").append("0" + (month + 1)).append("-").append(year);
        } else if ((day < 10) && (month + 1 >= 10)) {
            textDate = new StringBuilder().append("0" + (day))
                    .append("-").append(month + 1).append("-").append(year);
        } else if ((day >= 10) && (month + 1 < 10)) {
            textDate = new StringBuilder().append(day).append("-")
                    .append("0" + (month + 1)).append("-").append(year);
        } else {
            textDate = new StringBuilder().append(day).append("-")
                    .append(month + 1).append("-").append(year);
        }
        return textDate;
    }


    public void passDataDatetoAddEvent() {
        /*intentSetDate = new Intent(getActivity(), AddEventActivity.class);
        intentSetDate.putExtra("ChangeDate", txtDate.toString());
        intentSetDate.putExtra("ToDay", toDay.toString());
        intentSetDate.putExtra("MailUser", sMailUser);
        intentSetDate.putExtra("NameUser", sNameUser);
        startActivity(intentSetDate);*/

    }

}
