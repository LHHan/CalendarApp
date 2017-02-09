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

    private final int Item_Title = 0;
    private final int Item_Body = 1;
    private Context context;
    private ArrayList<MenuItem> array_menuitem;


    public MenuAdapter(ArrayList<MenuItem> array_menuitem, Context context) {
        this.array_menuitem = array_menuitem;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array_menuitem.size();
    }

    @Override
    public Object getItem(int position) {
        return array_menuitem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = array_menuitem.get(position).getItem_Type();

        if (type == Item_Title) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.menu_head, null);
                TextView txtTile = (TextView) convertView.findViewById(R.id.txtTitle);
                txtTile.setText(array_menuitem.get(position).getTitle());
            }
        } else if (type == Item_Body) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.menu_body, null);
                TextView txtTile = (TextView) convertView.findViewById(R.id.txtbody);
                txtTile.setText(array_menuitem.get(position).getTitle());
                ImageView imgBody = (ImageView) convertView.findViewById(R.id.img_icon);
                imgBody.setImageResource(array_menuitem.get(position).getIcon());
            }
        }
        return convertView;
    }
}