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
public class FindFriend extends Fragment {

    Activity root;
    View jview;
    public FindFriend(){}
    private Firebase firebase;
    private SearchView SvFindFriend;
    private RecyclerView RvListFriend;
    private LinearLayoutManager linearLayoutManager;
    private UserFindFriendRecyclerAdapter userFindFriendRecyclerAdapter;
    private List<UserFriend> listUserFriend;
    private List<String>listNameUser, listNameUserFriend;//lay tu value cua ca key Mail.
    private List<String>listMailUser, listMailUserFriend;//lay tu cac key thuoc value cua User
    private String GetName;
    private String GetMail;
    private Bundle bundleGiveMailfromMenu;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root=getActivity();
        jview=inflater.inflate(R.layout.screen_findfriend, container, false);
        GiveUserfromChoose();

        Firebase.setAndroidContext(root);
        firebase=new Firebase("https://appcalendar.firebaseio.com/");

        RvListFriend=(RecyclerView) jview.findViewById(R.id.rvListUserFriend);
        SvFindFriend=(SearchView) jview.findViewById(R.id.svFindName);

        listUserFriend =new ArrayList<UserFriend>();
        listNameUser=new ArrayList<String>();
        listMailUser=new ArrayList<String>();
        listNameUserFriend=new ArrayList<String>();
        listMailUserFriend=new ArrayList<String>();

        SvFindFriend.setVisibility(View.INVISIBLE);

        RvListFriend.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(jview.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RvListFriend.setLayoutManager(linearLayoutManager);
        GetDatafromFireBase();

        return jview;
    }

    public void GetDatafromFireBase()
    {
        //give data from My_friend table
        firebase.child("My_friend").child(GetMail.replace(".","&")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    listMailUserFriend.add(Snapshot.getKey());
                    listNameUserFriend.add(Snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        firebase.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // compare user in User with user in my_friend
                for(DataSnapshot Snapshot:dataSnapshot.getChildren())
                {
                    int Check=0;
                    if(Snapshot.getKey().compareTo(GetMail.replace(".","&"))!=0) {
                        for (String mail : listMailUserFriend) {
                            if (Snapshot.getKey().compareTo(mail) == 0) {
                                Check = 1;
                                break;
                            }
                        }
                        if (Check == 0) {
                            listMailUser.add(Snapshot.getKey().toString().replace("&", "."));
                            listNameUser.add(Snapshot.getValue().toString());
                        }
                    }
                }

                for (int i = 0; i < listNameUser.size(); i++) {
                    listUserFriend.add(new UserFriend(listNameUser.get(i), listMailUser.get(i)));
                }

                userFindFriendRecyclerAdapter = new UserFindFriendRecyclerAdapter(listUserFriend, GetMail, GetName);
                userFindFriendRecyclerAdapter.notifyDataSetChanged();
                RvListFriend.setAdapter(userFindFriendRecyclerAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
       // return listUserFriend;
    }

    public void GiveUserfromChoose(){
        bundleGiveMailfromMenu=this.getArguments();
        if(bundleGiveMailfromMenu!=null) {
            GetMail = bundleGiveMailfromMenu.getString("MailforFindFriend");
            GetMail=GetMail.replace("&", ".");
            GetName=bundleGiveMailfromMenu.getString("NameforFindFriend");
            GetName=GetName.toLowerCase();
        }
    }

}
