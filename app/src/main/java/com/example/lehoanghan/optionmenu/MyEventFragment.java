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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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
@EFragment(R.layout.fragment_my_event)
public class MyEventFragment extends Fragment {
    public static final int TITLE = 0;

    public static final int DATA = 1;

    @ViewById(R.id.fragment_my_envent_rcv_list_my_event)
    RecyclerView recyclerView;


    private View contentView;

    private EventRecyclerAdapter eventRecycleAdapter;

    private LinearLayoutManager linearLayoutManager;

    private Firebase aFirebase;

    private String nameUser;

    private String mailUser;

    private Bundle bundleGiveMailfromMenu;

    private ArrayList<EventValue> listNewEvent1;

    private ArrayList<Integer> dataType1;

    private ArrayList<String> listDateFrom;

    public MyEventFragment() {
    }

    @AfterViews
    void afterView() {
        passDataFromChoose();
        Firebase.setAndroidContext(getActivity());
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        aInit();
        linearLayoutManager = new LinearLayoutManager(contentView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getNewEventFormFirebase();

    }

    public void aInit() {
        listNewEvent1 = new ArrayList<EventValue>();
        dataType1 = new ArrayList<Integer>();
        listDateFrom = new ArrayList<String>();
    }

    public void getNewEventFormFirebase() {
        final ArrayList<EventValue> OLDEVENT = new ArrayList<EventValue>();
        final ArrayList<EventValue> LISTNEWEVENT = new ArrayList<EventValue>();
        final ArrayList<Integer> DATATYPE = new ArrayList<Integer>();
        aFirebase.child("Event").child(mailUser).child("Old_Event")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Object tem = snapshot.getValue();
                            HashMap event = (HashMap) tem;
                            OLDEVENT.add(new EventValue(event.get("NameEvent").toString(),
                                    event.get("DateFrom").toString(),
                                    event.get("TimeFrom").toString(),
                                    event.get("DateTo").toString(),
                                    event.get("TimeTo").toString(),
                                    event.get("Description").toString(),
                                    event.get("Place").toString(),
                                    event.get("FriendInvite").toString(),
                                    event.get("Alarm").toString(),
                                    event.get("Repeat").toString()));
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
        aFirebase.child("Event").child(mailUser).child("Accept_Event")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            int check = 0;
                            Object tem = snapshot.getValue();
                            HashMap event = (HashMap) tem;
                            if (check == 0) {
                                for (EventValue eventValue : OLDEVENT) {
                                    if (eventValue.getNameEvent()
                                            .compareTo(event.get("NameEvent")
                                                    .toString()) == 0 &&
                                            eventValue.getDateFrom()
                                                    .compareTo(event.get("DateFrom")
                                                            .toString()) == 0 &&
                                            eventValue.getTimeFrom()
                                                    .compareTo(event.get("TimeFrom")
                                                            .toString()) == 0) {
                                        check = 1;
                                        break;
                                    }
                                }
                            }
                            if (check == 0) {
                                Calendar cal = Calendar.getInstance();
                                String toDay = parseDate(Calendar.getInstance().getTime().getDate(),
                                        Calendar.getInstance().getTime().getMonth(),
                                        cal.get(Calendar.YEAR));
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                Date aDate1, aDate2;
                                aDate1 = aDate2 = cal.getTime();
                                try {
                                    aDate1 = sdf.parse(event.get("DateFrom").toString());
                                    aDate2 = sdf.parse(toDay);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

//                            Object tem= snapshot.getValue();
//                            HashMap ic_new_event=(HashMap)tem;
                                if (aDate1.compareTo(aDate2) <= 0) {
                                    check = -1;
                                    Map<String, String> eventValue =
                                            new Hashtable<String, String>();
                                    eventValue.put("NameEvent",
                                            event.get("NameEvent").toString());
                                    eventValue.put("DateFrom",
                                            event.get("DateFrom").toString());
                                    eventValue.put("TimeFrom",
                                            event.get("TimeFrom").toString());
                                    eventValue.put("DateTo",
                                            event.get("DateTo").toString());
                                    eventValue.put("TimeTo",
                                            event.get("TimeTo").toString());
                                    eventValue.put("Description",
                                            event.get("Description").toString());
                                    eventValue.put("Place",
                                            event.get("Place").toString());
                                    eventValue.put("FriendInvite",
                                            event.get("FriendInvite").toString());
                                    eventValue.put("Alarm",
                                            event.get("Alarm").toString());
                                    eventValue.put("Repeat",
                                            event.get("Repeat").toString());
                                    aFirebase.child("Event").child(mailUser)
                                            .child("Old_Event").push().setValue(eventValue);
                                    break;
                                }
                            }
                            if (check == 0) {
//                        Object tem= snapshot.getValue();
//                        HashMap ic_new_event=(HashMap)tem;
                                LISTNEWEVENT.add(new EventValue(
                                        event.get("NameEvent").toString(),
                                        event.get("DateFrom").toString(),
                                        event.get("TimeFrom").toString(),
                                        event.get("DateTo").toString(),
                                        event.get("TimeTo").toString(),
                                        event.get("Description").toString(),
                                        event.get("Place").toString(),
                                        event.get("FriendInvite").toString(),
                                        event.get("Alarm").toString(),
                                        event.get("Repeat").toString()));
                            }

                        }
                        for (int i = 0; i < LISTNEWEVENT.size(); i++) {
                            listDateFrom.add(LISTNEWEVENT.get(i).getDateFrom());
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
                            DATATYPE.add(h);
                        }

                        Collections.sort(listDateFrom);

                        int count = 0;

                        for (int i = 0; i < listDateFrom.size(); i++) {
                            int h = 0;
                            for (int j = 0; j < LISTNEWEVENT.size(); j++) {
                                if (LISTNEWEVENT.get(j).getDateFrom()
                                        .compareTo(listDateFrom.get(i)) == 0) {
                                    if (h == 0) {

                                        listNewEvent1.add(new EventValue(
                                                LISTNEWEVENT.get(j).getNameEvent(),
                                                LISTNEWEVENT.get(j).getDateFrom(),
                                                LISTNEWEVENT.get(j).getTimeFrom(),
                                                LISTNEWEVENT.get(j).getDateTo(),
                                                LISTNEWEVENT.get(j).getTimeTo(),
                                                LISTNEWEVENT.get(j).getDescription(),
                                                LISTNEWEVENT.get(j).getPlace(),
                                                LISTNEWEVENT.get(j).getFriendInvite(),
                                                LISTNEWEVENT.get(j).getAlarm(),
                                                LISTNEWEVENT.get(j).getRepeat(), 0));
                                        dataType1.add(TITLE);
                                        count++;
                                        h++;
                                    }
                                    listNewEvent1.add(new EventValue(
                                            LISTNEWEVENT.get(j).getNameEvent(),
                                            LISTNEWEVENT.get(j).getDateFrom(),
                                            LISTNEWEVENT.get(j).getTimeFrom(),
                                            LISTNEWEVENT.get(j).getDateTo(),
                                            LISTNEWEVENT.get(j).getTimeTo(),
                                            LISTNEWEVENT.get(j).getDescription(),
                                            LISTNEWEVENT.get(j).getPlace(),
                                            LISTNEWEVENT.get(j).getFriendInvite(),
                                            LISTNEWEVENT.get(j).getAlarm(),
                                            LISTNEWEVENT.get(j).getRepeat(), 1));
                                    count++;
                                    dataType1.add(DATA);
                                }
                            }
                        }

                        eventRecycleAdapter =
                                new EventRecyclerAdapter(listNewEvent1, dataType1, mailUser, 2);
                        eventRecycleAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(eventRecycleAdapter);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

    }

    public void passDataFromChoose() {
        bundleGiveMailfromMenu = this.getArguments();
        if (bundleGiveMailfromMenu != null) {
            mailUser = bundleGiveMailfromMenu.getString("MailforFindFriend");
            nameUser = bundleGiveMailfromMenu.getString("NameforFindFriend");
            nameUser = nameUser.toLowerCase();
        }
    }

    public String parseDate(int day, int month, int year) {
        StringBuilder textdate;
        if ((day < 10) && (month + 1 < 10)) {
            textdate = new StringBuilder().append("0" + (day))
                    .append("-").append("0" + (month + 1)).append("-").append(year);
        } else if ((day < 10) && (month + 1 >= 10)) {
            textdate = new StringBuilder().append("0" + (day))
                    .append("-").append(month + 1).append("-").append(year);
        } else if ((day >= 10) && (month + 1 < 10)) {
            textdate = new StringBuilder().append(day).append("-")
                    .append("0" + (month + 1)).append("-").append(year);
        } else {
            textdate = new StringBuilder().append(day).append("-")
                    .append(month + 1).append("-").append(year);
        }
        return textdate.toString();
    }

}
