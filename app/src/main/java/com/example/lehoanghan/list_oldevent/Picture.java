package com.example.lehoanghan.list_oldevent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lehoanghan.EventValue;
import com.example.lehoanghan.appcalendar.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import java.util.ArrayList;

/**
 * Created by lehoanghan on 6/28/2016.
 */
public class Picture extends AppCompatActivity implements EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    private Bitmap bitmap;
    private EventValue eventValue;
    private String position,nameUser,mailUser,ImagePath;
    private Firebase firebase;
    private ArrayList<String> ListComment;
    private int visible;
    private EmojiconEditText EdtMyComment;
    private FrameLayout frameLayout;
    private ImageView ImgPicture;
    private Button BtnSendComment,BtnEmoji;
    private ListView LvComment;
    private Uri uriImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://appcalendar.firebaseio.com/");
        GiveValue();
        Init();
        SetComment();
    }

    public void Init(){
        ImgPicture=(ImageView) findViewById(R.id.imgPicture);
        ImgPicture.setImageBitmap(bitmap);
        ImgPicture.setLayoutParams(new LinearLayout.LayoutParams(1000, 1000));
        ImgPicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ImagePath = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"picture"+position,position);
                uriImage= Uri.parse(ImagePath);
                Toast.makeText(getBaseContext(),"Save success",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        BtnSendComment = (Button) findViewById(R.id.btnSendCommentPicture);
        BtnEmoji=(Button) findViewById(R.id.btnEmojiPicture);
        EdtMyComment = (EmojiconEditText) findViewById(R.id.edtCommentPicture);
        frameLayout=(FrameLayout) findViewById(R.id.emojiconsPicture);
        LvComment = (ListView) findViewById(R.id.lvCommentPicture);

    }

    public void SetComment(){
        visible=0;
        ListComment = new ArrayList<String>();
        firebase.child("CommentPicture").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String tem = eventValue.getNameEvent().replace(" ", "&") + "*" + eventValue.getDateFrom() + "*" + eventValue.getTimeFrom()+"*"+position;
                    if (data.getKey().toString().compareTo(tem) == 0) {
                        for (DataSnapshot value : data.getChildren()) {
                            ListComment.add(value.getValue().toString());
                        }
                        if (ListComment.size() == 0)
                            ListComment.add("No Comment");
                    }
                }
                ListView LvComment = (ListView) findViewById(R.id.lvCommentPicture);
                Custom_Adapter_Comment arrayAdapter = new Custom_Adapter_Comment(getApplication().getBaseContext(),ListComment);
                LvComment.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        BtnEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visible == 0) {
                    visible = 1;
                    setEmojiconFragment(false, visible);
                } else {
                    visible = 0;
                    setEmojiconFragment(false, visible);
                }
            }
        });
        BtnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                firebase.child("CommentPicture").child(eventValue.getNameEvent().replace(" ", "&") + "*" + eventValue.getDateFrom() + "*" +
                        eventValue.getTimeFrom()+"*"+position).push().setValue(nameUser + ": " + EdtMyComment.getText().toString());
                firebase.child("CommentPicture").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (data.getKey().toString().compareTo(
                                    eventValue.getNameEvent().replace(" ", "&") + "*" + eventValue.getDateFrom() + "*" + eventValue.getTimeFrom()+"*"+position) == 0) {
                                ListComment.clear();
                                for (DataSnapshot value : data.getChildren()) {
                                    ListComment.add(value.getValue().toString());
                                }
                            }
                        }
                        ListView LvComment = (ListView) findViewById(R.id.lvCommentPicture);
                        //ArrayAdapter arrayAdapter = new ArrayAdapter(v.getContext(), android.R.layout.simple_expandable_list_item_1, ListComment);
                        Custom_Adapter_Comment arrayAdapter = new Custom_Adapter_Comment(v.getContext(), ListComment);
                        LvComment.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                EdtMyComment.setText("");
            }
        });
    }
    private void setEmojiconFragment(boolean useSystemDefault,int in) {
        if(in==1){
            frameLayout.setVisibility(View.VISIBLE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.emojiconsPicture, EmojiconsFragment.newInstance(useSystemDefault))
                    .commit();
        }
        else{
            frameLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void GiveValue(){
        bitmap=(Bitmap) getIntent().getParcelableExtra("bitmapofposition");
        position=getIntent().getStringExtra("position");
        eventValue=(EventValue)getIntent().getSerializableExtra("EventValue");
        mailUser=getIntent().getStringExtra("MailUser");
        nameUser=getIntent().getStringExtra("NameUser");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_back,menu);
        setTitle("Comment");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.backfromChangeAccount:
                Intent intent=new Intent(Picture.this,MemoryEvent.class);
                intent.putExtra("EventValue",eventValue);
                intent.putExtra("NameUser",nameUser);
                intent.putExtra("MailUser", mailUser);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(EdtMyComment);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(EdtMyComment,emojicon);
    }
}
