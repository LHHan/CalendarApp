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
public class OldEventActivity extends Fragment {
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

    public OldEventActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = getActivity();
        contentView = inflater.inflate(R.layout.activity_old_event, container, false);
        passDataFromChoose();
        Firebase.setAndroidContext(root);
        firebase = new Firebase("https://appcalendar.firebaseio.com/");
        Init();
        linearLayoutManager = new LinearLayoutManager(contentView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getNewEventFormFirebase();
        return contentView;
    }

    public void Init() {
        listNewEvent1 = new ArrayList<EventValue>();
        dataType1 = new ArrayList<Integer>();
        listDateFrom = new ArrayList<String>();
        recyclerView = (RecyclerView) contentView.findViewById(R.id.activity_old_event_rcv_list_old_event);
    }

    public void getNewEventFormFirebase() {
        final ArrayList<EventValue> listNewEvent = new ArrayList<>();
        firebase.child("Event").child(mailUser).child("Old_Event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Object tem = snapshot.getValue();
                    HashMap event = (HashMap) tem;
                    listNewEvent.add(new EventValue(event.get("NameEvent").toString(), event.get("DateFrom").toString(),
                            event.get("TimeFrom").toString(), event.get("DateTo").toString(), event.get("TimeTo").toString(),
                            event.get("Description").toString(), event.get("Place").toString(), event.get("FriendInvite").toString(),
                            event.get("Alarm").toString(), event.get("Repeat").toString()));
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
                }

                Collections.sort(listDateFrom);

                int count = 0;

                for (int i = 0; i < listDateFrom.size(); i++) {
                    int h = 0;
                    for (int j = 0; j < listNewEvent.size(); j++) {
                        if (listNewEvent.get(j).getDateFrom().compareTo(listDateFrom.get(i)) == 0) {
                            if (h == 0) {

                                listNewEvent1.add(new EventValue(listNewEvent.get(j).getNameEvent(), listNewEvent.get(j).getDateFrom(),
                                        listNewEvent.get(j).getTimeFrom(), listNewEvent.get(j).getDateTo(), listNewEvent.get(j).getTimeTo(),
                                        listNewEvent.get(j).getDescription(), listNewEvent.get(j).getPlace(), listNewEvent.get(j).getFriendInvite(),
                                        listNewEvent.get(j).getAlarm(), listNewEvent.get(j).getRepeat(), 0));
                                dataType1.add(title);
                                count++;
                                h++;
                            }
                            listNewEvent1.add(new EventValue(listNewEvent.get(j).getNameEvent(), listNewEvent.get(j).getDateFrom(),
                                    listNewEvent.get(j).getTimeFrom(), listNewEvent.get(j).getDateTo(), listNewEvent.get(j).getTimeTo(),
                                    listNewEvent.get(j).getDescription(), listNewEvent.get(j).getPlace(), listNewEvent.get(j).getFriendInvite(),
                                    listNewEvent.get(j).getAlarm(), listNewEvent.get(j).getRepeat(), 1));
                            count++;
                            dataType1.add(data);
                        }
                    }
                }

                eventRecyclerAdapter = new EventRecyclerAdapter(listNewEvent1, dataType1, mailUser, nameUser, 3);
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
