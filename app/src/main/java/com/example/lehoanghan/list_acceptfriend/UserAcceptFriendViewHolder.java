package com.example.lehoanghan.list_acceptfriend;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lehoanghan.appcalendar.R;


public class UserAcceptFriendViewHolder extends RecyclerView.ViewHolder {
    protected TextView TxtUserNameAcceptFriend, TxtUserMailAcceptFriend;
    protected Button BtnUserAcceptFriend;


    public UserAcceptFriendViewHolder(View itemView) {
        super(itemView);
        TxtUserNameAcceptFriend=(TextView) itemView.findViewById(R.id.txtCarViewNameAccept);
        TxtUserMailAcceptFriend=(TextView) itemView.findViewById(R.id.txtCarViewMailAccept);
        BtnUserAcceptFriend=(Button) itemView.findViewById(R.id.btnCarViewAccept);
    }
}
