package com.example.lehoanghan.list_oldevent;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.example.lehoanghan.EventValue;
import com.example.lehoanghan.appcalendar.R;

import java.util.ArrayList;

/**
 * Created by lehoanghan on 6/7/2016.
 */
public class GalleryAdapter extends BaseAdapter {


    private int mGalleryItem;
    private Context mContext;
    EventValue mPassValue;
    String mNameUser, mMailUser;


    private ArrayList<Bitmap> ImageArray = new ArrayList<>();

    public GalleryAdapter(Context C, ArrayList<Bitmap> imageArray, EventValue eventValue, String Mail, String Name) {
        mContext = C;
        mPassValue=eventValue;
//        Log.i("NameEvent",mValue.getNameEvent());
//        Log.i("NameEvent",mValue.getDateFrom());
//        Log.i("NameEvent",mValue.getTimeFrom());
//        Log.i("NameEvent",mValue.getDateTo());
//        Log.i("NameEvent",mValue.getTimeTo());
//        Log.i("NameEvent",mValue.getDescription());
//        Log.i("NameEvent",mValue.getPlace());
//        Log.i("NameEvent",mValue.getFriendInvite());
        mNameUser=Name;
        mMailUser=Mail;
        for (Bitmap b : imageArray) {
            ImageArray.add(b);
        }
        TypedArray attr = mContext.obtainStyledAttributes(R.styleable.GalleryActivity);
        mGalleryItem = attr.getResourceId(R.styleable.GalleryActivity_android_galleryItemBackground, 0);
        attr.recycle();
    }

    @Override
    public int getCount() {
        return ImageArray.size();
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
    public View getView(final int position, final View convertView, ViewGroup parent) {
        ImageView imageView = (ImageView) convertView;

        // If convertView is not recycled set it up now
        if (null == imageView) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new Gallery.LayoutParams(450, 450));
            imageView.setBackgroundResource(mGalleryItem);

        }

        // Set the image for the imageView
        imageView.setImageBitmap(ImageArray.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,Picture.class);
                intent.putExtra("bitmapofposition",ImageArray.get(position));
                intent.putExtra("position", position+"");
                intent.putExtra("EventValue",mPassValue);
                intent.putExtra("NameUser",mNameUser);
                intent.putExtra("MailUser",mMailUser);
                mContext.startActivity(intent);
            }
        });

        return imageView;
    }
}
