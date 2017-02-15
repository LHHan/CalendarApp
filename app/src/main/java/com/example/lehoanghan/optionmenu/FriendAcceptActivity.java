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

import com.example.lehoanghan.UserFriend;
import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.list_acceptfriend.UserAcceptFriendRecyclerAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lehoanghan on 3/30/2016.
 */
public class FriendAcceptActivity extends Fragment {

    private static String sGetName;

    private static String sGetMail;

    private Activity activityRoot;

    private View contentView;

    private Bundle bundleGiveMailfromMenu;

    private Firebase aFirebase;

    private RecyclerView rcvListFriendAccept;

    private List<String> listMail;

    private List<String> listName;

    private List<String> listMailPre;

    private List<String> listNamePre;

    private List<UserFriend> listUserAccept;

    private UserAcceptFriendRecyclerAdapter userAcceptFriendRecyclerAdapter;

    private LinearLayoutManager linearLayoutManager;

    public FriendAcceptActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        giveUserfromChoose();
        activityRoot = getActivity();
        contentView = inflater.inflate(R.layout.activity_friend_accept, container, false);
        Firebase.setAndroidContext(activityRoot);
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        aInit();

        rcvListFriendAccept.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(contentView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvListFriendAccept.setLayoutManager(linearLayoutManager);
        //getDatafromFireBase();
        resetData();
        return contentView;
    }

    public void resetData() {
        aFirebase.child("My_friend").child(sGetMail)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                            //String change = Snapshot.getKey().toString().replace("&", ".");
                            listMailPre.add(snapShot.getKey());
                            listNamePre.add(snapShot.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
        aFirebase.child("Add_friend").child(sGetMail)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                            int check = 0;
                            for (String mail : listMailPre) {
                                if (snapShot.getKey().compareTo(mail) == 0) {
                                    check = 1;
                                    break;
                                }
                            }
                            if (check == 0) {
                                listMail.add(snapShot.getKey().toString().replace("&", "."));
                                listName.add(snapShot.getValue().toString());
                            }
                        }
                        for (int i = 0; i < listName.size(); i++) {
                            listUserAccept.add(new UserFriend(listName.get(i), listMail.get(i)));
                        }

                        userAcceptFriendRecyclerAdapter =
                                new UserAcceptFriendRecyclerAdapter(
                                        listUserAccept, sGetMail, sGetName);
                        userAcceptFriendRecyclerAdapter.notifyDataSetChanged();
                        rcvListFriendAccept.setAdapter(userAcceptFriendRecyclerAdapter);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
    }

    public void aInit() {
        rcvListFriendAccept = (RecyclerView) contentView.findViewById(
                R.id.activity_friend_accept_rcv_listUserFriendAccept);
        listUserAccept = new ArrayList<UserFriend>();
        listName = new ArrayList<String>();
        listMail = new ArrayList<String>();
        listNamePre = new ArrayList<String>();
        listMailPre = new ArrayList<String>();
    }

    public void giveUserfromChoose() {
        bundleGiveMailfromMenu = this.getArguments();
        if (bundleGiveMailfromMenu != null) {
            sGetMail = bundleGiveMailfromMenu.getString("MailforFindFriend");
            //sGetMail=sGetMail.replace("&", ".");
            sGetName = bundleGiveMailfromMenu.getString("NameforFindFriend");
            sGetName = sGetName.toLowerCase();
        }
    }

}
