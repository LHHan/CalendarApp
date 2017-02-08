package com.example.lehoanghan.list_oldevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
public class Comment extends AppCompatActivity implements EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    private String NameUser, MailUser, KeyComment;
    private EventValue PassValue;
    private Firebase firebase;
    private ArrayList<String> ListComment;
    private int visible;
    private EmojiconEditText EdtMyComment;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        GivedataUser();
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://appcalendar.firebaseio.com/");
        //Init();
        SetComment();
    }

    public void Init(){

    }

    public void SetComment(){
            visible=0;
            ListComment = new ArrayList<String>();
            firebase.child("Comment").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            String tem = PassValue.getNameEvent().replace(" ", "&") + "*" + PassValue.getDateFrom() + "*" + PassValue.getTimeFrom();
                            if (data.getKey().toString().compareTo(tem) == 0) {
                                for (DataSnapshot value : data.getChildren()) {
                                    ListComment.add(value.getValue().toString());
                                }
                                if (ListComment.size() == 0)
                                    ListComment.add("No Comment");
                            }
                        }
                        ListView LvComment = (ListView) findViewById(R.id.lvComment);
                        Custom_Adapter_Comment arrayAdapter = new Custom_Adapter_Comment(getApplication().getBaseContext(),ListComment);
                        LvComment.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                Button BtnSendComment = (Button) findViewById(R.id.btnSendComment);
                Button BtnEmoji=(Button) findViewById(R.id.btnEmoji);
                EdtMyComment = (EmojiconEditText) findViewById(R.id.edtMyComment);
                frameLayout=(FrameLayout) findViewById(R.id.emojicons);
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
                        firebase.child("Comment").child(PassValue.getNameEvent().replace(" ", "&") + "*" + PassValue.getDateFrom() + "*" +
                                PassValue.getTimeFrom()).push().setValue(NameUser + ": " + EdtMyComment.getText().toString());
                        firebase.child("Comment").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    if (data.getKey().toString().compareTo(
                                            PassValue.getNameEvent().replace(" ", "&") + "*" + PassValue.getDateFrom() + "*" + PassValue.getTimeFrom()) == 0) {
                                        ListComment.clear();
                                        for (DataSnapshot value : data.getChildren()) {
                                            ListComment.add(value.getValue().toString());
                                        }
                                    }
                                }
                                ListView LvComment = (ListView) findViewById(R.id.lvComment);
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
                Intent intent=new Intent(Comment.this,MemoryEvent.class);
                intent.putExtra("EventValue",PassValue);
                intent.putExtra("NameUser",NameUser);
                intent.putExtra("MailUser", MailUser);
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

    private void setEmojiconFragment(boolean useSystemDefault,int in) {
        if(in==1){
            frameLayout.setVisibility(View.VISIBLE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                    .commit();
        }
        else{
            frameLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void GivedataUser() {
        MailUser = getIntent().getStringExtra("MailUser");
        NameUser = getIntent().getStringExtra("NameUser");
        PassValue = (EventValue) getIntent().getSerializableExtra("EventValue");
        KeyComment=getIntent().getStringExtra("KeyaddComment");

    }
}
