package com.example.lehoanghan.list_oldevent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lehoanghan.EventValue;
import com.example.lehoanghan.ImageAdapter;
import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.choosemenu.Menu_Choose;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.rockerhieu.emojicon.EmojiconEditText;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by lehoanghan on 5/22/2016.
 */
public class MemoryEventActivity extends AppCompatActivity {

    private Firebase firebase;
    private String nameUser, mailUser;
    private EventValue passValue;
    private TextView tvNameEventValue, tvDateStartValue, tvTimeStartValue, tvDateEndValue, tvTimeEndValue,
            tvDecriptionValue, tvPlaceValue, tvFriendEventValue, tvAlarmValue, tvRepeatValue;
    private Button btnGallery, btnComment, btnAddPic;
    private ArrayList<String> listComment;
    private ArrayList<Bitmap> listPic;
    private int tem, temfinish, visible;
    private Dialog dialogLoad;
    private EmojiconEditText emojiconMyComment;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_event);
        giveDataUser();
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://appcalendar.firebaseio.com/");
        Init();
        setValue();
    }


    public void Init() {
        tvNameEventValue = (TextView) findViewById(R.id.activity_memory_event_tv_name_event_value);
        tvDateStartValue = (TextView) findViewById(R.id.activity_memory_event_tv_date_start_value);
        tvTimeStartValue = (TextView) findViewById(R.id.activity_memory_event_tv_time_start_value);
        tvDateEndValue = (TextView) findViewById(R.id.activity_memory_event_tv_date_end_value);
        tvTimeEndValue = (TextView) findViewById(R.id.activity_memory_event_tv_time_end_value);
        tvDecriptionValue = (TextView) findViewById(R.id.activity_memory_event_tv_decription_value);
        tvPlaceValue = (TextView) findViewById(R.id.activity_memory_event_tv_place_value);
        tvFriendEventValue = (TextView) findViewById(R.id.activity_memory_event_tv_friend_invite_value);
        btnGallery = (Button) findViewById(R.id.activity_memory_event_btn_gallery);
        tvAlarmValue = (TextView) findViewById(R.id.activity_memory_event_tv_alarm_value);
        tvRepeatValue = (TextView) findViewById(R.id.activity_memory_event_tv_repeat_value);
        btnComment = (Button) findViewById(R.id.activity_memory_event_btn_commnet);
        btnAddPic = (Button) findViewById(R.id.activity_memory_event_btn_picture);
    }


    public void setValue() {
        tvNameEventValue.setText(passValue.getNameEvent());
        tvDateStartValue.setText(passValue.getDateFrom());
        tvTimeStartValue.setText(passValue.getTimeFrom());
        tvDateEndValue.setText(passValue.getDateTo());
        tvTimeEndValue.setText(passValue.getTimeTo());
        tvDecriptionValue.setText(passValue.getDescription());
        tvPlaceValue.setText(passValue.getPlace());
        tvFriendEventValue.setMovementMethod(new ScrollingMovementMethod());
        tvFriendEventValue.setText(passValue.getFriendInvite().replace("&", "."));
        tvAlarmValue.setText(passValue.getAlarm());
        tvRepeatValue.setText(passValue.getRepeat());
        final ArrayList<Bitmap> imageArray = new ArrayList<>();
        final String[] Friend = passValue.getFriendInvite().toString().split(",");
        btnGallery.setVisibility(View.INVISIBLE);
        for (int i = 0; i < Friend.length; i++) {
            firebase.child("Avata").child(Friend[i]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    byte[] getimage = Base64.decode(dataSnapshot.getValue().toString(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(getimage, 0, getimage.length);
                    imageArray.add(bitmap);
                    if (imageArray.size() == Friend.length) {
                        btnGallery.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context mContext = v.getContext();
                AlertDialog.Builder builder;
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_gallery_friend_invite, (ViewGroup) v.findViewById(R.id.activity_gallery_friend_invite_ll_main));
                Gallery gallery = (Gallery) view.findViewById(R.id.activity_gallery_friend_invite);
                gallery.setAdapter(new ImageAdapter(v.getContext(), passValue.getFriendInvite().toString(), imageArray));
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
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(MemoryEventActivity.this, CommentActivity.class);
                intent.putExtra("KeyaddComment", passValue.getNameEvent().replace(" ", "&") + "*" + passValue.getDateFrom() + "*" + passValue.getTimeFrom());
                intent.putExtra("EventValue", passValue);
                intent.putExtra("nameUser", nameUser);
                intent.putExtra("mailUser", mailUser);
                startActivity(intent);
            }
        });
        btnAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPic(v);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_back, menu);
        setTitle("Memory");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backfromChangeAccount:
                Intent intent = new Intent(this, Menu_Choose.class);
                intent.putExtra("NameUser", nameUser);
                intent.putExtra("MailUser", mailUser);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void AddPic(final View v) {
        final CharSequence[] options = {"Pic's Event", "Add Pic", "Cancel"};

        final AlertDialog.Builder buider = new AlertDialog.Builder(MemoryEventActivity.this);
        buider.setTitle("Photo");
        buider.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Pic's Event")) {
                    listPic = new ArrayList<Bitmap>();
                    dialogLoad(v);

                } else if (options[which].equals("Add Pic")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                } else if (options[which].equals("Cancel"))
                    dialog.dismiss();

            }
        });
        buider.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap bitmap = (BitmapFactory.decodeFile(picturePath));
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    byte[] array = outputStream.toByteArray();
                    String PicFirebase = Base64.encodeToString(array, Base64.DEFAULT);
                    firebase.child("Memory").child(passValue.getNameEvent().replace(" ", "&") + "*" + passValue.getDateFrom() + "*" +
                            passValue.getTimeFrom()).push().setValue(PicFirebase);
                    Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getBaseContext(), "Fail, You don't choose any pic", Toast.LENGTH_LONG).show();


            }

        }
    }

    public void SeenPic(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_gallery_friend_invite, null);
        Gallery gallery = (Gallery) view.findViewById(R.id.activity_gallery_friend_invite);
        gallery.setAdapter(new GalleryAdapter(view.getContext(), listPic, passValue, mailUser, nameUser));
        builder.setView(view);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.setTitle("Memory");
        dialog.show();

    }

    public void dialogLoad(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_progress, null);
        final ProgressBar progress = (ProgressBar) view.findViewById(R.id.dialog_progress_pb_status);
        tem = 0;
        temfinish = 0;
        firebase.child("Memory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String tempory = passValue.getNameEvent().replace(" ", "&") + "*" + passValue.getDateFrom() + "*" + passValue.getTimeFrom();
                    if (data.getKey().toString().compareTo(tempory) == 0) {
                        for (DataSnapshot value : data.getChildren()) {
                            final byte[] getimage = Base64.decode(value.getValue().toString(), Base64.DEFAULT);
                            temfinish = 30;
                            Bitmap bitmap = BitmapFactory.decodeByteArray(getimage, 0, getimage.length);
                            listPic.add(bitmap);
                        }
                        newThread(progress);
                        break;
                    } else {
                        newThread(progress);
                    }
                }
                SeenPic(v);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        builder.setView(view);
        dialogLoad = builder.create();
        dialogLoad.show();
    }

    private void doFakeWork() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void giveDataUser() {
        mailUser = getIntent().getStringExtra("MailUser");
        nameUser = getIntent().getStringExtra("NameUser");
        passValue = (EventValue) getIntent().getSerializableExtra("EventValue");
    }

    public void newThread(final ProgressBar progress) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (tem != temfinish) {
                    tem += 5;
                    doFakeWork();
                    progress.post(new Runnable() {
                        @Override
                        public void run() {
                            progress.setProgress(tem);
                        }
                    });
                }
                dialogLoad.dismiss();
            }
        };
        new Thread(runnable).start();
        Log.i("So luong pic", listPic.size() + "");
    }
}




