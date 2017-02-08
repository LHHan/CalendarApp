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

import com.example.lehoanghan.addevent.AddEvent;
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
public class Home extends Fragment {

    Activity root;
    private Fragment fragment=null;
    public Home()
    {

    }


    private TextView TxtNameUser;
    private ImageView ImgAvataHome;
    private Bundle bundleSetDate;
    private Intent intentSetDate;
    private MaterialCalendarView calendar;
    private View jview;
    private StringBuilder textDate,toDay;
    private static String MailUser,NameUser;
    private Firebase firebase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle=this.getArguments();
        NameUser=bundle.getString("NameforHome");
        MailUser=bundle.getString("MailforHome");
        jview= inflater.inflate(R.layout.screen_home, container, false);
        root=getActivity();
        Firebase.setAndroidContext(root);
        firebase=new Firebase("https://appcalendar.firebaseio.com/");
        setHasOptionsMenu(true);
        TxtNameUser=(TextView)jview.findViewById(R.id.txtNameUser);
        ImgAvataHome=(ImageView) jview.findViewById(R.id.imgAvataHome);
        TxtNameUser.setText(TxtNameUser.getText().toString() + " " + NameUser);
        TxtNameUser.setTextColor(Color.parseColor("#FFFF50A4"));
        SetImage();

        calendar= (MaterialCalendarView) jview.findViewById(R.id.calendarView);
        calendar.setSelectedDate(Calendar.getInstance());

        Calendar cal=Calendar.getInstance();
        toDay=textDate=ParseDate(Calendar.getInstance().getTime().getDate(), calendar.getCurrentDate().getMonth(),calendar.getCurrentDate().getYear());

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                calendar.setSelectionColor(R.color.DateSelection);
                if (calendar.getCurrentDate() != calendar.getSelectedDate()) {
                    textDate=ParseDate(calendar.getSelectedDate().getDay(),calendar.getSelectedDate().getMonth(),calendar.getSelectedDate().getYear());
                }
            }
        });
        return jview;
    }



    public StringBuilder ParseDate(int day, int month, int year) {
        StringBuilder textdate;
        if((day<10)&&(month+1<10))
            textdate=new StringBuilder().append("0"+(day)).append("-").append("0" + (month + 1)).append("-").append(year);
        else if((day<10)&&(month+1>=10))
            textdate=new StringBuilder().append("0"+(day)).append("-"). append(month + 1).append("-").append(year);
        else if((day>=10)&&(month+1<10))
            textdate=new StringBuilder().append(day).append("-").append("0" + (month + 1)).append("-").append(year);
        else
            textdate=new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year);
        return textdate;
    }


    private MenuInflater menuInflater;
    private Menu menu;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu=menu;
        this.menuInflater=inflater;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.add_event:
                PassDataDatetoAddEvent();
                startActivity(intentSetDate);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void SetImage(){
        firebase.child("Avata").child(MailUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                byte []img= Base64.decode(dataSnapshot.getValue().toString(),Base64.DEFAULT);
                Bitmap bmp= BitmapFactory.decodeByteArray(img,0,img.length);
                ImgAvataHome.setImageBitmap(bmp);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void PassDataDatetoAddEvent()
    {
        intentSetDate=new Intent(getActivity(), AddEvent.class);
        intentSetDate.putExtra("ChangeDate",textDate.toString());
        intentSetDate.putExtra("ToDay",toDay.toString());
        intentSetDate.putExtra("MailUser",MailUser);
        intentSetDate.putExtra("NameUser",NameUser);
    }

}
