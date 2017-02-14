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


    private int galleryItem;
    private Context context;
    EventValue eventValue;
    private String nameUser, mailUser;


    private ArrayList<Bitmap> imageArray = new ArrayList<>();

    public GalleryAdapter(Context C, ArrayList<Bitmap> imageArray, EventValue eventValue, String Mail, String Name) {
        context = C;
        this.eventValue = eventValue;
        nameUser = Name;
        mailUser = Mail;
        for (Bitmap b : imageArray) {
            this.imageArray.add(b);
        }
        TypedArray attr = context.obtainStyledAttributes(R.styleable.GalleryActivity);
        galleryItem = attr.getResourceId(R.styleable.GalleryActivity_android_galleryItemBackground, 0);
        attr.recycle();
    }

    @Override
    public int getCount() {
        return imageArray.size();
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
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new Gallery.LayoutParams(450, 450));
            imageView.setBackgroundResource(galleryItem);
        }

        // Set the image for the imageView
        imageView.setImageBitmap(imageArray.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PictureActivity.class);
                intent.putExtra("bitmapofposition", imageArray.get(position));
                intent.putExtra("position", position + "");
                intent.putExtra("EventValue", eventValue);
                intent.putExtra("NameUser", nameUser);
                intent.putExtra("MailUser", mailUser);
                context.startActivity(intent);
            }
        });

        return imageView;
    }
}
