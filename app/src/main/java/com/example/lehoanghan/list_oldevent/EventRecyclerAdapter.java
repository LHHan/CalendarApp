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

    private ArrayList<EventValue> dataSet;
    private ArrayList<Integer> dataSetTypes;
    private String mailUser, nameUser;

    public static final int title = 0;
    public static final int data = 1;
    private int type;
    private Context context;

    private Firebase firebase;

    public EventRecyclerAdapter(ArrayList<EventValue> DataSet, ArrayList<Integer> DataSetType, String Mail, int type) {
        dataSet = DataSet;
        dataSetTypes = DataSetType;
        mailUser = Mail;
        this.type = type;
    }

    public EventRecyclerAdapter(ArrayList<EventValue> DataSet, ArrayList<Integer> DataSetType, String Mail, String Name, int type) {
        dataSet = DataSet;
        dataSetTypes = DataSetType;
        mailUser = Mail;
        nameUser = Name;
        this.type = type;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class TitleAboutDate extends ViewHolder {

        TextView tvTitle;

        public TitleAboutDate(View itemView) {

            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.cardview_recycler_title_tv_title_event);
        }
    }

    public class eventData extends ViewHolder {
        TextView tvTimeEvent, tvNameEventValue, tvDateStartValue, tvTimeStartValue, tvDateEndValue, tvTimeEndValue,
                tvDecriptionValue, tvPlaceValue, tvFriendEventValue, tvAlarmValue, tvRepeatValue;
        Button btnAgree, btnRefuse, btnGallery, btnDetail;

        public eventData(View itemView) {
            super(itemView);
            tvTimeEvent = (TextView) itemView.findViewById(R.id.cardview_recycler_new_event_tv_time_event);
            tvNameEventValue = (TextView) itemView.findViewById(R.id.cardview_recycler_new_event_tv_name_event_value);
            if (type != 3) {
                tvDateStartValue = (TextView) itemView.findViewById(R.id.cardview_recycler_new_event_tv_date_start_value);
                tvTimeStartValue = (TextView) itemView.findViewById(R.id.cardview_recycler_new_event_tv_time_start_value);
                tvDateEndValue = (TextView) itemView.findViewById(R.id.cardview_recycler_new_event_tv_date_end_value);
                tvTimeEndValue = (TextView) itemView.findViewById(R.id.cardview_recycler_new_event_tv_time_end_value);
                tvDecriptionValue = (TextView) itemView.findViewById(R.id.cardview_recycler_new_event_tv_decription_value);
                tvPlaceValue = (TextView) itemView.findViewById(R.id.cardview_recycler_new_event_tv_place_value);
                tvFriendEventValue = (TextView) itemView.findViewById(R.id.cardview_recycler_new_event_tv_friend_invite_value);
                btnGallery = (Button) itemView.findViewById(R.id.cardview_recycler_new_event_btn_gallery);
                tvAlarmValue = (TextView) itemView.findViewById(R.id.cardview_recycler_new_event_tv_alarm_value);
                tvRepeatValue = (TextView) itemView.findViewById(R.id.cardview_recycler_new_event_tv_repeat_value);
                if (type == 1) {
                    btnAgree = (Button) itemView.findViewById(R.id.cardview_recycler_new_event_btn_agree);
                    btnRefuse = (Button) itemView.findViewById(R.id.cardview_recycler_new_event_btn_refuse);
                }
            } else if (type == 3) {
                btnDetail = (Button) itemView.findViewById(R.id.cardview_recycler_old_event_btn_detail);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        context = parent.getContext();
        if (viewType == title) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recycler_title, parent, false);
            Firebase.setAndroidContext(v.getContext());
            firebase = new Firebase("https://appcalendar.firebaseio.com/");
            return new TitleAboutDate(v);
        } else //if(viewType == data)
        {
            if (type == 1)
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recycler_new_event, parent, false);
            else if (type == 2)
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recycler_my_event, parent, false);
            else
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recycler_old_event, parent, false);
            Firebase.setAndroidContext(v.getContext());
            firebase = new Firebase("https://appcalendar.firebaseio.com/");
            return new eventData(v);
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder.getItemViewType() == title) {
            TitleAboutDate viewholder = (TitleAboutDate) holder;
            viewholder.tvTitle.setText(dataSet.get(position).getDateFrom());
        } else {
            final eventData viewholder = (eventData) holder;
            viewholder.tvTimeEvent.setText(dataSet.get(position).getTimeFrom());
            viewholder.tvNameEventValue.setText(dataSet.get(position).getNameEvent());
            if (type != 3) {
                viewholder.tvDateStartValue.setText(dataSet.get(position).getDateFrom());
                viewholder.tvTimeStartValue.setText(dataSet.get(position).getTimeFrom());
                viewholder.tvDateEndValue.setText(dataSet.get(position).getDateTo());
                viewholder.tvTimeEndValue.setText(dataSet.get(position).getTimeTo());
                viewholder.tvDecriptionValue.setText(dataSet.get(position).getDescription());
                viewholder.tvPlaceValue.setText(dataSet.get(position).getPlace());
                viewholder.tvFriendEventValue.setText(dataSet.get(position).getFriendInvite().replace("&", "."));
                viewholder.tvAlarmValue.setText(dataSet.get(position).getAlarm());
                viewholder.tvRepeatValue.setText(dataSet.get(position).getRepeat());
                final ArrayList<Bitmap> imageArray = new ArrayList<>();
                final String[] Friend = dataSet.get(position).getFriendInvite().toString().split(",");

                viewholder.btnGallery.setVisibility(View.INVISIBLE);
                for (int i = 0; i < Friend.length; i++) {
                    firebase.child("Avata").child(Friend[i]).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            byte[] getimage = Base64.decode(dataSnapshot.getValue().toString(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(getimage, 0, getimage.length);
                            imageArray.add(bitmap);
                            if (imageArray.size() == Friend.length) {
                                viewholder.btnGallery.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                }
                viewholder.btnGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context mContext = v.getContext();
                        AlertDialog.Builder builder;
                        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.activity_gallery_friend_invite, (ViewGroup) v.findViewById(R.id.activity_gallery_friend_invite_ll_main));
                        Gallery gallery = (Gallery) view.findViewById(R.id.activity_gallery_friend_invite);
                        gallery.setAdapter(new ImageAdapter(v.getContext(), dataSet.get(position).getFriendInvite().toString(), imageArray));
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
                if (type == 1) {
                    viewholder.btnAgree.setVisibility(View.VISIBLE);
                    viewholder.btnRefuse.setVisibility(View.VISIBLE);

                    viewholder.btnAgree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlarmManager mAlarmManager;
                            Intent mNotificationReceiverIntent;
                            PendingIntent mNotificationReceiverPendingIntent;
                            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            mNotificationReceiverIntent = new Intent(context.getApplicationContext(), AlarmNotificationReceiver.class);
                            mNotificationReceiverIntent.putExtra("Name", dataSet.get(position).getNameEvent());
                            mNotificationReceiverIntent.putExtra("Date", dataSet.get(position).getDateFrom());
                            mNotificationReceiverIntent.putExtra("Time", dataSet.get(position).getTimeFrom());
                            mNotificationReceiverIntent.putExtra("Place", dataSet.get(position).getPlace());
                            mNotificationReceiverIntent.putExtra("nameUser", nameUser);
                            mNotificationReceiverIntent.putExtra("mailUser", mailUser);
                            mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                                    context.getApplicationContext(), 0, mNotificationReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            float timeAlarm = 0;
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:m");
                            try {
                                Date date = format.parse(dataSet.get(position).getDateFrom() + " " + dataSet.get(position).getTimeFrom());
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                timeAlarm = date.getTime() - (Integer.parseInt(dataSet.get(position).getAlarm()) * 60 * 1000);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (timeAlarm != 0) {
                                mAlarmManager.set(AlarmManager.RTC_WAKEUP, (long) timeAlarm, mNotificationReceiverPendingIntent);
                            }

//                            mLoggerReceiverIntent = new Intent(context.getApplicationContext(),
//                                    RingtonePlayingService.class);
//
//                            mLoggerReceiverPendingIntent = PendingIntent.getBroadcast(
//                                    context.getApplicationContext(), 0, mLoggerReceiverIntent, 0);


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
                            firebase.child("Event").child(mailUser).child("Accept_Event").push().setValue(eventValue);
                            dataSet.remove(position);
                            notifyDataSetChanged();
                            notifyItemRangeChanged(position, dataSet.size());
                            viewholder.btnRefuse.setVisibility(View.INVISIBLE);
                            viewholder.btnAgree.setVisibility(View.INVISIBLE);
                        }
                    });
                    viewholder.btnRefuse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String FriendInvite = "";
                            String[] friend = dataSet.get(position).getFriendInvite().split(",");
                            ArrayList<String> friend1 = new ArrayList<String>();
                            for (int i = 0; i < friend.length; i++) {
                                friend1.add(new String(friend[i]));
                            }
                            for (int i = 0; i < friend1.size(); i++) {
                                if (friend1.get(i).trim().replace(".", "&").compareTo(mailUser) == 0)
                                    friend1.remove(i);
                            }
                            for (int i = 0; i < friend1.size(); i++) {
                                if (i == (friend1.size() - 1))
                                    FriendInvite += friend1.get(i);
                                else
                                    FriendInvite += friend1.get(i) + ", ";
                            }
                            Map<String, String> eventValue = new Hashtable<String, String>();
                            eventValue.put("NameEvent", dataSet.get(position).getNameEvent());
                            eventValue.put("DateFrom", dataSet.get(position).getDateFrom());
                            eventValue.put("TimeFrom", dataSet.get(position).getTimeFrom());
                            eventValue.put("DateTo", dataSet.get(position).getDateTo());
                            eventValue.put("TimeTo", dataSet.get(position).getTimeTo());
                            eventValue.put("Description", dataSet.get(position).getDescription());
                            eventValue.put("Place", dataSet.get(position).getPlace());
                            eventValue.put("FriendInvite", FriendInvite);
                            eventValue.put("Alarm", dataSet.get(position).getAlarm());
                            eventValue.put("Repeat", dataSet.get(position).getRepeat());
                            firebase.child("Event").child(mailUser).child("Refuse_Event").push().setValue(eventValue);
                            dataSet.remove(position);
                            notifyDataSetChanged();
                            notifyItemRangeChanged(position, dataSet.size());
                            viewholder.btnRefuse.setVisibility(View.INVISIBLE);
                            viewholder.btnAgree.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            } else if (type == 3) {
                viewholder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), MemoryEventActivity.class);
                        intent.putExtra("EventValue", dataSet.get(position));
                        intent.putExtra("nameUser", nameUser);
                        intent.putExtra("mailUser", mailUser);
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
