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

    public OldEventActivity(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root=getActivity();
        jview=inflater.inflate(R.layout.activity_old_event,container,false);
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
        mRecyclerView = (RecyclerView) jview.findViewById(R.id.RVOldEvent);
    }

    public void GetNewEventFormFirebase() {
        final ArrayList<EventValue> Listnewevent = new ArrayList<>();
        firebase.child("Event").child(MailUser).child("Old_Event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Object tem = snapshot.getValue();
                    HashMap event = (HashMap) tem;
                    Listnewevent.add(new EventValue(event.get("NameEvent").toString(), event.get("DateFrom").toString(),
                            event.get("TimeFrom").toString(), event.get("DateTo").toString(), event.get("TimeTo").toString(),
                            event.get("Description").toString(), event.get("Place").toString(), event.get("FriendInvite").toString(),
                            event.get("Alarm").toString(), event.get("Repeat").toString()));
                }

                for (int i = 0; i < Listnewevent.size(); i++) {
                    Listdatefrom.add(Listnewevent.get(i).getDateFrom());
                }
                for (int i = 0; i < Listdatefrom.size(); i++) {
                    int h = 1;
                    for (int j = i + 1; j < Listdatefrom.size(); j++) {
                        if (Listdatefrom.get(j).compareTo(Listdatefrom.get(i)) == 0) {
                            h++;
                            Listdatefrom.remove(j);
                            j--;
                        }
                    }
                }

                Collections.sort(Listdatefrom);

                int count = 0;

                for (int i = 0; i < Listdatefrom.size(); i++) {
                    int h = 0;
                    for (int j = 0; j < Listnewevent.size(); j++) {
                        if (Listnewevent.get(j).getDateFrom().compareTo(Listdatefrom.get(i)) == 0) {
                            if (h == 0) {

                                listnewevent1.add(new EventValue(Listnewevent.get(j).getNameEvent(), Listnewevent.get(j).getDateFrom(),
                                        Listnewevent.get(j).getTimeFrom(), Listnewevent.get(j).getDateTo(), Listnewevent.get(j).getTimeTo(),
                                        Listnewevent.get(j).getDescription(), Listnewevent.get(j).getPlace(), Listnewevent.get(j).getFriendInvite(),
                                        Listnewevent.get(j).getAlarm(), Listnewevent.get(j).getRepeat(), 0));
                                datatype1.add(TiTle);
                                count++;
                                h++;
                            }
                            listnewevent1.add(new EventValue(Listnewevent.get(j).getNameEvent(), Listnewevent.get(j).getDateFrom(),
                                    Listnewevent.get(j).getTimeFrom(), Listnewevent.get(j).getDateTo(), Listnewevent.get(j).getTimeTo(),
                                    Listnewevent.get(j).getDescription(), Listnewevent.get(j).getPlace(), Listnewevent.get(j).getFriendInvite(),
                                    Listnewevent.get(j).getAlarm(), Listnewevent.get(j).getRepeat(), 1));
                            count++;
                            datatype1.add(Data);
                        }
                    }
                }

                mAdapter = new EventRecyclerAdapter(listnewevent1, datatype1, MailUser,NameUser,3);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled (FirebaseError firebaseError){

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

}
