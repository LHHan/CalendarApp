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
public class Custom_Adapter_Comment extends BaseAdapter {
    private ArrayList<String>LComment;
    private Context context;
    private static LayoutInflater inflater=null;
    Custom_Adapter_Comment(Context context,ArrayList<String>Comment){
        LComment=new ArrayList<String>();

        for(String temp:Comment)
        {
            LComment.add(temp);
        }
        this.context=context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return LComment.size();
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
        rowView = inflater.inflate(R.layout.adapter_comment, null);
        EmojiconTextView emojiconTextView=(EmojiconTextView) rowView.findViewById(R.id.Emoji_Txt);
        emojiconTextView.setText(LComment.get(position));
        return rowView;
    }
}
