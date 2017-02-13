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
public class MyEventActivity extends Fragment {
    Activity root;
    View contentView;

    public static final int title = 0;
    public static final int data = 1;
    private RecyclerView recyclerView;
    private EventRecyclerAdapter eventRecycleAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Firebase firebase;
    private String nameUser, mailUser;
    private Bundle bundleGiveMailfromMenu;
    private ArrayList<EventValue> listnewevent1;
    private ArrayList<Integer> dataType1;
    private ArrayList<String> listDateFrom;

    public MyEventActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = getActivity();
        contentView = inflater.inflate(R.layout.activity_my_event, container, false);
        PassDataFromChoose();
        Firebase.setAndroidContext(root);
        firebase = new Firebase("https://appcalendar.firebaseio.com/");
        Init();
        linearLayoutManager = new LinearLayoutManager(contentView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        GetNewEventFormFirebase();

        return contentView;
    }


    public void Init() {
        listnewevent1 = new ArrayList<EventValue>();
        dataType1 = new ArrayList<Integer>();
        listDateFrom = new ArrayList<String>();
        recyclerView = (RecyclerView) contentView.findViewById(R.id.activity_my_envent_rcv_list_my_event);
    }

    public void GetNewEventFormFirebase() {

        final ArrayList<EventValue> Old_Event = new ArrayList<EventValue>();

        final ArrayList<EventValue> listNewEvent = new ArrayList<EventValue>();
        final ArrayList<Integer> dataType = new ArrayList<Integer>();
        firebase.child("Event").child(mailUser).child("Old_Event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Object tem = snapshot.getValue();
                    HashMap event = (HashMap) tem;
                    Old_Event.add(new EventValue(event.get("NameEvent").toString(), event.get("DateFrom").toString(),
                            event.get("TimeFrom").toString(), event.get("DateTo").toString(), event.get("TimeTo").toString(),
                            event.get("Description").toString(), event.get("Place").toString(), event.get("FriendInvite").toString(),
                            event.get("Alarm").toString(), event.get("Repeat").toString()));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
//        firebase.child("Event").child(mailUser).child("Accept_Event").addValueEventListener(new ValueEventListener() {
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
        firebase.child("Event").child(mailUser).child("Accept_Event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int Check = 0;
                    Object tem = snapshot.getValue();
                    HashMap event = (HashMap) tem;
                    if (Check == 0) {
                        for (EventValue eventValue : Old_Event) {
                            if (eventValue.getNameEvent().compareTo(event.get("NameEvent").toString()) == 0 &&
                                    eventValue.getDateFrom().compareTo(event.get("DateFrom").toString()) == 0 &&
                                    eventValue.getTimeFrom().compareTo(event.get("TimeFrom").toString()) == 0) {
                                Check = 1;
                                break;
                            }
                        }
                    }
                    if (Check == 0) {
                        Calendar cal = Calendar.getInstance();
                        String toDay = ParseDate(Calendar.getInstance().getTime().getDate(), Calendar.getInstance().getTime().getMonth(), cal.get(Calendar.YEAR));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Date date1, date2;
                        date1 = date2 = cal.getTime();
                        try {
                            date1 = sdf.parse(event.get("DateFrom").toString());
                            date2 = sdf.parse(toDay);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

//                            Object tem= snapshot.getValue();
//                            HashMap event=(HashMap)tem;
                        if (date1.compareTo(date2) <= 0) {
                            Check = -1;
                            Map<String, String> eventValue = new Hashtable<String, String>();
                            eventValue.put("NameEvent", event.get("NameEvent").toString());
                            eventValue.put("DateFrom", event.get("DateFrom").toString());
                            eventValue.put("TimeFrom", event.get("TimeFrom").toString());
                            eventValue.put("DateTo", event.get("DateTo").toString());
                            eventValue.put("TimeTo", event.get("TimeTo").toString());
                            eventValue.put("Description", event.get("Description").toString());
                            eventValue.put("Place", event.get("Place").toString());
                            eventValue.put("FriendInvite", event.get("FriendInvite").toString());
                            eventValue.put("Alarm", event.get("Alarm").toString());
                            eventValue.put("Repeat", event.get("Repeat").toString());
                            firebase.child("Event").child(mailUser).child("Old_Event").push().setValue(eventValue);
                            break;
                        }
                    }
                    if (Check == 0) {
//                        Object tem= snapshot.getValue();
//                        HashMap event=(HashMap)tem;
                        listNewEvent.add(new EventValue(event.get("NameEvent").toString(), event.get("DateFrom").toString(),
                                event.get("TimeFrom").toString(), event.get("DateTo").toString(), event.get("TimeTo").toString(),
                                event.get("Description").toString(), event.get("Place").toString(), event.get("FriendInvite").toString(),
                                event.get("Alarm").toString(), event.get("Repeat").toString()));
                    }

                }
                for (int i = 0; i < listNewEvent.size(); i++) {
                    listDateFrom.add(listNewEvent.get(i).getDateFrom());
                }
                for (int i = 0; i < listDateFrom.size(); i++) {
                    int h = 1;
                    for (int j = i + 1; j < listDateFrom.size(); j++) {
                        if (listDateFrom.get(j).compareTo(listDateFrom.get(i)) == 0) {
                            h++;
                            listDateFrom.remove(j);
                            j--;
                        }
                    }
                    dataType.add(h);
                }

                Collections.sort(listDateFrom);

                int count = 0;

                for (int i = 0; i < listDateFrom.size(); i++) {
                    int h = 0;
                    for (int j = 0; j < listNewEvent.size(); j++) {
                        if (listNewEvent.get(j).getDateFrom().compareTo(listDateFrom.get(i)) == 0) {
                            if (h == 0) {

                                listnewevent1.add(new EventValue(listNewEvent.get(j).getNameEvent(), listNewEvent.get(j).getDateFrom(),
                                        listNewEvent.get(j).getTimeFrom(), listNewEvent.get(j).getDateTo(), listNewEvent.get(j).getTimeTo(),
                                        listNewEvent.get(j).getDescription(), listNewEvent.get(j).getPlace(), listNewEvent.get(j).getFriendInvite(),
                                        listNewEvent.get(j).getAlarm(), listNewEvent.get(j).getRepeat(), 0));
                                dataType1.add(title);
                                count++;
                                h++;
                            }
                            listnewevent1.add(new EventValue(listNewEvent.get(j).getNameEvent(), listNewEvent.get(j).getDateFrom(),
                                    listNewEvent.get(j).getTimeFrom(), listNewEvent.get(j).getDateTo(), listNewEvent.get(j).getTimeTo(),
                                    listNewEvent.get(j).getDescription(), listNewEvent.get(j).getPlace(), listNewEvent.get(j).getFriendInvite(),
                                    listNewEvent.get(j).getAlarm(), listNewEvent.get(j).getRepeat(), 1));
                            count++;
                            dataType1.add(data);
                        }
                    }
                }

                eventRecycleAdapter = new EventRecyclerAdapter(listnewevent1, dataType1, mailUser, 2);
                eventRecycleAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(eventRecycleAdapter);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void PassDataFromChoose() {
        bundleGiveMailfromMenu = this.getArguments();
        if (bundleGiveMailfromMenu != null) {
            mailUser = bundleGiveMailfromMenu.getString("MailforFindFriend");
            nameUser = bundleGiveMailfromMenu.getString("NameforFindFriend");
            nameUser = nameUser.toLowerCase();
        }
    }

    public String ParseDate(int day, int month, int year) {
        StringBuilder textdate;
        if ((day < 10) && (month + 1 < 10))
            textdate = new StringBuilder().append("0" + (day)).append("-").append("0" + (month + 1)).append("-").append(year);
        else if ((day < 10) && (month + 1 >= 10))
            textdate = new StringBuilder().append("0" + (day)).append("-").append(month + 1).append("-").append(year);
        else if ((day >= 10) && (month + 1 < 10))
            textdate = new StringBuilder().append(day).append("-").append("0" + (month + 1)).append("-").append(year);
        else
            textdate = new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year);
        return textdate.toString();
    }

}
