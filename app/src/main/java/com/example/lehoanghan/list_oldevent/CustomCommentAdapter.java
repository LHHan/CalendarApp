package com.example.lehoanghan.list_oldevent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.lehoanghan.appcalendar.R;
import com.rockerhieu.emojicon.EmojiconTextView;

import java.util.ArrayList;

/**
 * Created by lehoanghan on 6/24/2016.
 */
public class CustomCommentAdapter extends BaseAdapter {
    private static LayoutInflater sLayoutInflater = null;

    private ArrayList<String> listComment;

    private Context adapterContext;

    CustomCommentAdapter(Context context, ArrayList<String> Comment) {
        listComment = new ArrayList<String>();
        for (String temp : Comment) {
            listComment.add(temp);
        }
        this.adapterContext = context;
        sLayoutInflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return listComment.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = sLayoutInflater.inflate(R.layout.adapter_comment, null);
        EmojiconTextView emojiconTextView =
                (EmojiconTextView) rowView.findViewById(R.id.adapter_comment_eitv_emojicon);
        emojiconTextView.setText(listComment.get(position));
        return rowView;
    }
}
