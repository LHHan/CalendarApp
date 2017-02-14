package com.example.lehoanghan.optionmenu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
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
public class HomeActivity extends Fragment {

    Activity root;
    private Fragment fragment = null;

    public HomeActivity() {

    }


    private TextView tvNameUser;
    private ImageView imgAvatarHome;
    private Bundle bundleSetDate;
    private Intent intentSetDate;
    private MaterialCalendarView calendar;
    private View contentView;
    private StringBuilder txtDate, toDay;
    private static String strMailUser, strNameUser;
    private Firebase firebase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        strNameUser = bundle.getString("NameforHome");
        strMailUser = bundle.getString("MailforHome");
        contentView = inflater.inflate(R.layout.activity_home, container, false);
        root = getActivity();
        Firebase.setAndroidContext(root);
        firebase = new Firebase("https://appcalendar.firebaseio.com/");
        setHasOptionsMenu(true);
        tvNameUser = (TextView) contentView.findViewById(R.id.activity_home_tv_name);
        imgAvatarHome = (ImageView) contentView.findViewById(R.id.activity_home_iv_avatar);
        tvNameUser.setText(tvNameUser.getText().toString() + " " + strNameUser);
        tvNameUser.setTextColor(Color.parseColor("#FFFF50A4"));
        setImage();

        calendar = (MaterialCalendarView) contentView.findViewById(R.id.activity_home_calendar);
        calendar.setSelectedDate(Calendar.getInstance());

        Calendar cal = Calendar.getInstance();
        toDay = txtDate = ParseDate(Calendar.getInstance().getTime().getDate(), calendar.getCurrentDate().getMonth(), calendar.getCurrentDate().getYear());

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                calendar.setSelectionColor(R.color.dateSelection);
                if (calendar.getCurrentDate() != calendar.getSelectedDate()) {
                    txtDate = ParseDate(calendar.getSelectedDate().getDay(), calendar.getSelectedDate().getMonth(), calendar.getSelectedDate().getYear());
                }
            }
        });
        return contentView;
    }


    public StringBuilder ParseDate(int day, int month, int year) {
        StringBuilder textdate;
        if ((day < 10) && (month + 1 < 10))
            textdate = new StringBuilder().append("0" + (day)).append("-").append("0" + (month + 1)).append("-").append(year);
        else if ((day < 10) && (month + 1 >= 10))
            textdate = new StringBuilder().append("0" + (day)).append("-").append(month + 1).append("-").append(year);
        else if ((day >= 10) && (month + 1 < 10))
            textdate = new StringBuilder().append(day).append("-").append("0" + (month + 1)).append("-").append(year);
        else
            textdate = new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year);
        return textdate;
    }


    private MenuInflater menuInflater;
    private Menu menu;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
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
        firebase.child("Avata").child(strMailUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                byte[] img = Base64.decode(dataSnapshot.getValue().toString(), Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(img, 0, img.length);
                imgAvatarHome.setImageBitmap(bmp);
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
        intentSetDate.putExtra("strMailUser", strMailUser);
        intentSetDate.putExtra("strNameUser", strNameUser);
    }

}
