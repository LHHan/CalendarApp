package com.example.lehoanghan.list_myfriend;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.lehoanghan.appcalendar.R;

public class UserMyFriendViewHolder extends RecyclerView.ViewHolder {

    protected TextView tvCardViewNameMyFriend;

    protected TextView tvCardViewMailMyFriend;

    public UserMyFriendViewHolder(View itemView) {
        super(itemView);
        tvCardViewNameMyFriend =
                (TextView) itemView.findViewById(R.id.cardview_recycler_my_friend_tv_name);
        tvCardViewMailMyFriend =
                (TextView) itemView.findViewById(R.id.cardview_recycler_my_friend_tv_mail);
    }

}
