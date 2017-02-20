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

    private View contentView;

    private List<UserFriend> listUser;

    private Firebase aFirebase;

    private String getMail;

    private String getName;

    public UserFindFriendRecyclerAdapter(List<UserFriend> listUser, String mail, String name) {
        this.listUser = listUser;
        getMail = mail;
        getName = name;
    }

    @Override
    public UserFindFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        contentView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_recycler_find_friend, parent, false);
        Firebase.setAndroidContext(contentView.getContext());
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        return new UserFindFriendViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(final UserFindFriendViewHolder holder, int position) {
        final UserFriend USERFRIEND = listUser.get(position);
        holder.tvName.setText(USERFRIEND.getName());
        holder.tvMail.setText(USERFRIEND.getMail());
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changeMail = USERFRIEND.getMail().replace(".", "&");
                getMail = getMail.replace(".", "&");
                aFirebase.child("Add_friend").child(changeMail).child(getMail).setValue(getName);
                holder.btnAdd.setText("Wait for Accept");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

}
