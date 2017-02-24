package com.example.lehoanghan.optionmenu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lehoanghan.addevent.AddEventActivity;
import com.example.lehoanghan.appcalendar.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;

/**
 * Created by lehoanghan on 3/16/2016.
 */
public class HomeFragment extends Fragment {

    private static String sMailUser;

    private static String sNameUser;

    private Activity activityRoot;

    private Fragment aFragment = null;

    private TextView tvNameUser;

    private ImageView imgAvatarHome;

    private Bundle bundleSetDate;

    private Intent intentSetDate;

    private MaterialCalendarView mcvCalendar;

    private View contentView;

    private StringBuilder txtDate;

    private StringBuilder toDay;

    private Firebase aFirebase;

    private MenuInflater menuInflater;

    private Menu aMenu;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        sNameUser = bundle.getString("NameforHome");
        sMailUser = bundle.getString("MailforHome");
        contentView = inflater.inflate(R.layout.fragment_home, container, false);
        activityRoot = getActivity();
        Firebase.setAndroidContext(activityRoot);
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        setHasOptionsMenu(true);
        tvNameUser = (TextView) contentView.findViewById(R.id.fragment_home_tv_name);
        imgAvatarHome = (ImageView) contentView.findViewById(R.id.fragment_home_iv_avatar);
        tvNameUser.setText(tvNameUser.getText().toString() + " " + sNameUser);
        tvNameUser.setTextColor(Color.parseColor("#FFFF50A4"));
        setImage();

        mcvCalendar = (MaterialCalendarView) contentView.findViewById(R.id.fragment_home_calendar);
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
        return contentView;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.aMenu = menu;
        this.menuInflater = inflater;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_home_ab_add_event:
                passDataDatetoAddEvent();
                startActivity(intentSetDate);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setImage() {
        aFirebase.child("Avata").child(sMailUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                byte[] img = Base64.decode(dataSnapshot.getValue().toString(), Base64.DEFAULT);
//                Bitmap bmp = BitmapFactory.decodeByteArray(img, 0, img.length);
                imgAvatarHome.setImageResource(R.drawable.avt_default);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void passDataDatetoAddEvent() {
        intentSetDate = new Intent(getActivity(), AddEventActivity.class);
        intentSetDate.putExtra("ChangeDate", txtDate.toString());
        intentSetDate.putExtra("ToDay", toDay.toString());
        intentSetDate.putExtra("MailUser", sMailUser);
        intentSetDate.putExtra("NameUser", sNameUser);
    }

}
