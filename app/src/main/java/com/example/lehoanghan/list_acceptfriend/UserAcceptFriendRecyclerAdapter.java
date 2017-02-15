package com.example.lehoanghan.list_acceptfriend;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lehoanghan.UserFriend;
import com.example.lehoanghan.appcalendar.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

public class UserAcceptFriendRecyclerAdapter extends
        RecyclerView.Adapter<UserAcceptFriendViewHolder> {

    private View contentView;

    private List<UserFriend> listUserAccept;

    private Firebase aFirebase;

    private String getMail;

    private String getName;

    private int intTest = 0;

    public UserAcceptFriendRecyclerAdapter(List<UserFriend> listUser, String mail, String name) {
        listUserAccept = listUser;
        getMail = mail;
        getName = name;
    }

    @Override
    public UserAcceptFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        contentView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_recycler_accept_friend, parent, false);
        Firebase.setAndroidContext(contentView.getContext());
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        return new UserAcceptFriendViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(final UserAcceptFriendViewHolder holder, final int position) {
        final UserFriend USERFINDFRIEND = listUserAccept.get(position);
        holder.tvUserNameAcceptFriend.setText(USERFINDFRIEND.getName());
        holder.tvUserMailAcceptFriend.setText(USERFINDFRIEND.getMail());
        holder.btnUserAcceptFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String CHANGE = USERFINDFRIEND.getMail().replace(".", "&");
                aFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        aFirebase.child("My_friend").child(getMail).child(CHANGE)
                                .setValue(USERFINDFRIEND.getName());
                        aFirebase.child("My_friend").child(CHANGE).child(getMail).setValue(getName);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                listUserAccept.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, listUserAccept.size());
                //notifyItemChanged(position);
                // if(holder.btnUserAcceptFriend.getVisibility()==contentView.VISIBLE)
                holder.btnUserAcceptFriend.setVisibility(contentView.INVISIBLE);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listUserAccept.size();
    }

}
