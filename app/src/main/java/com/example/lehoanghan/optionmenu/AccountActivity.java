package com.example.lehoanghan.optionmenu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.myaccount.ChangeAccountActivity;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by lehoanghan on 3/30/2016.
 */
public class AccountActivity extends Fragment {
    private Activity activityRoot;

    private View contentView;

    private Bundle bundleGiveMailfromMenu;

    private String getMail;

    private String getName;

    private TextView tvName;

    private TextView tvMail;

    private ImageView ivAvatar;

    private Button btnChageInfo;

    private Firebase aFirebase;

    private Intent intentPassdata;

    public AccountActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        activityRoot = getActivity();
        contentView = layoutInflater.inflate(R.layout.activity_account, container, false);
        setHasOptionsMenu(true);
        giveUserfromChoose();
        Firebase.setAndroidContext(activityRoot);
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        aInit();

        tvName.setText(getName);
        tvMail.setText(getMail.replace("&", "."));
        setImage();
        btnChageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passDatatoChangeAccount();
                startActivity(intentPassdata);

            }
        });

        return contentView;
    }

    public void aInit() {
        tvName = (TextView) contentView.findViewById(R.id.activity_account_tv_name_value);
        tvMail = (TextView) contentView.findViewById(R.id.activity_account_tv_mail_value);
        ivAvatar = (ImageView) contentView.findViewById(R.id.activity_account_iv_avatar);
        btnChageInfo = (Button) contentView.findViewById(R.id.activity_account_btn_change);
    }

    public void setImage() {
        aFirebase.child("Avata").child(getMail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                byte[] getImage = Base64.decode(dataSnapshot.getValue().toString(), Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(getImage, 0, getImage.length);
                ivAvatar.setImageBitmap(bmp);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void passDatatoChangeAccount() {
        intentPassdata = new Intent(getActivity(), ChangeAccountActivity.class);
        intentPassdata.putExtra("MailUser", getMail);
        intentPassdata.putExtra("NameUser", getName);
    }

    public void giveUserfromChoose() {
        bundleGiveMailfromMenu = this.getArguments();
        if (bundleGiveMailfromMenu != null) {
            getMail = bundleGiveMailfromMenu.getString("MailforFindFriend");
            //GetMail=GetMail.replace("&", ".");
            getName = bundleGiveMailfromMenu.getString("NameforFindFriend");
            getName = getName.toLowerCase();
        }
    }
}
