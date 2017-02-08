package com.example.lehoanghan.list_oldevent;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;

import com.example.lehoanghan.EventValue;
import com.example.lehoanghan.ImageAdapter;
import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.list_newevent.AlarmNotificationReceiver;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by lehoanghan on 5/22/2016.
 */
public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    private ArrayList<EventValue> mDataSet;
    private ArrayList<Integer> mDataSetTypes;
    private String MailUser, NameUser;

    public static final int TiTle = 0;
    public static final int Data = 1;
    private int Type;
    private Context context;

    private Firebase firebase;

    public EventRecyclerAdapter(ArrayList<EventValue> DataSet, ArrayList<Integer> DataSetType, String Mail,int type) {
        mDataSet = DataSet;
        mDataSetTypes = DataSetType;
        MailUser = Mail;
        Type=type;
    }
    public EventRecyclerAdapter(ArrayList<EventValue> DataSet, ArrayList<Integer> DataSetType, String Mail,String Name,int type) {
        mDataSet = DataSet;
        mDataSetTypes = DataSetType;
        MailUser = Mail;
        NameUser=Name;
        Type=type;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class TitleAboutDate extends ViewHolder {

        TextView TxtTitle;

        public TitleAboutDate(View itemView) {

            super(itemView);
            TxtTitle = (TextView) itemView.findViewById(R.id.txtTitleEvent);
        }
    }

    public class EventData extends ViewHolder {
        TextView TxtTimeEvent, TxtNameEventValue, TxtDateStartValue, TxtTimeStartValue, TxtDateEndValue, TxtTimeEndValue,
                TxtDecriptionValue, TxtPlaceValue, TxtFriendEventValue, TxtAlarmValue, TxtRepeatValue;
        Button BtnAgree, BtnRefuse, BtnGallery,BtnDetail;

        public EventData(View itemView) {
            super(itemView);
            TxtTimeEvent = (TextView) itemView.findViewById(R.id.txtTimeEvent);
            TxtNameEventValue = (TextView) itemView.findViewById(R.id.txtNameEventValue);
            if(Type!=3) {
                TxtDateStartValue = (TextView) itemView.findViewById(R.id.txtDateStartValue);
                TxtTimeStartValue = (TextView) itemView.findViewById(R.id.txtTimeStartValue);
                TxtDateEndValue = (TextView) itemView.findViewById(R.id.txtDateEndValue);
                TxtTimeEndValue = (TextView) itemView.findViewById(R.id.txtTimeEndValue);
                TxtDecriptionValue = (TextView) itemView.findViewById(R.id.txtDecriptionValue);
                TxtPlaceValue = (TextView) itemView.findViewById(R.id.txtPlaceValue);
                TxtFriendEventValue = (TextView) itemView.findViewById(R.id.txtFriendInviteValue);
                BtnGallery = (Button) itemView.findViewById(R.id.btngallery);
                TxtAlarmValue = (TextView) itemView.findViewById(R.id.txtAlarmValue);
                TxtRepeatValue = (TextView) itemView.findViewById(R.id.txtRepeatValue);
                if (Type == 1) {
                    BtnAgree = (Button) itemView.findViewById(R.id.btnAgree);
                    BtnRefuse = (Button) itemView.findViewById(R.id.btnRefuse);
                }
            }
            else
            if(Type==3){
                BtnDetail=(Button) itemView.findViewById(R.id.btnDetail);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        context=parent.getContext();
        if (viewType == TiTle) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recycler_title, parent, false);
            Firebase.setAndroidContext(v.getContext());
            firebase = new Firebase("https://appcalendar.firebaseio.com/");
            return new TitleAboutDate(v);
        } else
        //if(viewType==Data)
        {
            if(Type==1)
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recycler_newevent, parent, false);
            else if(Type==2)
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recycler_myevent, parent, false);
            else
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recycler_oldevent, parent, false);
            Firebase.setAndroidContext(v.getContext());
            firebase = new Firebase("https://appcalendar.firebaseio.com/");
            return new EventData(v);
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder.getItemViewType() == TiTle) {
            TitleAboutDate viewholder = (TitleAboutDate) holder;
            viewholder.TxtTitle.setText(mDataSet.get(position).getDateFrom());
        } else {
            final EventData viewholder = (EventData) holder;
            viewholder.TxtTimeEvent.setText(mDataSet.get(position).getTimeFrom());
            viewholder.TxtNameEventValue.setText(mDataSet.get(position).getNameEvent());
            if(Type!=3) {
                viewholder.TxtDateStartValue.setText(mDataSet.get(position).getDateFrom());
                viewholder.TxtTimeStartValue.setText(mDataSet.get(position).getTimeFrom());
                viewholder.TxtDateEndValue.setText(mDataSet.get(position).getDateTo());
                viewholder.TxtTimeEndValue.setText(mDataSet.get(position).getTimeTo());
                viewholder.TxtDecriptionValue.setText(mDataSet.get(position).getDescription());
                viewholder.TxtPlaceValue.setText(mDataSet.get(position).getPlace());
                viewholder.TxtFriendEventValue.setText(mDataSet.get(position).getFriendInvite().replace("&", "."));
                viewholder.TxtAlarmValue.setText(mDataSet.get(position).getAlarm());
                viewholder.TxtRepeatValue.setText(mDataSet.get(position).getRepeat());
                final ArrayList <Bitmap> ImageArray=new ArrayList<>();
                final String []Friend=mDataSet.get(position).getFriendInvite().toString().split(",");

                viewholder.BtnGallery.setVisibility(View.INVISIBLE);
                for (int i = 0; i <Friend.length; i++) {
                    firebase.child("Avata").child(Friend[i]).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            byte[] getimage = Base64.decode(dataSnapshot.getValue().toString(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(getimage, 0, getimage.length);
                            ImageArray.add(bitmap);
                            if(ImageArray.size()==Friend.length)
                            {
                                //viewholder.TxtFriendEventValue.setVisibility(View.INVISIBLE);
                                viewholder.BtnGallery.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });}
                viewholder.BtnGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context mContext = v.getContext();
                        AlertDialog.Builder builder;
                        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        View view = inflater.inflate(R.layout.gallery_friendinvite, (ViewGroup) v.findViewById(R.id.LLGallery));
                        Gallery gallery = (Gallery) view.findViewById(R.id.glrFriendInvite);
                        gallery.setAdapter(new ImageAdapter(v.getContext(), mDataSet.get(position).getFriendInvite().toString(), ImageArray));
                        builder = new AlertDialog.Builder(v.getContext());
                        builder.setView(view);
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        Dialog dialog = builder.create();
                        dialog.setTitle("Friend Invite");
                        dialog.show();
                    }
                });
                if (Type == 1) {
                    viewholder.BtnAgree.setVisibility(View.VISIBLE);
                    viewholder.BtnRefuse.setVisibility(View.VISIBLE);

                    viewholder.BtnAgree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlarmManager mAlarmManager;
                            Intent mNotificationReceiverIntent;
                            PendingIntent mNotificationReceiverPendingIntent;
                            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            mNotificationReceiverIntent = new Intent(context.getApplicationContext(), AlarmNotificationReceiver.class);
                            mNotificationReceiverIntent.putExtra("Name",mDataSet.get(position).getNameEvent());
                            mNotificationReceiverIntent.putExtra("Date",mDataSet.get(position).getDateFrom());
                            mNotificationReceiverIntent.putExtra("Time",mDataSet.get(position).getTimeFrom());
                            mNotificationReceiverIntent.putExtra("Place",mDataSet.get(position).getPlace());
                            mNotificationReceiverIntent.putExtra("NameUser",NameUser);
                            mNotificationReceiverIntent.putExtra("MailUser",MailUser);
                            mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                                    context.getApplicationContext(), 0, mNotificationReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            float timeAlarm = 0;
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:m");
                            try {
                                Date date = format.parse(mDataSet.get(position).getDateFrom() + " " + mDataSet.get(position).getTimeFrom());
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                timeAlarm = date.getTime()-(Integer.parseInt(mDataSet.get(position).getAlarm())*60*1000);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(timeAlarm!=0){
                                mAlarmManager.set(AlarmManager.RTC_WAKEUP, (long) timeAlarm,mNotificationReceiverPendingIntent);
                            }

//                            mLoggerReceiverIntent = new Intent(context.getApplicationContext(),
//                                    RingtonePlayingService.class);
//
//                            mLoggerReceiverPendingIntent = PendingIntent.getBroadcast(
//                                    context.getApplicationContext(), 0, mLoggerReceiverIntent, 0);


                            Map<String, String> eventValue = new Hashtable<String, String>();
                            eventValue.put("NameEvent", mDataSet.get(position).getNameEvent());
                            eventValue.put("DateFrom", mDataSet.get(position).getDateFrom());
                            eventValue.put("TimeFrom", mDataSet.get(position).getTimeFrom());
                            eventValue.put("DateTo", mDataSet.get(position).getDateTo());
                            eventValue.put("TimeTo", mDataSet.get(position).getTimeTo());
                            eventValue.put("Description", mDataSet.get(position).getDescription());
                            eventValue.put("Place", mDataSet.get(position).getPlace());
                            eventValue.put("FriendInvite", mDataSet.get(position).getFriendInvite());
                            eventValue.put("Alarm", mDataSet.get(position).getAlarm());
                            eventValue.put("Repeat", mDataSet.get(position).getRepeat());
                            firebase.child("Event").child(MailUser).child("Accept_Event").push().setValue(eventValue);
                            mDataSet.remove(position);
                            notifyDataSetChanged();
                            notifyItemRangeChanged(position, mDataSet.size());
                            viewholder.BtnRefuse.setVisibility(View.INVISIBLE);
                            viewholder.BtnAgree.setVisibility(View.INVISIBLE);
                        }
                    });
                    viewholder.BtnRefuse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String FriendInvite = "";
                            String[] friend = mDataSet.get(position).getFriendInvite().split(",");
                            ArrayList<String> friend1 = new ArrayList<String>();
                            for (int i = 0; i < friend.length; i++) {
                                friend1.add(new String(friend[i]));
                            }
                            for (int i = 0; i < friend1.size(); i++) {
                                if (friend1.get(i).trim().replace(".", "&").compareTo(MailUser) == 0)
                                    friend1.remove(i);
                            }
                            for (int i = 0; i < friend1.size(); i++) {
                                if (i == (friend1.size() - 1))
                                    FriendInvite += friend1.get(i);
                                else
                                    FriendInvite += friend1.get(i) + ", ";
                            }
                            Map<String, String> eventValue = new Hashtable<String, String>();
                            eventValue.put("NameEvent", mDataSet.get(position).getNameEvent());
                            eventValue.put("DateFrom", mDataSet.get(position).getDateFrom());
                            eventValue.put("TimeFrom", mDataSet.get(position).getTimeFrom());
                            eventValue.put("DateTo", mDataSet.get(position).getDateTo());
                            eventValue.put("TimeTo", mDataSet.get(position).getTimeTo());
                            eventValue.put("Description", mDataSet.get(position).getDescription());
                            eventValue.put("Place", mDataSet.get(position).getPlace());
                            eventValue.put("FriendInvite", FriendInvite);
                            eventValue.put("Alarm", mDataSet.get(position).getAlarm());
                            eventValue.put("Repeat", mDataSet.get(position).getRepeat());
                            firebase.child("Event").child(MailUser).child("Refuse_Event").push().setValue(eventValue);
                            mDataSet.remove(position);
                            notifyDataSetChanged();
                            notifyItemRangeChanged(position, mDataSet.size());
                            viewholder.BtnRefuse.setVisibility(View.INVISIBLE);
                            viewholder.BtnAgree.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
            else
            if(Type==3){
                        viewholder.BtnDetail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(v.getContext(),MemoryEvent.class);
                                intent.putExtra("EventValue",mDataSet.get(position));
                                intent.putExtra("NameUser",NameUser);
                                intent.putExtra("MailUser",MailUser);
                                v.getContext().startActivity(intent);
                            }
                        });
            }
        }

    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).getCheck();
    }



}
