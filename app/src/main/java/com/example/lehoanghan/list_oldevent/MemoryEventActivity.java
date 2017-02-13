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
public class MemoryEventActivity extends AppCompatActivity  {

    private Firebase firebase;
    private String NameUser, MailUser;
    private EventValue PassValue;
    private TextView TxtNameEventValue, TxtDateStartValue, TxtTimeStartValue, TxtDateEndValue, TxtTimeEndValue,
            TxtDecriptionValue, TxtPlaceValue, TxtFriendEventValue, TxtAlarmValue, TxtRepeatValue;
    private Button BtnGallery, BtnComment, BtnAddPic;
    private ArrayList<String> ListComment;
    private ArrayList<Bitmap> ListPic;
    private int tem,temfinish,visible;
    private Dialog DialogLoad;
    private EmojiconEditText EdtMyComment;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_event);
        GivedataUser();
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://appcalendar.firebaseio.com/");
        Init();
        SetValue();
    }


    public void Init() {
        TxtNameEventValue = (TextView) findViewById(R.id.txtNameEventValue);
        TxtDateStartValue = (TextView) findViewById(R.id.txtDateStartValue);
        TxtTimeStartValue = (TextView) findViewById(R.id.txtTimeStartValue);
        TxtDateEndValue = (TextView) findViewById(R.id.txtDateEndValue);
        TxtTimeEndValue = (TextView) findViewById(R.id.txtTimeEndValue);
        TxtDecriptionValue = (TextView) findViewById(R.id.txtDecriptionValue);
        TxtPlaceValue = (TextView) findViewById(R.id.txtPlaceValue);
        TxtFriendEventValue = (TextView) findViewById(R.id.txtFriendInviteValue);
        BtnGallery = (Button) findViewById(R.id.btngallery);
        TxtAlarmValue = (TextView) findViewById(R.id.txtAlarmValue);
        TxtRepeatValue = (TextView) findViewById(R.id.txtRepeatValue);
        BtnComment = (Button) findViewById(R.id.btnComment);
        BtnAddPic = (Button) findViewById(R.id.btnPic);
    }


    public void SetValue() {
        TxtNameEventValue.setText(PassValue.getNameEvent());
        TxtDateStartValue.setText(PassValue.getDateFrom());
        TxtTimeStartValue.setText(PassValue.getTimeFrom());
        TxtDateEndValue.setText(PassValue.getDateTo());
        TxtTimeEndValue.setText(PassValue.getTimeTo());
        TxtDecriptionValue.setText(PassValue.getDescription());
        TxtPlaceValue.setText(PassValue.getPlace());
        TxtFriendEventValue.setMovementMethod(new ScrollingMovementMethod());
        TxtFriendEventValue.setText(PassValue.getFriendInvite().replace("&", "."));
        TxtAlarmValue.setText(PassValue.getAlarm());
        TxtRepeatValue.setText(PassValue.getRepeat());
        final ArrayList<Bitmap> ImageArray = new ArrayList<>();
        final String[] Friend = PassValue.getFriendInvite().toString().split(",");
        BtnGallery.setVisibility(View.INVISIBLE);
        for (int i = 0; i < Friend.length; i++) {
            firebase.child("Avata").child(Friend[i]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    byte[] getimage = Base64.decode(dataSnapshot.getValue().toString(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(getimage, 0, getimage.length);
                    ImageArray.add(bitmap);
                    if (ImageArray.size() == Friend.length) {
                        BtnGallery.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }
        BtnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context mContext = v.getContext();
                AlertDialog.Builder builder;
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.gallery_friendinvite, (ViewGroup) v.findViewById(R.id.LLGallery));
                Gallery gallery = (Gallery) view.findViewById(R.id.glrFriendInvite);
                gallery.setAdapter(new ImageAdapter(v.getContext(), PassValue.getFriendInvite().toString(), ImageArray));
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
        BtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent=new Intent(MemoryEventActivity.this,CommentActivity.class);
                intent.putExtra("KeyaddComment",PassValue.getNameEvent().replace(" ", "&") + "*" + PassValue.getDateFrom() + "*" + PassValue.getTimeFrom());
                intent.putExtra("EventValue",PassValue);
                intent.putExtra("NameUser",NameUser);
                intent.putExtra("MailUser",MailUser);
                startActivity(intent);
//                visible=0;
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                final View view = inflater.inflate(R.layout.activity_comment, null);
//                ListComment = new ArrayList<String>();
//                firebase.child("CommentActivity").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot data : dataSnapshot.getChildren()) {
//                            String tem = PassValue.getNameEvent().replace(" ", "&") + "*" + PassValue.getDateFrom() + "*" + PassValue.getTimeFrom();
//                            if (data.getKey().toString().compareTo(tem) == 0) {
//                                for (DataSnapshot value : data.getChildren()) {
//                                    ListComment.add(value.getValue().toString());
//                                }
//                                if (ListComment.size() == 0)
//                                    ListComment.add("No CommentActivity");
//                            }
//                        }
//                        ListView LvComment = (ListView) view.findViewById(R.id.lvComment);
//                        ArrayAdapter arrayAdapter = new ArrayAdapter(v.getContext(), android.R.layout.simple_expandable_list_item_1, ListComment);
//                        LvComment.setAdapter(arrayAdapter);
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });
//                Button BtnSendComment = (Button) view.findViewById(R.id.btnSendComment);
//                Button BtnEmoji=(Button) view.findViewById(R.id.btnEmoji);
//                EdtMyComment = (EmojiconEditText) view.findViewById(R.id.edtMyComment);
//                frameLayout=(FrameLayout) view.findViewById(R.id.emojicons);
//                BtnEmoji.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(visible==0){
//                            visible=1;
//                            setEmojiconFragment(false,visible);
//                        }
//                        else{
//                            visible=0;
//                            setEmojiconFragment(false,visible);
//                        }
//                    }
//                });
//                BtnSendComment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(final View v) {
//                        firebase.child("CommentActivity").child(PassValue.getNameEvent().replace(" ", "&") + "*" + PassValue.getDateFrom() + "*" +
//                                PassValue.getTimeFrom()).push().setValue(NameUser + ": " + EdtMyComment.getText().toString());
//                        firebase.child("CommentActivity").addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                                    if (data.getKey().toString().compareTo(
//                                            PassValue.getNameEvent().replace(" ", "&") + "*" + PassValue.getDateFrom() + "*" + PassValue.getTimeFrom()) == 0) {
//                                        ListComment.clear();
//                                        for (DataSnapshot value : data.getChildren()) {
//                                            ListComment.add(value.getValue().toString());
//                                        }
//                                    }
//                                }
//                                ListView LvComment = (ListView) view.findViewById(R.id.lvComment);
//                                //ArrayAdapter arrayAdapter = new ArrayAdapter(v.getContext(), android.R.layout.simple_expandable_list_item_1, ListComment);
//                                Custom_Adapter_Comment arrayAdapter = new Custom_Adapter_Comment(v.getContext(),ListComment);
//                                LvComment.setAdapter(arrayAdapter);
//                            }
//
//                            @Override
//                            public void onCancelled(FirebaseError firebaseError) {
//
//                            }
//                        });
//                        EdtMyComment.setText("");
//                    }
//                });
//                builder.setView(view);
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                Dialog dialog = builder.create();
//                dialog.setTitle("CommentActivity");
//                dialog.show();
            }
        });
        BtnAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPic(v);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_back,menu);
        setTitle("Memory");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.backfromChangeAccount:
                Intent intent=new Intent(this, Menu_Choose.class);
                intent.putExtra("NameUser",NameUser);
                intent.putExtra("MailUser",MailUser);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void AddPic(final View v){
        final CharSequence[] options={"Pic's Event","Add Pic","Cancel"};

        final AlertDialog.Builder buider=new AlertDialog.Builder(MemoryEventActivity.this);
        buider.setTitle("Photo");
        buider.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Pic's Event")) {
                    ListPic=new ArrayList<Bitmap>();
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
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap bitmap = (BitmapFactory.decodeFile(picturePath));
                 ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
                 if(bitmap!=null) {
                     bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                     byte[] array = outputStream.toByteArray();
                     String PicFirebase = Base64.encodeToString(array, Base64.DEFAULT);
                     firebase.child("Memory").child(PassValue.getNameEvent().replace(" ", "&") + "*" + PassValue.getDateFrom() + "*" +
                             PassValue.getTimeFrom()).push().setValue(PicFirebase);
                     Toast.makeText(getBaseContext(),"Success",Toast.LENGTH_LONG).show();
                 }
                 else
                     Toast.makeText(getBaseContext(),"Fail, You don't choose any pic",Toast.LENGTH_LONG).show();


            }

        }
    }

    public void SeenPic(View v){
        AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
        LayoutInflater inflater=(LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.gallery_friendinvite,null);
        Gallery gallery=(Gallery) view.findViewById(R.id.glrFriendInvite);
        gallery.setAdapter(new GalleryAdapter(view.getContext(),ListPic, PassValue,MailUser,NameUser));
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

    public void dialogLoad(final View v){
//        final ProgressDialog progress=new ProgressDialog(v.getContext());
//        progress.setMessage("Load pic");
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setIndeterminate(true);
//        progress.setProgress(0);
//        progress.show();
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_progress, null);
        final ProgressBar progress=(ProgressBar) view.findViewById(R.id.progressBar);
        tem=0;
        temfinish=0;
        firebase.child("Memory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String tempory = PassValue.getNameEvent().replace(" ", "&") + "*" + PassValue.getDateFrom() + "*" + PassValue.getTimeFrom();
                    if (data.getKey().toString().compareTo(tempory) == 0) {
                        for (DataSnapshot value : data.getChildren()) {
                            final byte[] getimage = Base64.decode(value.getValue().toString(), Base64.DEFAULT);
                            temfinish = 30;
                            Bitmap bitmap = BitmapFactory.decodeByteArray(getimage, 0, getimage.length);
                            ListPic.add(bitmap);
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
        DialogLoad = builder.create();
        DialogLoad.show();
    }

    private void doFakeWork() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void GivedataUser() {
        MailUser = getIntent().getStringExtra("MailUser");
        NameUser = getIntent().getStringExtra("NameUser");
        PassValue = (EventValue) getIntent().getSerializableExtra("EventValue");

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
                DialogLoad.dismiss();
            }
        };
        new Thread(runnable).start();
        Log.i("So luong pic", ListPic.size() + "");
    }

//    private void setEmojiconFragment(boolean useSystemDefault,int in) {
//
//        if(in==1){
//            frameLayout.setVisibility(View.VISIBLE);
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
//                    .commit();
//        }
//        else{
//            frameLayout.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    @Override
//    public void onEmojiconClicked(Emojicon emojicon) {
//        EmojiconsFragment.input(EdtMyComment,emojicon);
//    }
//
//    @Override
//    public void onEmojiconBackspaceClicked(View v) {
//        EmojiconsFragment.backspace(EdtMyComment);
//    }
}




