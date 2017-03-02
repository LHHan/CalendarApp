package com.example.lehoanghan.list_oldevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lehoanghan.EventValue;
import com.example.lehoanghan.appcalendar.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lehoanghan on 6/25/2016.
 */
@EActivity(R.layout.activity_comment)
public class CommentActivity extends AppCompatActivity
        implements EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener, Validator.ValidationListener {
    @ViewById(R.id.activity_comment_lv_list_comment)
    ListView lvComment;

    @ViewById(R.id.activity_comment_btn_send)
    Button btnSendComment;

    @ViewById(R.id.activity_comment_btn_emojicon)
    Button btnEmoji;

    @NotEmpty(message = "You can't send empty message")
    @ViewById(R.id.activity_comment_eiet_my_comment)
    EmojiconEditText emojIconComment;

    @ViewById(R.id.activity_comment_fl_emojicon)
    FrameLayout frameLayout;

    private String nameUser;

    private String mailUser;

    private String keyComment;

    private EventValue passValue;

    private Firebase fireBase;

    private ArrayList<String> listComment;

    private int intVisible;

    private Validator validator;

    @Click(R.id.activity_comment_btn_send)
    void setBtnSendComment() {
        validator.validate();
    }

    @Click(R.id.activity_comment_btn_emojicon)
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
        giveDataUser();
        Firebase.setAndroidContext(this);
        fireBase = new Firebase("https://appcalendar.firebaseio.com/");
        setComment();
        validator = new Validator(this);
        validator.setValidationListener(this);
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
                CustomCommentAdapter arrayAdapter =
                        new CustomCommentAdapter(getApplication().getBaseContext(), listComment);
                lvComment.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    void loadComment() {
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
                        CustomCommentAdapter arrayAdapter =
                                new CustomCommentAdapter(getApplicationContext(), listComment);
                        lvComment.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
        emojIconComment.setText("");
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
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


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

    @Override
    public void onValidationSucceeded() {
        loadComment();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View contentView = error.getView();
            String message = error.getCollatedErrorMessage(this);

            //Display error message
            if (contentView instanceof EditText) {
                ((EditText) contentView).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
