package com.example.lehoanghan.list_findfriend;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lehoanghan.UserFriend;
import com.example.lehoanghan.appcalendar.R;
import com.firebase.client.Firebase;

import java.util.List;


public class UserFindFriendRecyclerAdapter extends RecyclerView.Adapter<UserFindFriendViewHolder> {


    View view;

    private List<UserFriend> ListUser;
    private Firebase firebase;
    private String GetMail,GetName;


    public UserFindFriendRecyclerAdapter(List<UserFriend> listUser, String mail, String name)
    {
        ListUser=listUser;
        GetMail=mail;
        GetName=name;
    }


    @Override
    public UserFindFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recycler_findfriend, parent, false);
        Firebase.setAndroidContext(view.getContext());
        firebase=new Firebase("https://appcalendar.firebaseio.com/");
        return new UserFindFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserFindFriendViewHolder holder, int position) {
        final UserFriend userFriend =ListUser.get(position);
        holder.TxtName.setText(userFriend.getName());
        holder.TxtMail.setText(userFriend.getMail());
        holder.BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changeMail= userFriend.getMail().replace(".","&");
                GetMail=GetMail.replace(".","&");
                firebase.child("Add_friend").child(changeMail).child(GetMail).setValue(GetName);
                holder.BtnAdd.setText("Wait for Accept");
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListUser.size();
    }

}
