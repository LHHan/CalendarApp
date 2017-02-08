//package com.example.lehoanghan.list_myevent;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.support.v7.widget.RecyclerView;
//import android.util.Base64;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Gallery;
//import android.widget.TextView;
//
//import com.example.lehoanghan.ImageAdapter;
//import com.example.lehoanghan.EventValue;
//import com.example.lehoanghan.appcalendar.R;
//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.firebase.client.ValueEventListener;
//
//import java.util.ArrayList;
//
///**
// * Created by lehoanghan on 5/22/2016.
// */
//public class MyEventRecyclerAdapter extends RecyclerView.Adapter<MyEventRecyclerAdapter.ViewHolder> {
//
//    private ArrayList<EventValue> mDataSet;
//    private ArrayList<Integer> mDataSetTypes;
//    private String MailUser, Tem;
//
//    public static final int TiTle = 0;
//    public static final int Data = 1;
//
//    private Firebase firebase;
//
//    public MyEventRecyclerAdapter(ArrayList<EventValue> DataSet, ArrayList<Integer> DataSetType, String Mail) {
//        mDataSet = DataSet;
//        mDataSetTypes = DataSetType;
//        MailUser = Mail;
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    public class TitleAboutDate extends ViewHolder {
//
//        TextView TxtTitle;
//
//        public TitleAboutDate(View itemView) {
//
//            super(itemView);
//            TxtTitle = (TextView) itemView.findViewById(R.id.txtTitleEvent);
//        }
//    }
//
//    public class EventData extends ViewHolder {
//        TextView TxtTimeEvent, TxtNameEventValue, TxtDateStartValue, TxtTimeStartValue, TxtDateEndValue, TxtTimeEndValue,
//                TxtDecriptionValue, TxtPlaceValue, TxtFriendEventValue, TxtAlarmValue, TxtRepeatValue;
//        Button BtnGallery;
//
//        public EventData(View itemView) {
//            super(itemView);
//            TxtTimeEvent = (TextView) itemView.findViewById(R.id.txtTimeEvent);
//            TxtNameEventValue = (TextView) itemView.findViewById(R.id.txtNameEventValue);
//            TxtDateStartValue = (TextView) itemView.findViewById(R.id.txtDateStartValue);
//            TxtTimeStartValue = (TextView) itemView.findViewById(R.id.txtTimeStartValue);
//            TxtDateEndValue = (TextView) itemView.findViewById(R.id.txtDateEndValue);
//            TxtTimeEndValue = (TextView) itemView.findViewById(R.id.txtTimeEndValue);
//            TxtDecriptionValue = (TextView) itemView.findViewById(R.id.txtDecriptionValue);
//            TxtPlaceValue = (TextView) itemView.findViewById(R.id.txtPlaceValue);
//            TxtFriendEventValue = (TextView) itemView.findViewById(R.id.txtFriendInviteValue);
//            BtnGallery = (Button) itemView.findViewById(R.id.btngallery);
//            TxtAlarmValue = (TextView) itemView.findViewById(R.id.txtAlarmValue);
//            TxtRepeatValue = (TextView) itemView.findViewById(R.id.txtRepeatValue);
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v;
//
//        if (viewType == TiTle) {
//            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recycler_title, parent, false);
//            Firebase.setAndroidContext(v.getContext());
//            firebase = new Firebase("https://appcalendar.firebaseio.com/");
//            return new TitleAboutDate(v);
//        } else
//        //if(viewType==Data)
//        {
//            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recycler_myevent, parent, false);
//            Firebase.setAndroidContext(v.getContext());
//            firebase = new Firebase("https://appcalendar.firebaseio.com/");
//            return new EventData(v);
//        }
//    }
//
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//        if (holder.getItemViewType() == TiTle) {
//            TitleAboutDate viewholder = (TitleAboutDate) holder;
//            viewholder.TxtTitle.setText(mDataSet.get(position).getDateFrom());
//        } else {
//            final EventData viewholder = (EventData) holder;
//            viewholder.TxtTimeEvent.setText(mDataSet.get(position).getTimeFrom());
//            viewholder.TxtNameEventValue.setText(mDataSet.get(position).getNameEvent());
//            viewholder.TxtDateStartValue.setText(mDataSet.get(position).getDateFrom());
//            viewholder.TxtTimeStartValue.setText(mDataSet.get(position).getTimeFrom());
//            viewholder.TxtDateEndValue.setText(mDataSet.get(position).getDateTo());
//            viewholder.TxtTimeEndValue.setText(mDataSet.get(position).getTimeTo());
//            viewholder.TxtDecriptionValue.setText(mDataSet.get(position).getDescription());
//            viewholder.TxtPlaceValue.setText(mDataSet.get(position).getPlace());
//            viewholder.TxtFriendEventValue.setText(mDataSet.get(position).getFriendInvite().replace("&", "."));
//            viewholder.TxtAlarmValue.setText(mDataSet.get(position).getAlarm());
//            viewholder.TxtRepeatValue.setText(mDataSet.get(position).getRepeat());
//
//            final ArrayList<Bitmap> ImageArray = new ArrayList<>();
//            final String[] Friend = mDataSet.get(position).getFriendInvite().toString().split(",");
//
//            viewholder.BtnGallery.setVisibility(View.INVISIBLE);
//            for (int i = 0; i < Friend.length; i++) {
//                firebase.child("Avata").child(Friend[i]).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        byte[] getimage = Base64.decode(dataSnapshot.getValue().toString(), Base64.DEFAULT);
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(getimage, 0, getimage.length);
//                        ImageArray.add(bitmap);
//                        if (ImageArray.size() == Friend.length) {
//                            //viewholder.TxtFriendEventValue.setVisibility(View.INVISIBLE);
//                            viewholder.BtnGallery.setVisibility(View.VISIBLE);
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });
//
//            }
//            viewholder.BtnGallery.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Context mContext = v.getContext();
//                    AlertDialog.Builder builder;
//                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View view = inflater.inflate(R.layout.gallery_friendinvite, (ViewGroup) v.findViewById(R.id.LLGallery));
//                    Gallery gallery = (Gallery) view.findViewById(R.id.glrFriendInvite);
//                    gallery.setAdapter(new ImageAdapter(v.getContext(), mDataSet.get(position).getFriendInvite().toString(), ImageArray));
//                    builder = new AlertDialog.Builder(v.getContext());
//                    builder.setView(view);
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    Dialog dialog = builder.create();
//                    dialog.setTitle("Friend Invite");
//                    dialog.show();
//                }
//            });
//        }
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return mDataSet.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return mDataSet.get(position).getCheck();
//    }
//}
