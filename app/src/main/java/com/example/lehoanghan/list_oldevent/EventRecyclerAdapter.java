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

    public static final int TITLE = 0;

    public static final int data = 1;

    private ArrayList<EventValue> dataSet;

    private ArrayList<Integer> dataSetTypes;

    private String mailUser;

    private String nameUser;

    private int intType;

    private Context adapterContext;

    private Firebase fireBase;

    public EventRecyclerAdapter(ArrayList<EventValue> DataSet,
                                ArrayList<Integer> DataSetType,
                                String Mail, int type) {
        dataSet = DataSet;
        dataSetTypes = DataSetType;
        mailUser = Mail;
        this.intType = type;
    }

    public EventRecyclerAdapter(ArrayList<EventValue> DataSet,
                                ArrayList<Integer> DataSetType,
                                String Mail, String Name, int type) {
        dataSet = DataSet;
        dataSetTypes = DataSetType;
        mailUser = Mail;
        nameUser = Name;
        this.intType = type;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class TitleAboutDate extends ViewHolder {

        private TextView tvTitle;

        public TitleAboutDate(View itemView) {

            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.cardview_recycler_title_tv_title_event);
        }
    }

    public class eventData extends ViewHolder {
        private TextView tvTimeEvent;

        private TextView tvNameEventValue;

        private TextView tvDateStartValue;

        private TextView tvTimeStartValue;

        private TextView tvDateEndValue;

        private TextView tvTimeEndValue;

        private TextView tvDecriptionValue;

        private TextView tvPlaceValue;

        private TextView tvFriendEventValue;

        private TextView tvAlarmValue;

        private TextView tvRepeatValue;

        private Button btnAgree;

        private Button btnRefuse;

        private Button btnGallery;

        private Button btnDetail;

        public eventData(View itemView) {
            super(itemView);
            tvTimeEvent = (TextView) itemView.findViewById(
                    R.id.cardview_recycler_new_event_tv_time_event);
            tvNameEventValue = (TextView) itemView.findViewById(
                    R.id.cardview_recycler_new_event_tv_name_event_value);
            if (intType != 3) {
                tvDateStartValue = (TextView) itemView.findViewById(
                        R.id.cardview_recycler_new_event_tv_date_start_value);
                tvTimeStartValue = (TextView) itemView.findViewById(
                        R.id.cardview_recycler_new_event_tv_time_start_value);
                tvDateEndValue = (TextView) itemView.findViewById(
                        R.id.cardview_recycler_new_event_tv_date_end_value);
                tvTimeEndValue = (TextView) itemView.findViewById(
                        R.id.cardview_recycler_new_event_tv_time_end_value);
                tvDecriptionValue = (TextView) itemView.findViewById(
                        R.id.cardview_recycler_new_event_tv_decription_value);
                tvPlaceValue = (TextView) itemView.findViewById(
                        R.id.cardview_recycler_new_event_tv_place_value);
                tvFriendEventValue = (TextView) itemView.findViewById(
                        R.id.cardview_recycler_new_event_tv_friend_invite_value);
                btnGallery = (Button) itemView.findViewById(
                        R.id.cardview_recycler_new_event_btn_gallery);
                tvAlarmValue = (TextView) itemView.findViewById(
                        R.id.cardview_recycler_new_event_tv_alarm_value);
                tvRepeatValue = (TextView) itemView.findViewById(
                        R.id.cardview_recycler_new_event_tv_repeat_value);
                if (intType == 1) {
                    btnAgree = (Button) itemView.findViewById(
                            R.id.cardview_recycler_new_event_btn_agree);
                    btnRefuse = (Button) itemView.findViewById(
                            R.id.cardview_recycler_new_event_btn_refuse);
                }
            } else if (intType == 3) {
                btnDetail = (Button) itemView.findViewById(
                        R.id.cardview_recycler_old_event_btn_detail);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        adapterContext = parent.getContext();
        if (viewType == TITLE) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_recycler_title, parent, false);
            Firebase.setAndroidContext(v.getContext());
            fireBase = new Firebase("https://appcalendar.firebaseio.com/");
            return new TitleAboutDate(v);
        } else { //if(viewType == data)
            if (intType == 1) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_recycler_new_event, parent, false);
            } else if (intType == 2) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_recycler_my_event, parent, false);
            } else {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_recycler_old_event, parent, false);
            }
            Firebase.setAndroidContext(v.getContext());
            fireBase = new Firebase("https://appcalendar.firebaseio.com/");
            return new eventData(v);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder.getItemViewType() == TITLE) {
            TitleAboutDate viewholder = (TitleAboutDate) holder;
            viewholder.tvTitle.setText(dataSet.get(position).getDateFrom());
        } else {
            final eventData VIEWHOLDER = (eventData) holder;
            VIEWHOLDER.tvTimeEvent.setText(dataSet.get(position).getTimeFrom());
            VIEWHOLDER.tvNameEventValue.setText(dataSet.get(position).getNameEvent());
            if (intType != 3) {
                VIEWHOLDER.tvDateStartValue.setText(dataSet.get(position).getDateFrom());
                VIEWHOLDER.tvTimeStartValue.setText(dataSet.get(position).getTimeFrom());
                VIEWHOLDER.tvDateEndValue.setText(dataSet.get(position).getDateTo());
                VIEWHOLDER.tvTimeEndValue.setText(dataSet.get(position).getTimeTo());
                VIEWHOLDER.tvDecriptionValue.setText(dataSet.get(position).getDescription());
                VIEWHOLDER.tvPlaceValue.setText(dataSet.get(position).getPlace());
                VIEWHOLDER.tvFriendEventValue.setText(dataSet.get(position).getFriendInvite()
                        .replace("&", "."));
                VIEWHOLDER.tvAlarmValue.setText(dataSet.get(position).getAlarm());
                VIEWHOLDER.tvRepeatValue.setText(dataSet.get(position).getRepeat());
                final ArrayList<Bitmap> IMAGEARRAY = new ArrayList<>();
                final String[] FRIEND =
                        dataSet.get(position).getFriendInvite().toString().split(",");

                VIEWHOLDER.btnGallery.setVisibility(View.INVISIBLE);
                for (int i = 0; i < FRIEND.length; i++) {
                    fireBase.child("Avata").child(FRIEND[i])
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    byte[] getimage =
                                            Base64.decode(dataSnapshot.getValue().toString(),
                                                    Base64.DEFAULT);
                                    Bitmap bitmap =
                                            BitmapFactory.decodeByteArray(getimage, 0,
                                                    getimage.length);
                                    IMAGEARRAY.add(bitmap);
                                    if (IMAGEARRAY.size() == FRIEND.length) {
                                        VIEWHOLDER.btnGallery.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                }
                            });
                }
                VIEWHOLDER.btnGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context mContext = v.getContext();
                        AlertDialog.Builder builder;
                        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);
                        View view =
                                inflater.inflate(R.layout.activity_gallery_friend_invite,
                                        (ViewGroup) v.findViewById(
                                                R.id.activity_gallery_friend_invite_ll_main));
                        Gallery gallery =
                                (Gallery) view.findViewById(R.id.activity_gallery_friend_invite);
                        gallery.setAdapter(new ImageAdapter(v.getContext(),
                                dataSet.get(position).getFriendInvite().toString(),
                                IMAGEARRAY));
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
                if (intType == 1) {
                    VIEWHOLDER.btnAgree.setVisibility(View.VISIBLE);
                    VIEWHOLDER.btnRefuse.setVisibility(View.VISIBLE);

                    VIEWHOLDER.btnAgree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlarmManager mAlarmManager;
                            Intent mNotificationReceiverIntent;
                            PendingIntent mNotificationReceiverPendingIntent;
                            mAlarmManager =
                                    (AlarmManager) adapterContext
                                            .getSystemService(Context.ALARM_SERVICE);
                            mNotificationReceiverIntent =
                                    new Intent(adapterContext.getApplicationContext(),
                                            AlarmNotificationReceiver.class);
                            mNotificationReceiverIntent.putExtra(
                                    "Name", dataSet.get(position).getNameEvent());
                            mNotificationReceiverIntent.putExtra(
                                    "Date", dataSet.get(position).getDateFrom());
                            mNotificationReceiverIntent.putExtra(
                                    "Time", dataSet.get(position).getTimeFrom());
                            mNotificationReceiverIntent.putExtra(
                                    "Place", dataSet.get(position).getPlace());
                            mNotificationReceiverIntent.putExtra(
                                    "NameUser", nameUser);
                            mNotificationReceiverIntent.putExtra(
                                    "MailUser", mailUser);
                            mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                                    adapterContext.getApplicationContext(), 0,
                                    mNotificationReceiverIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                            float timeAlarm = 0;
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:m");
                            try {
                                Date date = format.parse(dataSet.get(position).getDateFrom()
                                        + " " + dataSet.get(position).getTimeFrom());
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                timeAlarm = date.getTime()
                                        - (Integer.parseInt(dataSet.get(position)
                                        .getAlarm()) * 60 * 1000);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (timeAlarm != 0) {
                                mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                                        (long) timeAlarm,
                                        mNotificationReceiverPendingIntent);
                            }

                            Map<String, String> eventValue = new Hashtable<String, String>();
                            eventValue.put("NameEvent", dataSet.get(position).getNameEvent());
                            eventValue.put("DateFrom", dataSet.get(position).getDateFrom());
                            eventValue.put("TimeFrom", dataSet.get(position).getTimeFrom());
                            eventValue.put("DateTo", dataSet.get(position).getDateTo());
                            eventValue.put("TimeTo", dataSet.get(position).getTimeTo());
                            eventValue.put("Description", dataSet.get(position).getDescription());
                            eventValue.put("Place", dataSet.get(position).getPlace());
                            eventValue.put("FriendInvite", dataSet.get(position).getFriendInvite());
                            eventValue.put("Alarm", dataSet.get(position).getAlarm());
                            eventValue.put("Repeat", dataSet.get(position).getRepeat());
                            fireBase.child("Event").child(mailUser).child("Accept_Event").push()
                                    .setValue(eventValue);
                            dataSet.remove(position);
                            notifyDataSetChanged();
                            notifyItemRangeChanged(position, dataSet.size());
                            VIEWHOLDER.btnRefuse.setVisibility(View.INVISIBLE);
                            VIEWHOLDER.btnAgree.setVisibility(View.INVISIBLE);
                        }
                    });
                    VIEWHOLDER.btnRefuse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String friendInvite = "";
                            String[] listFriend =
                                    dataSet.get(position).getFriendInvite().split(",");
                            ArrayList<String> listFriend1 = new ArrayList<String>();
                            for (int i = 0; i < listFriend.length; i++) {
                                listFriend1.add(new String(listFriend[i]));
                            }
                            for (int i = 0; i < listFriend1.size(); i++) {
                                if (listFriend1.get(i).trim().replace(".", "&")
                                        .compareTo(mailUser) == 0) {
                                    listFriend1.remove(i);
                                }
                            }
                            for (int i = 0; i < listFriend1.size(); i++) {
                                if (i == (listFriend1.size() - 1)) {
                                    friendInvite += listFriend1.get(i);
                                } else {
                                    friendInvite += listFriend1.get(i) + ", ";
                                }
                            }
                            Map<String, String> eventValue = new Hashtable<String, String>();
                            eventValue.put("NameEvent", dataSet.get(position).getNameEvent());
                            eventValue.put("DateFrom", dataSet.get(position).getDateFrom());
                            eventValue.put("TimeFrom", dataSet.get(position).getTimeFrom());
                            eventValue.put("DateTo", dataSet.get(position).getDateTo());
                            eventValue.put("TimeTo", dataSet.get(position).getTimeTo());
                            eventValue.put("Description", dataSet.get(position).getDescription());
                            eventValue.put("Place", dataSet.get(position).getPlace());
                            eventValue.put("FriendInvite", friendInvite);
                            eventValue.put("Alarm", dataSet.get(position).getAlarm());
                            eventValue.put("Repeat", dataSet.get(position).getRepeat());
                            fireBase.child("Event").child(mailUser).child("Refuse_Event").push()
                                    .setValue(eventValue);
                            dataSet.remove(position);
                            notifyDataSetChanged();
                            notifyItemRangeChanged(position, dataSet.size());
                            VIEWHOLDER.btnRefuse.setVisibility(View.INVISIBLE);
                            VIEWHOLDER.btnAgree.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            } else if (intType == 3) {
                VIEWHOLDER.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), MemoryEventActivity.class);
                        intent.putExtra("EventValue", dataSet.get(position));
                        intent.putExtra("NameUser", nameUser);
                        intent.putExtra("MailUser", mailUser);
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getCheck();
    }

}
