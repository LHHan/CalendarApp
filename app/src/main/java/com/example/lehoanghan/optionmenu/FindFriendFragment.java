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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lehoanghan on 3/30/2016.
 */
@EFragment(R.layout.fragment_find_friend)
public class FindFriendFragment extends Fragment {
    @ViewById(R.id.fragment_find_friend_rcv_list_find_friend)
    RecyclerView rcvListFriend;

    private Firebase fireBase;

    private LinearLayoutManager linearLayoutManager;

    private com.example.lehoanghan.list_findfriend.UserFindFriendRecyclerAdapter
            userFindFriendRecyclerAdapter;

    private List<UserFriend> listUserFriend;

    private List<String> listNameUser;

    private List<String> listNameUserFriend;//search friend base on name.

    private List<String> listMailUser;

    private List<String> listMailUserFriend;//search friend base on mail.

    private String getName;

    private String getMail;

    private Bundle bundleGiveMailFromMenu;

    public FindFriendFragment() {
    }

    @AfterViews()
    void afterView() {
        //using google firebase like database
        Firebase.setAndroidContext(getActivity());
        fireBase = new Firebase("https://appcalendar.firebaseio.com/");
        giveUserfromChoose();
        initView();
        //Give date from data base
        getDatafromFireBase();
    }

    void initView() {
        listUserFriend = new ArrayList<UserFriend>();
        listNameUser = new ArrayList<String>();
        listMailUser = new ArrayList<String>();
        listNameUserFriend = new ArrayList<String>();
        listMailUserFriend = new ArrayList<String>();
        rcvListFriend.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvListFriend.setLayoutManager(linearLayoutManager);
    }

    public void getDatafromFireBase() {
        //give Data from My_friend table
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
