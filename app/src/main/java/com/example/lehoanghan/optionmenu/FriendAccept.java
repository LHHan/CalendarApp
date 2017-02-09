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
public class FriendAccept extends Fragment {

    Activity root;
    private View jview;
    private Bundle bundleGiveMailfromMenu;
    private static String GetName;
    private static String GetMail;
    private Firebase firebase;
    private RecyclerView RvListFriendAccept;
    private List<String> listMail, listName, listMailPre, listNamePre;
    private List<UserFriend> ListUserAccept;
    private UserAcceptFriendRecyclerAdapter userAcceptFriendRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;


    public FriendAccept(){}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GiveUserfromChoose();
        root=getActivity();
        jview =inflater.inflate(R.layout.screen_friendaccept, container, false);
        Firebase.setAndroidContext(root);
        firebase=new Firebase("https://appcalendar.firebaseio.com/");
        Init();

        RvListFriendAccept.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(jview.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RvListFriendAccept.setLayoutManager(linearLayoutManager);
        //getDatafromFireBase();
         resetData();
        return jview;
    }

//   /* public void getDatafromFireBase() {
//       firebase.child("Add_friend").child(GetMail).addValueEventListener(new ValueEventListener() {
//           @Override
//           public void onDataChange(DataSnapshot dataSnapshot) {
//               for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
//                   String change = Snapshot.getKey().toString().replace("&", ".");
//                   listMail.add(change);
//                   listName.add(Snapshot.getValue().toString());
//               }
//               for (int i = 0; i < listName.size(); i++) {
//                   ListUserAccept.add(new UserFriend(listName.get(i), listMail.get(i)));
//               }
//               userAcceptFriendRecyclerAdapter = new UserAcceptFriendRecyclerAdapter(ListUserAccept, GetMail, GetName);
//               userAcceptFriendRecyclerAdapter.notifyDataSetChanged();
//               RvListFriendAccept.setAdapter(userAcceptFriendRecyclerAdapter);
//           }
//
//           @Override
//           public void onCancelled(FirebaseError firebaseError) {
//
//           }
//       });
//    }*/

    public void resetData(){
        //final List<String> listMailPre, listNamePre;
        //listMailPre=new ArrayList<>();
        //listNamePre=new ArrayList<>();
        firebase.child("My_friend").child(GetMail).addValueEventListener(new ValueEventListener() {
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
        firebase.child("Add_friend").child(GetMail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    int Check=0;
                        for (String mail : listMailPre) {
                            if (Snapshot.getKey().compareTo(mail) == 0) {
                                Check = 1;
                                break;
                            }
                        }
                        if (Check == 0) {
                            listMail.add(Snapshot.getKey().toString().replace("&", "."));
                            listName.add(Snapshot.getValue().toString());
                        }
                }
                for (int i = 0; i < listName.size(); i++) {
                    ListUserAccept.add(new UserFriend(listName.get(i), listMail.get(i)));
                }

                userAcceptFriendRecyclerAdapter = new UserAcceptFriendRecyclerAdapter(ListUserAccept, GetMail, GetName);
                userAcceptFriendRecyclerAdapter.notifyDataSetChanged();
                RvListFriendAccept.setAdapter(userAcceptFriendRecyclerAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void Init(){
        RvListFriendAccept=(RecyclerView) jview.findViewById(R.id.rvListUserFriendAccept);
        ListUserAccept= new ArrayList<UserFriend>();
        listName=new ArrayList<String>();
        listMail=new ArrayList<String>();
        listNamePre=new ArrayList<String>();
        listMailPre=new ArrayList<String>();
    }

    public void GiveUserfromChoose(){
        bundleGiveMailfromMenu=this.getArguments();
        if(bundleGiveMailfromMenu!=null) {
            GetMail = bundleGiveMailfromMenu.getString("MailforFindFriend");
            //GetMail=GetMail.replace("&", ".");
            GetName=bundleGiveMailfromMenu.getString("NameforFindFriend");
            GetName=GetName.toLowerCase();
        }
    }

}
