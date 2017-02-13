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

    Activity root;
    private View contentView;
    private Bundle bundleGiveMailfromMenu;
    private static String getName;
    private static String getMail;
    private Firebase firebase;
    private RecyclerView rcvListFriendAccept;
    private List<String> listMail, listName, listMailPre, listNamePre;
    private List<UserFriend> listUserAccept;
    private UserAcceptFriendRecyclerAdapter userAcceptFriendRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;

    public FriendAcceptActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GiveUserfromChoose();
        root = getActivity();
        contentView = inflater.inflate(R.layout.activity_friend_accept, container, false);
        Firebase.setAndroidContext(root);
        firebase = new Firebase("https://appcalendar.firebaseio.com/");
        Init();

        rcvListFriendAccept.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(contentView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvListFriendAccept.setLayoutManager(linearLayoutManager);
        //getDatafromFireBase();
        resetData();
        return contentView;
    }

//   /* public void getDatafromFireBase() {
//       firebase.child("Add_friend").child(getMail).addValueEventListener(new ValueEventListener() {
//           @Override
//           public void onDataChange(DataSnapshot dataSnapshot) {
//               for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
//                   String change = Snapshot.getKey().toString().replace("&", ".");
//                   listMail.add(change);
//                   listName.add(Snapshot.getValue().toString());
//               }
//               for (int i = 0; i < listName.size(); i++) {
//                   listUserAccept.add(new UserFriend(listName.get(i), listMail.get(i)));
//               }
//               userAcceptFriendRecyclerAdapter = new UserAcceptFriendRecyclerAdapter(listUserAccept, getMail, getName);
//               userAcceptFriendRecyclerAdapter.notifyDataSetChanged();
//               rcvListFriendAccept.setAdapter(userAcceptFriendRecyclerAdapter);
//           }
//
//           @Override
//           public void onCancelled(FirebaseError firebaseError) {
//
//           }
//       });
//    }*/

    public void resetData() {
        //final List<String> listMailPre, listNamePre;
        //listMailPre=new ArrayList<>();
        //listNamePre=new ArrayList<>();
        firebase.child("My_friend").child(getMail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    //String change = Snapshot.getKey().toString().replace("&", ".");
                    listMailPre.add(Snapshot.getKey());
                    listNamePre.add(Snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        firebase.child("Add_friend").child(getMail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    int check = 0;
                    for (String mail : listMailPre) {
                        if (Snapshot.getKey().compareTo(mail) == 0) {
                            check = 1;
                            break;
                        }
                    }
                    if (check == 0) {
                        listMail.add(Snapshot.getKey().toString().replace("&", "."));
                        listName.add(Snapshot.getValue().toString());
                    }
                }
                for (int i = 0; i < listName.size(); i++) {
                    listUserAccept.add(new UserFriend(listName.get(i), listMail.get(i)));
                }

                userAcceptFriendRecyclerAdapter = new UserAcceptFriendRecyclerAdapter(listUserAccept, getMail, getName);
                userAcceptFriendRecyclerAdapter.notifyDataSetChanged();
                rcvListFriendAccept.setAdapter(userAcceptFriendRecyclerAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void Init() {
        rcvListFriendAccept = (RecyclerView) contentView.findViewById(R.id.activity_friend_accept_rcv_listUserFriendAccept);
        listUserAccept = new ArrayList<UserFriend>();
        listName = new ArrayList<String>();
        listMail = new ArrayList<String>();
        listNamePre = new ArrayList<String>();
        listMailPre = new ArrayList<String>();
    }

    public void GiveUserfromChoose() {
        bundleGiveMailfromMenu = this.getArguments();
        if (bundleGiveMailfromMenu != null) {
            getMail = bundleGiveMailfromMenu.getString("MailforFindFriend");
            //getMail=getMail.replace("&", ".");
            getName = bundleGiveMailfromMenu.getString("NameforFindFriend");
            getName = getName.toLowerCase();
        }
    }

}
