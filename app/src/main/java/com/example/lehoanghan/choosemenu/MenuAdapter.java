package com.example.lehoanghan.choosemenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lehoanghan.appcalendar.R;

import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {

    private final int itemTitle = 0;

    private final int itemBody = 1;

    private Context adapterContext;

    private ArrayList<MenuItem> arrayMenuItem;

    public MenuAdapter(ArrayList<MenuItem> array_menuitem, Context context) {
        this.arrayMenuItem = array_menuitem;
        this.adapterContext = context;
    }

    @Override
    public int getCount() {
        return arrayMenuItem.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayMenuItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = arrayMenuItem.get(position).getItemType();

        if (type == itemTitle) {
            if (convertView == null) {
                LayoutInflater inflater =
                        (LayoutInflater) adapterContext.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.menu_head, null);
                TextView txtTile = (TextView) convertView.findViewById(R.id.menu_head_tv_title);
                txtTile.setText(arrayMenuItem.get(position).getTitle());
            }
        } else if (type == itemBody) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) adapterContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.menu_body, null);
                TextView tvTile = (TextView) convertView.findViewById(R.id.menu_body_tv_body);
                tvTile.setText(arrayMenuItem.get(position).getTitle());
                ImageView ivBody = (ImageView) convertView.findViewById(R.id.menu_body_iv_icon);
                ivBody.setImageResource(arrayMenuItem.get(position).getIcon());
            }
        }
        return convertView;
    }
}