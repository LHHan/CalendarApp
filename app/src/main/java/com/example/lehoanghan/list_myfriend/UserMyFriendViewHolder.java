package com.example.lehoanghan.list_myfriend;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.lehoanghan.appcalendar.R;


public class UserMyFriendViewHolder extends RecyclerView.ViewHolder {

    protected TextView TxtCardViewNameMyFriend, TxtCardViewMailMyFriend;

    public UserMyFriendViewHolder(View itemView) {
        super(itemView);
        TxtCardViewNameMyFriend=(TextView) itemView.findViewById(R.id.txtCarViewNameFriend);
        TxtCardViewMailMyFriend=(TextView) itemView.findViewById(R.id.txtCarViewMailFriend);
    }


}
