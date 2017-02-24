package com.example.lehoanghan.optionmenu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.myaccount.ChangeAccountActivity;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by lehoanghan on 3/30/2016.
 */
@EFragment(R.layout.fragment_account)
public class AccountFragment extends Fragment {
    private Activity activityRoot;

    @ViewById(R.id.fragment_account_tv_name_value)
    TextView getTvName;

    @ViewById(R.id.fragment_account_tv_mail_value)
    TextView getTvMail;

    @ViewById(R.id.fragment_account_iv_avatar)
    ImageView getIvAvatar;

    @ViewById(R.id.fragment_account_btn_change)
    Button getBtnChageInfo;

    private Bundle bundleGiveMailfromMenu;

    private String getMail;

    private String getName;

    private Firebase aFirebase;

    private Intent intentPassdata;

    public AccountFragment() {
    }

    @Click(R.id.fragment_account_btn_change)
    public void setBtnChangeInfo() {
        passDatatoChangeAccount();
        startActivity(intentPassdata);
    }


    @AfterViews
    public void afterView() {
        activityRoot = getActivity();
        setHasOptionsMenu(true);
        giveUserfromChoose();
        Firebase.setAndroidContext(activityRoot);
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        getTvName.setText(getName);
        getTvMail.setText(getMail.replace("&", "."));
        setImage();
    }

    public void setImage() {
        aFirebase.child("Avata").child(getMail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getIvAvatar.setImageResource(R.drawable.avt_default);
//                byte[] getImage = Base64.decode(dataSnapshot.getValue().toString(), Base64.DEFAULT);
//                Bitmap bmp = BitmapFactory.decodeByteArray(getImage, 0, getImage.length);
//                getIvAvatar.setImageBitmap(bmp);
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
            getName = bundleGiveMailfromMenu.getString("NameforFindFriend");
            getName = getName.toLowerCase();
        }
    }
}
