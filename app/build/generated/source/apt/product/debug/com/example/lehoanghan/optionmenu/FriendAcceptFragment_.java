//
// DO NOT EDIT THIS FILE.
// Generated using AndroidAnnotations 4.0.0.
// 
// You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.example.lehoanghan.optionmenu;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.lehoanghan.appcalendar.R;
import org.androidannotations.api.builder.FragmentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class FriendAcceptFragment_
    extends com.example.lehoanghan.optionmenu.FriendAcceptFragment
    implements HasViews, OnViewChangedListener
{
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private View contentView_;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    @Override
    public View findViewById(int id) {
        if (contentView_ == null) {
            return null;
        }
        return contentView_.findViewById(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView_ == null) {
            contentView_ = inflater.inflate(R.layout.fragment_friend_accept, container, false);
        }
        return contentView_;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contentView_ = null;
        rcvListFriendAccept = null;
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static FriendAcceptFragment_.FragmentBuilder_ builder() {
        return new FriendAcceptFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        this.rcvListFriendAccept = ((RecyclerView) hasViews.findViewById(R.id.fragment_friend_accept_rcv_listUserFriendAccept));
        afterView();
    }

    public static class FragmentBuilder_
        extends FragmentBuilder<FriendAcceptFragment_.FragmentBuilder_, com.example.lehoanghan.optionmenu.FriendAcceptFragment>
    {

        @Override
        public com.example.lehoanghan.optionmenu.FriendAcceptFragment build() {
            FriendAcceptFragment_ fragment_ = new FriendAcceptFragment_();
            fragment_.setArguments(args);
            return fragment_;
        }
    }
}
