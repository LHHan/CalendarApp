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
import android.widget.SearchView;

import com.example.lehoanghan.UserFriend;
import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.list_findfriend.UserFindFriendRecyclerAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lehoanghan on 3/30/2016.
 */
public class FindFriendActivity extends Fragment {
    private Activity activityRoot;

    private View contentView;

    private Firebase fireBase;

    private SearchView srvFindFriend;

    private RecyclerView rcvListFriend;

    private LinearLayoutManager linearLayoutManager;

    private com.example.lehoanghan.list_findfriend.UserFindFriendRecyclerAdapter
            userFindFriendRecyclerAdapter;

    private List<UserFriend> listUserFriend;

    private List<String> listNameUser;

    private List<String> listNameUserFriend;//ab_search friend base on name.

    private List<String> listMailUser;

    private List<String> listMailUserFriend;//ab_search friend base on mail.

    private String getName;

    private String getMail;

    private Bundle bundleGiveMailFromMenu;

    public FindFriendActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activityRoot = getActivity();
        contentView = inflater.inflate(R.layout.activity_find_friend, container, false);
        giveUserfromChoose();

        Firebase.setAndroidContext(activityRoot);
        fireBase = new Firebase("https://appcalendar.firebaseio.com/");

        rcvListFriend = (RecyclerView) contentView.findViewById(
                R.id.activity_find_friend_rcv_list_find_friend);
        srvFindFriend = (SearchView) contentView.findViewById(
                R.id.activity_find_friend_srv_search);

        listUserFriend = new ArrayList<UserFriend>();
        listNameUser = new ArrayList<String>();
        listMailUser = new ArrayList<String>();
        listNameUserFriend = new ArrayList<String>();
        listMailUserFriend = new ArrayList<String>();

        srvFindFriend.setVisibility(View.INVISIBLE);

        rcvListFriend.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(contentView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvListFriend.setLayoutManager(linearLayoutManager);
        getDatafromFireBase();

        return contentView;
    }

    public void getDatafromFireBase() {
        //give DATA from My_friend table
        fireBase.child("My_friend").child(getMail.replace(".", "&"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                            listMailUserFriend.add(snapShot.getKey());
                            listNameUserFriend.add(snapShot.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
        fireBase.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // compare user in User with user in my_friend
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    int check = 0;
                    if (snapShot.getKey().compareTo(getMail.replace(".", "&")) != 0) {
                        for (String mail : listMailUserFriend) {
                            if (snapShot.getKey().compareTo(mail) == 0) {
                                check = 1;
                                break;
                            }
                        }
                        if (check == 0) {
                            listMailUser.add(snapShot.getKey().toString().replace("&", "."));
                            listNameUser.add(snapShot.getValue().toString());
                        }
                    }
                }

                for (int i = 0; i < listNameUser.size(); i++) {
                    listUserFriend.add(new UserFriend(listNameUser.get(i), listMailUser.get(i)));
                }

                userFindFriendRecyclerAdapter =
                        new UserFindFriendRecyclerAdapter(listUserFriend, getMail, getName);
                userFindFriendRecyclerAdapter.notifyDataSetChanged();
                rcvListFriend.setAdapter(userFindFriendRecyclerAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
        // return listUserFriend;
    }

    public void giveUserfromChoose() {
        bundleGiveMailFromMenu = this.getArguments();
        if (bundleGiveMailFromMenu != null) {
            getMail = bundleGiveMailFromMenu.getString("MailforFindFriend");
            getMail = getMail.replace("&", ".");
            getName = bundleGiveMailFromMenu.getString("NameforFindFriend");
            getName = getName.toLowerCase();
        }
    }

}
