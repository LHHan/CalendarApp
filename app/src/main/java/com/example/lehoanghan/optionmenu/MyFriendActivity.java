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
public class MyFriendActivity extends Fragment {

    private Activity root;
    private View jview;
    private Firebase firebase;
    private List<UserFriend> ListMyFriend;
    private List<String> listMail,listName;
    private RecyclerView RvListMyFriend;
    private UserMyFriendRecyclerAdapter userMyFriendRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Bundle bundleGiveMailfromMenu;
    private String GetMail ,GetName;

    public MyFriendActivity(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root=getActivity();
        jview=inflater.inflate(R.layout.activity_my_friend, container, false);
        GiveUserfromChoose();
        Firebase.setAndroidContext(jview.getContext());
        firebase=new Firebase("https://appcalendar.firebaseio.com/");

        Init();

        RvListMyFriend.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(jview.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RvListMyFriend.setLayoutManager(linearLayoutManager);
        getDatafromFirebase();

        return jview;
    }

    public void getDatafromFirebase(){
        firebase.child("My_friend").child(GetMail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot Snapshort: dataSnapshot.getChildren())
                {
                    listMail.add(Snapshort.getKey().toString().replace("&","."));
                    listName.add(Snapshort.getValue().toString());
                }

                for(int i=0;i<listName.size();i++)
                {
                    ListMyFriend.add(new UserFriend(listName.get(i),listMail.get(i)));
                }

                userMyFriendRecyclerAdapter=new UserMyFriendRecyclerAdapter(ListMyFriend,GetMail,GetName);
                userMyFriendRecyclerAdapter.notifyDataSetChanged();
                RvListMyFriend.setAdapter(userMyFriendRecyclerAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void Init(){
        ListMyFriend=new ArrayList<UserFriend>();
        listMail=new ArrayList<String>();
        listName=new ArrayList<String>();
        RvListMyFriend=(RecyclerView) jview.findViewById(R.id.rvListUserMyFriend);

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
