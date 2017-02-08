package com.example.lehoanghan.list_myfriend;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lehoanghan.UserFriend;
import com.example.lehoanghan.appcalendar.R;

import java.util.List;


public class UserMyFriendRecyclerAdapter extends RecyclerView.Adapter<UserMyFriendViewHolder> {

    private View view;
    private List<UserFriend> listMyFriend;
    private String GetMail,GetName;
   // private Firebase firebase;

    public UserMyFriendRecyclerAdapter(List<UserFriend> listFriend, String Mail, String Name) {
        listMyFriend=listFriend;
        GetMail=Mail;
        GetName=Name;
    }



    @Override
    public UserMyFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recycler_myfriend, parent, false);
        //Firebase.setAndroidContext(view.getContext());
        //firebase=new Firebase("https://appcalendar.firebaseio.com/");
        return new UserMyFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserMyFriendViewHolder holder, int position) {
        UserFriend userMyFriend =listMyFriend.get(position);
        holder.TxtCardViewNameMyFriend.setText(userMyFriend.getName());
        holder.TxtCardViewMailMyFriend.setText(userMyFriend.getMail());
    }

    @Override
    public int getItemCount() {
        return listMyFriend.size();
    }


}
