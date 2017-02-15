package com.example.lehoanghan.list_myfriend;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lehoanghan.UserFriend;
import com.example.lehoanghan.appcalendar.R;

import java.util.List;

public class userMyFriendRecyclerAdapter extends RecyclerView.Adapter<UserMyFriendViewHolder> {
    private View contentView;

    private List<UserFriend> listMyFriend;

    private String getMail;

    private String getName;

    // private Firebase firebase;

    public userMyFriendRecyclerAdapter(List<UserFriend> listFriend, String Mail, String Name) {
        listMyFriend = listFriend;
        getMail = Mail;
        getName = Name;
    }

    @Override
    public UserMyFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        contentView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_recycler_my_friend, parent, false);
        //Firebase.setAndroidContext(contentView.getContext());
        //firebase=new Firebase("https://appcalendar.firebaseio.com/");
        return new UserMyFriendViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(UserMyFriendViewHolder holder, int position) {
        UserFriend userMyFriend = listMyFriend.get(position);
        holder.tvCardViewNameMyFriend.setText(userMyFriend.getName());
        holder.tvCardViewMailMyFriend.setText(userMyFriend.getMail());
    }

    @Override
    public int getItemCount() {
        return listMyFriend.size();
    }

}
