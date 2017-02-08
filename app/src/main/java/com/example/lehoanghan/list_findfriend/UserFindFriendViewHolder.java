package com.example.lehoanghan.list_findfriend;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lehoanghan.appcalendar.R;


public class UserFindFriendViewHolder extends RecyclerView.ViewHolder {

    protected TextView TxtName;
    protected TextView TxtMail;
    protected Button BtnAdd;

    public UserFindFriendViewHolder(final View itemView) {
        super(itemView);
        TxtName=(TextView) itemView.findViewById(R.id.txtCarViewName);
        TxtMail=(TextView) itemView.findViewById(R.id.txtCarViewMail);
        BtnAdd=(Button) itemView.findViewById(R.id.btnCarViewAdd);
       /* BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(),"hello baby",Toast.LENGTH_LONG);
            }
        });*/
    }
}
