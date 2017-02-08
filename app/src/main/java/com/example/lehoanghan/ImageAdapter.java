package com.example.lehoanghan;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lehoanghan.appcalendar.R;

import java.util.ArrayList;

/**
 * Created by lehoanghan on 5/22/2016.
 */
public class ImageAdapter extends BaseAdapter {

    private int mGalleryItem;
    private Context mContext;
    private String listFriendInvite;
    private LayoutInflater inflater = null;
    private String[] friend;


    private ArrayList<Bitmap> ImageArray = new ArrayList<>();

    public ImageAdapter(Context C, String Friend, ArrayList<Bitmap> imageArray) {
        //inflater=LayoutInflater.from(C);
        mContext = C;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listFriendInvite = Friend;
        friend = listFriendInvite.split(",");
        for (Bitmap b : imageArray) {
            ImageArray.add(b);
        }
        TypedArray attr = mContext.obtainStyledAttributes(R.styleable.GalleryActivity);
        mGalleryItem = attr.getResourceId(R.styleable.GalleryActivity_android_galleryItemBackground, 0);
        attr.recycle();
    }

    @Override
    public int getCount() {
        return friend.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class ViewHolder {
        public TextView TxtGallery;
        public ImageView ImgGallery;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        View vi = convertView;
//            if (parent != null)
//                parent.removeView(convertView);

        if (vi == null) {
            vi = inflater.inflate(R.layout.custom_gallery_friendinvite, parent, false);
            vi.setLayoutParams(new Gallery.LayoutParams(500, 700));
            holder = new ViewHolder();
            holder.ImgGallery = (ImageView) vi.findViewById(R.id.imgGallery);
            holder.TxtGallery = (TextView) vi.findViewById(R.id.txtGallery);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();
        holder.ImgGallery.setImageBitmap(ImageArray.get(position));
        holder.ImgGallery.setLayoutParams(new LinearLayout.LayoutParams(500, 500));
        holder.ImgGallery.setBackgroundResource(mGalleryItem);
        holder.TxtGallery.setText(friend[position].trim().replace("&", "."));
        holder.TxtGallery.setTag(friend[position].trim().replace("&", "."));
        holder.TxtGallery.setGravity(Gravity.CENTER);
        return vi;
    }
}
