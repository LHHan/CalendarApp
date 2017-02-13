package com.example.lehoanghan.list_acceptfriend;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lehoanghan.appcalendar.R;


public class UserAcceptFriendViewHolder extends RecyclerView.ViewHolder {
    protected TextView tvUserNameAcceptFriend, tvUserMailAcceptFriend;
    protected Button btnUserAcceptFriend;


    public UserAcceptFriendViewHolder(View itemView) {
        super(itemView);
        tvUserNameAcceptFriend =(TextView) itemView.findViewById(R.id.cardview_recycler_accept_friend_tv_name);
        tvUserMailAcceptFriend =(TextView) itemView.findViewById(R.id.cardview_recycler_accept_friend_tv_mail);
        btnUserAcceptFriend =(Button) itemView.findViewById(R.id.cardview_recycler_accept_friend_btn_accept);
    }
}
