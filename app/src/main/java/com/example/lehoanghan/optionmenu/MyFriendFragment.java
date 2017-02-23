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
import com.example.lehoanghan.list_myfriend.UserMyFriendRecyclerAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lehoanghan on 3/30/2016.
 */
public class MyFriendFragment extends Fragment {

    private Activity activityRoot;

    private View contentView;

    private Firebase aFirebase;

    private List<UserFriend> listMyFriend;

    private List<String> listMail;

    private List<String> listName;

    private RecyclerView rcvListMyFriend;

    private UserMyFriendRecyclerAdapter userMyFriendRecyclerAdapter;

    private LinearLayoutManager linearLayoutManager;

    private Bundle bundleGiveMailfromMenu;

    private String getMail;

    private String getName;

    public MyFriendFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activityRoot = getActivity();
        contentView = inflater.inflate(R.layout.fragment_my_friend, container, false);
        giveUserfromChoose();
        Firebase.setAndroidContext(contentView.getContext());
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        aInit();
        rcvListMyFriend.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(contentView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvListMyFriend.setLayoutManager(linearLayoutManager);
        getDatafromFirebase();
        return contentView;
    }

    public void getDatafromFirebase() {
        aFirebase.child("My_friend").child(getMail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShort : dataSnapshot.getChildren()) {
                    listMail.add(snapShort.getKey().toString().replace("&", "."));
                    listName.add(snapShort.getValue().toString());
                }
                for (int i = 0; i < listName.size(); i++) {
                    listMyFriend.add(new UserFriend(listName.get(i), listMail.get(i)));
                }
                userMyFriendRecyclerAdapter =
                        new UserMyFriendRecyclerAdapter(listMyFriend, getMail, getName);
                userMyFriendRecyclerAdapter.notifyDataSetChanged();
                rcvListMyFriend.setAdapter(userMyFriendRecyclerAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void aInit() {
        listMyFriend = new ArrayList<UserFriend>();
        listMail = new ArrayList<String>();
        listName = new ArrayList<String>();
        rcvListMyFriend = (RecyclerView) contentView.findViewById(
                R.id.fragment_my_friend_rcv_list_my_friend);
    }

    public void giveUserfromChoose() {
        bundleGiveMailfromMenu = this.getArguments();
        if (bundleGiveMailfromMenu != null) {
            getMail = bundleGiveMailfromMenu.getString("MailforFindFriend");
            //getMail=getMail.replace("&", ".");
            getName = bundleGiveMailfromMenu.getString("NameforFindFriend");
            getName = getName.toLowerCase();
        }
    }

}
