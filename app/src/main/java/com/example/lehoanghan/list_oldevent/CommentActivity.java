package com.example.lehoanghan.list_oldevent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

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
 * Created by lehoanghan on 6/25/2016.
 */
public class CommentActivity extends AppCompatActivity
        implements EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    private String nameUser;

    private String mailUser;

    private String keyComment;

    private EventValue passValue;

    private Firebase fireBase;

    private ArrayList<String> listComment;

    private int intVisible;

    private EmojiconEditText emojIconComment;

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        giveDataUser();
        Firebase.setAndroidContext(this);
        fireBase = new Firebase("https://appcalendar.firebaseio.com/");
        setComment();
    }

    public void setComment() {
        intVisible = 0;
        listComment = new ArrayList<String>();
        fireBase.child("Comment").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String tem = passValue.getNameEvent().replace(" ", "&")
                            + "*" + passValue.getDateFrom() + "*" + passValue.getTimeFrom();
                    if (data.getKey().toString().compareTo(tem) == 0) {
                        for (DataSnapshot value : data.getChildren()) {
                            listComment.add(value.getValue().toString());
                        }
                        if (listComment.size() == 0) {
                            listComment.add("No Comment");
                        }
                    }
                }
                ListView lvComment = (ListView) findViewById(R.id.activity_comment_lv_list_comment);
                CustomCommentAdapter arrayAdapter =
                        new CustomCommentAdapter(getApplication().getBaseContext(), listComment);
                lvComment.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        Button btnSendComment = (Button) findViewById(R.id.activity_comment_btn_send);
        Button btnEmoji = (Button) findViewById(R.id.activity_comment_btn_emojicon);
        emojIconComment = (EmojiconEditText) findViewById(R.id.activity_comment_eiet_my_comment);
        frameLayout = (FrameLayout) findViewById(R.id.activity_comment_fl_emojicon);
        btnEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intVisible == 0) {
                    intVisible = 1;
                    setEmojiconFragment(false, intVisible);
                } else {
                    intVisible = 0;
                    setEmojiconFragment(false, intVisible);
                }
            }
        });
        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                fireBase.child("Comment").child(passValue.getNameEvent().replace(" ", "&")
                        + "*" + passValue.getDateFrom() + "*"
                        + passValue.getTimeFrom()).push().setValue(nameUser + ": "
                        + emojIconComment.getText().toString());
                fireBase.child("Comment")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    if (data.getKey().toString().compareTo(
                                            passValue.getNameEvent().replace(" ", "&")
                                                    + "*" + passValue.getDateFrom()
                                                    + "*" + passValue.getTimeFrom()) == 0) {
                                        listComment.clear();
                                        for (DataSnapshot value : data.getChildren()) {
                                            listComment.add(value.getValue().toString());
                                        }
                                    }
                                }
                                ListView lvComment =
                                        (ListView) findViewById(
                                                R.id.activity_comment_lv_list_comment);
                                CustomCommentAdapter arrayAdapter =
                                        new CustomCommentAdapter(v.getContext(), listComment);
                                lvComment.setAdapter(arrayAdapter);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                emojIconComment.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_back, menu);
        setTitle("Comment");
        return super.onCreateOptionsMenu(menu);
    }
    //@Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.backfromChangeAccount:
//                Intent intent = new Intent(CommentActivity.this, MemoryEventActivity.class);
//                intent.putExtra("EventValue", passValue);
//                intent.putExtra("NameUser", nameUser);
//                intent.putExtra("MailUser", mailUser);
//                startActivity(intent);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(emojIconComment);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(emojIconComment, emojicon);
    }

    private void setEmojiconFragment(boolean useSystemDefault, int in) {
        if (in == 1) {
            frameLayout.setVisibility(View.VISIBLE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_comment_fl_emojicon,
                            EmojiconsFragment.newInstance(useSystemDefault))
                    .commit();
        } else {
            frameLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void giveDataUser() {
        mailUser = getIntent().getStringExtra("MailUser");
        nameUser = getIntent().getStringExtra("NameUser");
        passValue = (EventValue) getIntent().getSerializableExtra("EventValue");
        keyComment = getIntent().getStringExtra("KeyaddComment");

    }
}
