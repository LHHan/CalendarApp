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

    private List<UserFriend> listUser;
    private Firebase firebase;
    private String getMail, getName;


    public UserFindFriendRecyclerAdapter(List<UserFriend> listUser, String mail, String name) {
        this.listUser = listUser;
        getMail = mail;
        getName = name;
    }


    @Override
    public UserFindFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recycler_find_friend, parent, false);
        Firebase.setAndroidContext(view.getContext());
        firebase = new Firebase("https://appcalendar.firebaseio.com/");
        return new UserFindFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserFindFriendViewHolder holder, int position) {
        final UserFriend userFriend = listUser.get(position);
        holder.tvName.setText(userFriend.getName());
        holder.tvMail.setText(userFriend.getMail());
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changeMail = userFriend.getMail().replace(".", "&");
                getMail = getMail.replace(".", "&");
                firebase.child("Add_friend").child(changeMail).child(getMail).setValue(getName);
                holder.btnAdd.setText("Wait for Accept");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

}
