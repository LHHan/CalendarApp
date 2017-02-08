package com.example.lehoanghan.optionmenu;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lehoanghan.EventValue;
import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.list_oldevent.EventRecyclerAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by lehoanghan on 5/13/2016.
 */
public class MyEvent extends Fragment {
    Activity root;
    View jview;

    public static final int TiTle = 0;
    public static final int Data = 1;
    private RecyclerView mRecyclerView;
    private EventRecyclerAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Firebase firebase;
    private String NameUser, MailUser;
    private Bundle bundleGiveMailfromMenu;
    private ArrayList<EventValue > listnewevent1;
    private ArrayList<Integer> datatype1;
    private ArrayList<String> Listdatefrom;

    public MyEvent(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root=getActivity();
        jview=inflater.inflate(R.layout.screen_myevent,container,false);
        PassDataFromChoose();
        Firebase.setAndroidContext(root);
        firebase=new Firebase("https://appcalendar.firebaseio.com/");
        Init();
        linearLayoutManager = new LinearLayoutManager(jview.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        GetNewEventFormFirebase();

        return jview;
    }


    public void Init()
    {
        listnewevent1=new ArrayList<EventValue>();
        datatype1=new ArrayList<Integer>();
        Listdatefrom=new ArrayList<String>();
        mRecyclerView = (RecyclerView) jview.findViewById(R.id.RVMyEvent);
    }

    public void GetNewEventFormFirebase(){

        final ArrayList<EventValue> Old_Event=new ArrayList<EventValue>();

        final ArrayList<EventValue > Listnewevent=new ArrayList<EventValue>();
        final ArrayList<Integer> DataType=new ArrayList<Integer>();
        firebase.child("Event").child(MailUser).child("Old_Event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Object tem= snapshot.getValue();
                    HashMap event=(HashMap)tem;
                    Old_Event.add(new EventValue(event.get("NameEvent").toString(),event.get("DateFrom").toString(),
                            event.get("TimeFrom").toString(),event.get("DateTo").toString(),event.get("TimeTo").toString(),
                            event.get("Description").toString(),event.get("Place").toString(),event.get("FriendInvite").toString(),
                            event.get("Alarm").toString(), event.get("Repeat").toString()));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
//        firebase.child("Event").child(MailUser).child("Accept_Event").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot:dataSnapshot.getChildren())
//                {
//                    Object tem= snapshot.getValue();
//                    HashMap event=(HashMap)tem;
//                    TemAgree.add(new EventValue(event.get("NameEvent").toString(), event.get("DateFrom").toString(),
//                            event.get("TimeFrom").toString(), event.get("DateTo").toString(), event.get("TimeTo").toString(),
//                            event.get("Description").toString(), event.get("Place").toString(), event.get("FriendInvite").toString(),
//                            event.get("Alarm").toString(), event.get("Repeat").toString()));
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
        firebase.child("Event").child(MailUser).child("Accept_Event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    int Check=0;
                    Object tem= snapshot.getValue();
                    HashMap event=(HashMap)tem;
                    if(Check==0)
                    {
                        for(EventValue eventValue:Old_Event)
                        {
                            if(eventValue.getNameEvent().compareTo(event.get("NameEvent").toString())==0&&
                                    eventValue.getDateFrom().compareTo(event.get("DateFrom").toString())==0&&
                                    eventValue.getTimeFrom().compareTo(event.get("TimeFrom").toString())==0)
                            {
                                Check=1;
                                break;
                            }
                        }
                    }
                    if(Check==0) {
                        Calendar cal=Calendar.getInstance();
                        String toDay=ParseDate(Calendar.getInstance().getTime().getDate(), Calendar.getInstance().getTime().getMonth(), cal.get(Calendar.YEAR));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Date date1,date2;
                        date1=date2=cal.getTime();
                        try {
                             date1= sdf.parse(event.get("DateFrom").toString());
                             date2 = sdf.parse(toDay);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

//                            Object tem= snapshot.getValue();
//                            HashMap event=(HashMap)tem;
                            if(date1.compareTo(date2)<=0)
                            {
                                Check=-1;
                                Map<String, String> eventValue = new Hashtable<String, String>();
                                eventValue.put("NameEvent", event.get("NameEvent").toString());
                                eventValue.put("DateFrom", event.get("DateFrom").toString());
                                eventValue.put("TimeFrom", event.get("TimeFrom").toString());
                                eventValue.put("DateTo", event.get("DateTo").toString());
                                eventValue.put("TimeTo", event.get("TimeTo").toString());
                                eventValue.put("Description",event.get("Description").toString());
                                eventValue.put("Place", event.get("Place").toString());
                                eventValue.put("FriendInvite", event.get("FriendInvite").toString());
                                eventValue.put("Alarm",event.get("Alarm").toString());
                                eventValue.put("Repeat", event.get("Repeat").toString());
                                firebase.child("Event").child(MailUser).child("Old_Event").push().setValue(eventValue);
                                break;
                            }
                    }
                    if(Check==0) {
//                        Object tem= snapshot.getValue();
//                        HashMap event=(HashMap)tem;
                        Listnewevent.add(new EventValue(event.get("NameEvent").toString(),event.get("DateFrom").toString(),
                                event.get("TimeFrom").toString(),event.get("DateTo").toString(),event.get("TimeTo").toString(),
                                event.get("Description").toString(),event.get("Place").toString(),event.get("FriendInvite").toString(),
                                event.get("Alarm").toString(), event.get("Repeat").toString()));
                    }

                }
                for(int i=0;i<Listnewevent.size();i++)
                {
                    Listdatefrom.add(Listnewevent.get(i).getDateFrom());
                }
                for(int i=0;i<Listdatefrom.size();i++)
                {
                    int h=1;
                    for(int j=i+1;j<Listdatefrom.size();j++)
                    {
                        if(Listdatefrom.get(j).compareTo(Listdatefrom.get(i))==0) {
                            h++;
                            Listdatefrom.remove(j);
                            j--;
                        }
                    }
                    DataType.add(h);
                }

                Collections.sort(Listdatefrom);

                int count=0;

                for(int i=0;i<Listdatefrom.size();i++)
                {
                    int h=0;
                    for(int j=0;j<Listnewevent.size();j++)
                    {
                        if(Listnewevent.get(j).getDateFrom().compareTo(Listdatefrom.get(i))==0)
                        {
                            if(h==0) {

                                listnewevent1.add(new EventValue(Listnewevent.get(j).getNameEvent(),Listnewevent.get(j).getDateFrom(),
                                        Listnewevent.get(j).getTimeFrom(),Listnewevent.get(j).getDateTo(),Listnewevent.get(j).getTimeTo(),
                                        Listnewevent.get(j).getDescription(),Listnewevent.get(j).getPlace(),Listnewevent.get(j).getFriendInvite(),
                                        Listnewevent.get(j).getAlarm(),Listnewevent.get(j).getRepeat(),0));
                                datatype1.add(TiTle);
                                count++;
                                h++;
                            }
                            listnewevent1.add(new EventValue(Listnewevent.get(j).getNameEvent(),Listnewevent.get(j).getDateFrom(),
                                    Listnewevent.get(j).getTimeFrom(),Listnewevent.get(j).getDateTo(),Listnewevent.get(j).getTimeTo(),
                                    Listnewevent.get(j).getDescription(),Listnewevent.get(j).getPlace(),Listnewevent.get(j).getFriendInvite(),
                                    Listnewevent.get(j).getAlarm(),Listnewevent.get(j).getRepeat(),1));
                            count++;
                            datatype1.add(Data);
                        }
                    }
                }

                mAdapter = new EventRecyclerAdapter(listnewevent1,datatype1,MailUser,2);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
    public void PassDataFromChoose(){
        bundleGiveMailfromMenu=this.getArguments();
        if(bundleGiveMailfromMenu!=null) {
            MailUser = bundleGiveMailfromMenu.getString("MailforFindFriend");
            NameUser=bundleGiveMailfromMenu.getString("NameforFindFriend");
            NameUser=NameUser.toLowerCase();
        }
    }

    public String ParseDate(int day, int month, int year) {
        StringBuilder textdate;
        if((day<10)&&(month+1<10))
            textdate=new StringBuilder().append("0"+(day)).append("-").append("0" + (month + 1)).append("-").append(year);
        else if((day<10)&&(month+1>=10))
            textdate=new StringBuilder().append("0"+(day)).append("-"). append(month + 1).append("-").append(year);
        else if((day>=10)&&(month+1<10))
            textdate=new StringBuilder().append(day).append("-").append("0" + (month + 1)).append("-").append(year);
        else
            textdate=new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year);
        return textdate.toString();
    }

}
