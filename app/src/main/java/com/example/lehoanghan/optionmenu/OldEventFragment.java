package com.example.lehoanghan.optionmenu;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by lehoanghan on 3/30/2016.
 */
@EFragment(R.layout.fragment_old_event)
public class OldEventFragment extends Fragment {
    public static final int TITLE = 0;

    public static final int DATA = 1;

    @ViewById(R.id.fragment_old_event_rcv_list_old_event)
    RecyclerView recyclerView;

    private EventRecyclerAdapter eventRecyclerAdapter;

    private LinearLayoutManager linearLayoutManager;

    private Firebase aFirebase;

    private String nameUser;

    private String mailUser;

    private Bundle bundleGiveMailfromMenu;

    private ArrayList<EventValue> listNewEvent1;

    private ArrayList<Integer> dataType1;

    private ArrayList<String> listDateFrom;

    public OldEventFragment() {
    }

    @AfterViews
    void afterView() {
        passDataFromChoose();
        Firebase.setAndroidContext(getActivity());
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        initView();
        getNewEventFormFirebase();
    }

    public void initView() {
        listNewEvent1 = new ArrayList<EventValue>();
        dataType1 = new ArrayList<Integer>();
        listDateFrom = new ArrayList<String>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void getNewEventFormFirebase() {
        final ArrayList<EventValue> LISTNEWEVENT = new ArrayList<>();
        aFirebase.child("Event").child(mailUser).child("Old_Event")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Object tem = snapshot.getValue();
                            HashMap event = (HashMap) tem;
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
                        }

                        Collections.sort(listDateFrom);

                        int count = 0;

                        for (int i = 0; i < listDateFrom.size(); i++) {
                            int h = 0;
                            for (int j = 0; j < LISTNEWEVENT.size(); j++) {
                                if (LISTNEWEVENT.get(j).getDateFrom().compareTo(listDateFrom.get(i)) == 0) {
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

                        eventRecyclerAdapter =
                                new EventRecyclerAdapter(listNewEvent1, dataType1, mailUser, nameUser, 3);
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
