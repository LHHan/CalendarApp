package com.example.lehoanghan.list_oldevent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by lehoanghan on 6/28/2016.
 */
@EActivity(R.layout.activity_picture)
public class PictureActivity extends AppCompatActivity
        implements EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    @ViewById(R.id.activity_picture_iv_picture)
    ImageView ivPicture;

    @ViewById(R.id.activity_picture_btn_send)
    Button btnSendComment;

    @ViewById(R.id.activity_picture_btn_emojicon)
    Button btnEmoji;

    @ViewById(R.id.activity_picture_eiet_my_comment)
    EmojiconEditText emojiconComment;

    @ViewById(R.id.activity_picture_fl_emojicon)
    FrameLayout frameLayout;

    @ViewById(R.id.activity_picture_lv_list_comment)
    ListView lvComment;

    @Extra
    ArrayList<String> listComment;

    private Bitmap aBitmap;

    private EventValue eventValue;

    private String aPosition;

    private String nameUser;

    private String mailUser;

    private String imagePath;

    private Firebase aFirebase;

    private int intVisible;

    private Uri uriImage;

    @Click(R.id.activity_picture_btn_send)
    void setBtnSendComment() {
        aFirebase.child("CommentPicture").child(eventValue.getNameEvent().replace(" ", "&")
                + "*" + eventValue.getDateFrom() +
                "*" + eventValue.getTimeFrom() +
                "*" + aPosition).push().setValue(nameUser + ": " +
                emojiconComment.getText().toString());
        aFirebase.child("CommentPicture")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (data.getKey().toString().compareTo(
                                    eventValue.getNameEvent().replace(" ", "&") +
                                            "*" + eventValue.getDateFrom() +
                                            "*" + eventValue.getTimeFrom() + "*"
                                            + aPosition) == 0) {
                                listComment.clear();
                                for (DataSnapshot value : data.getChildren()) {
                                    listComment.add(value.getValue().toString());
                                }
                            }
                        }
                        //ArrayAdapter arrayAdapter = new ArrayAdapter(v.getContext(),
                        // android.R.layout.simple_expandable_list_item_1, listComment);
                        CustomCommentAdapter arrayAdapter =
                                new CustomCommentAdapter(getApplicationContext(), listComment);
                        lvComment.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
        emojiconComment.setText("");
    }

    @Click(R.id.activity_picture_btn_emojicon)
    void setBtnEmoji() {
        if (intVisible == 0) {
            intVisible = 1;
            setEmojiconFragment(false, intVisible);
        } else {
            intVisible = 0;
            setEmojiconFragment(false, intVisible);
        }
    }

    @AfterViews
    void afterViews() {
        Firebase.setAndroidContext(this);
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        giveValue();
        initView();
        setComment();
    }

    public void initView() {
        ivPicture.setImageBitmap(aBitmap);
        ivPicture.setLayoutParams(new LinearLayout.LayoutParams(1000, 1000));
        ivPicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imagePath = MediaStore.Images.Media.insertImage(getContentResolver(), aBitmap,
                        "picture" + aPosition, aPosition);
                uriImage = Uri.parse(imagePath);
                Toast.makeText(getBaseContext(), "Save success", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    public void setComment() {
        intVisible = 0;
        listComment = new ArrayList<String>();
        aFirebase.child("CommentPicture").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String tem = eventValue.getNameEvent().replace(" ", "&") +
                            "*" + eventValue.getDateFrom() +
                            "*" + eventValue.getTimeFrom() +
                            "*" + aPosition;
                    if (data.getKey().toString().compareTo(tem) == 0) {
                        for (DataSnapshot value : data.getChildren()) {
                            listComment.add(value.getValue().toString());
                        }
                        if (listComment.size() == 0) {
                            listComment.add("No Comment");
                        }
                    }
                }
                CustomCommentAdapter arrayAdapter =
                        new CustomCommentAdapter(getApplication().getBaseContext(), listComment);
                lvComment.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void setEmojiconFragment(boolean useSystemDefault, int in) {
        if (in == 1) {
            frameLayout.setVisibility(View.VISIBLE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_picture_fl_emojicon,
                            EmojiconsFragment.newInstance(useSystemDefault))
                    .commit();
        } else {
            frameLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void giveValue() {
        aBitmap = (Bitmap) getIntent().getParcelableExtra("bitmapofposition");
        aPosition = getIntent().getStringExtra("position");
        eventValue = (EventValue) getIntent().getSerializableExtra("EventValue");
        mailUser = getIntent().getStringExtra("MailUser");
        nameUser = getIntent().getStringExtra("NameUser");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_back, menu);
        setTitle("Comment");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backfromChangeAccount:
                Intent intent = new Intent(PictureActivity.this, MemoryEventActivity.class);
                intent.putExtra("EventValue", eventValue);
                intent.putExtra("NameUser", nameUser);
                intent.putExtra("MailUser", mailUser);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(emojiconComment);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(emojiconComment, emojicon);
    }
}
