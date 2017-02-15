package com.example.lehoanghan.list_findfriend;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lehoanghan.appcalendar.R;

public class UserFindFriendViewHolder extends RecyclerView.ViewHolder {

    protected TextView tvName;

    protected TextView tvMail;

    protected Button btnAdd;

    public UserFindFriendViewHolder(final View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.cardview_recycler_find_friend_tv_name);
        tvMail = (TextView) itemView.findViewById(R.id.cardview_recycler_find_friend_tv_mail);
        btnAdd = (Button) itemView.findViewById(R.id.cardview_recycler_find_friend_btn_add);
       /* btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(),"hello baby",Toast.LENGTH_LONG);
            }
        });*/
    }
}
