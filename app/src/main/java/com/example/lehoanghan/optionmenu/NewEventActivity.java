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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by lehoanghan on 3/30/2016.
 */
public class NewEventActivity extends Fragment {
    Activity root;
    View contentView;

    public static final int title = 0;
    public static final int data = 1;
    private RecyclerView recyclerView;
    private EventRecyclerAdapter eventRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Firebase firebase;
    private String nameUser, mailUser;
    private Bundle bundleGiveMailfromMenu;
    private ArrayList<EventValue> listNewEvent1;
    private ArrayList<Integer> dataType1;
    private ArrayList<String> listDateFrom;

    public NewEventActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = getActivity();
        contentView = inflater.inflate(R.layout.activity_new_event, container, false);
        passDataFromChoose();
        Firebase.setAndroidContext(root);
        firebase = new Firebase("https://appcalendar.firebaseio.com/");
        Init();
        linearLayoutManager = new LinearLayoutManager(contentView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //eventRecyclerAdapter = new CustomAdapter(mDataset, mDataSetTypes);
        // recyclerView.setAdapter(eventRecyclerAdapter);
        getNewEventFormFirebase();

        return contentView;
    }

    public void Init() {
        listNewEvent1 = new ArrayList<EventValue>();
        dataType1 = new ArrayList<Integer>();
        listDateFrom = new ArrayList<String>();
        recyclerView = (RecyclerView) contentView.findViewById(R.id.activity_new_event_rcv_list_new_event);
    }

    public void getNewEventFormFirebase() {

        final ArrayList<EventValue> TemRefuse = new ArrayList<EventValue>();
        final ArrayList<EventValue> TemAgree = new ArrayList<EventValue>();
        final ArrayList<EventValue> Listnewevent = new ArrayList<EventValue>();
        final ArrayList<Integer> DataType = new ArrayList<Integer>();
        firebase.child("Event").child(mailUser).child("Refuse_Event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Object tem = snapshot.getValue();
                    HashMap event = (HashMap) tem;
                    TemRefuse.add(new EventValue(event.get("NameEvent").toString(), event.get("DateFrom").toString(),
                            event.get("TimeFrom").toString(), event.get("DateTo").toString(), event.get("TimeTo").toString(),
                            event.get("Description").toString(), event.get("Place").toString(), event.get("FriendInvite").toString(),
                            event.get("Alarm").toString(), event.get("Repeat").toString()));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        firebase.child("Event").child(mailUser).child("Accept_Event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Object tem = snapshot.getValue();
                    HashMap event = (HashMap) tem;
                    TemAgree.add(new EventValue(event.get("NameEvent").toString(), event.get("DateFrom").toString(),
                            event.get("TimeFrom").toString(), event.get("DateTo").toString(), event.get("TimeTo").toString(),
                            event.get("Description").toString(), event.get("Place").toString(), event.get("FriendInvite").toString(),
                            event.get("Alarm").toString(), event.get("Repeat").toString()));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        firebase.child("Event").child(mailUser).child("New_Event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int Check = 0;
                    if (Check == 0) {
                        for (EventValue eventValue : TemRefuse) {
                            Object tem = snapshot.getValue();
                            HashMap event = (HashMap) tem;
                            if (eventValue.getNameEvent().compareTo(event.get("NameEvent").toString()) == 0 &&
                                    eventValue.getDateFrom().compareTo(event.get("DateFrom").toString()) == 0 &&
                                    eventValue.getTimeFrom().compareTo(event.get("TimeFrom").toString()) == 0) {
                                Check = 1;
                                break;
                            }
                        }
                    }
                    if (Check == 0) {
                        for (EventValue eventValue : TemAgree) {
                            Object tem = snapshot.getValue();
                            HashMap event = (HashMap) tem;
                            if (eventValue.getNameEvent().compareTo(event.get("NameEvent").toString()) == 0 &&
                                    eventValue.getDateFrom().compareTo(event.get("DateFrom").toString()) == 0 &&
                                    eventValue.getTimeFrom().compareTo(event.get("TimeFrom").toString()) == 0) {
                                Check = -1;
                                break;
                            }
                        }
                    }
                    if (Check == 0) {
                        Object tem = snapshot.getValue();
                        HashMap event = (HashMap) tem;
                        Listnewevent.add(new EventValue(event.get("NameEvent").toString(), event.get("DateFrom").toString(),
                                event.get("TimeFrom").toString(), event.get("DateTo").toString(), event.get("TimeTo").toString(),
                                event.get("Description").toString(), event.get("Place").toString(), event.get("FriendInvite").toString(),
                                event.get("Alarm").toString(), event.get("Repeat").toString()));
                    }

                }
                for (int i = 0; i < Listnewevent.size(); i++) {
                    listDateFrom.add(Listnewevent.get(i).getDateFrom());
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
                    DataType.add(h);
                }

                Collections.sort(listDateFrom);

                int count = 0;

                for (int i = 0; i < listDateFrom.size(); i++) {
                    int h = 0;
                    for (int j = 0; j < Listnewevent.size(); j++) {
                        if (Listnewevent.get(j).getDateFrom().compareTo(listDateFrom.get(i)) == 0) {
                            if (h == 0) {

                                listNewEvent1.add(new EventValue(Listnewevent.get(j).getNameEvent(), Listnewevent.get(j).getDateFrom(),
                                        Listnewevent.get(j).getTimeFrom(), Listnewevent.get(j).getDateTo(), Listnewevent.get(j).getTimeTo(),
                                        Listnewevent.get(j).getDescription(), Listnewevent.get(j).getPlace(), Listnewevent.get(j).getFriendInvite(),
                                        Listnewevent.get(j).getAlarm(), Listnewevent.get(j).getRepeat(), 0));
                                dataType1.add(title);
                                count++;
                                h++;
                            }
                            listNewEvent1.add(new EventValue(Listnewevent.get(j).getNameEvent(), Listnewevent.get(j).getDateFrom(),
                                    Listnewevent.get(j).getTimeFrom(), Listnewevent.get(j).getDateTo(), Listnewevent.get(j).getTimeTo(),
                                    Listnewevent.get(j).getDescription(), Listnewevent.get(j).getPlace(), Listnewevent.get(j).getFriendInvite(),
                                    Listnewevent.get(j).getAlarm(), Listnewevent.get(j).getRepeat(), 1));
                            count++;
                            dataType1.add(data);
                        }
                    }
                }

                eventRecyclerAdapter = new EventRecyclerAdapter(listNewEvent1, dataType1, mailUser, nameUser, 1);
                eventRecyclerAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(eventRecyclerAdapter);

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
}
