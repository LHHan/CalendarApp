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
import com.example.lehoanghan.myaccount.ChangeAccount;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by lehoanghan on 3/30/2016.
 */
public class Account extends Fragment {

    Activity root;
    private View jview;
    public Account(){}

    private Bundle bundleGiveMailfromMenu;
    private String GetMail,GetName;
    private TextView TxtName, TxtMail;
    private ImageView ImgAvata;
    private Button BtnChageInfo;
    private Firebase firebase;
    private Intent intentPassdata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root=getActivity();
        jview = inflater.inflate(R.layout.screen_account, container, false);
        setHasOptionsMenu(true);
        GiveUserfromChoose();
        Firebase.setAndroidContext(root);
        firebase =new Firebase("https://appcalendar.firebaseio.com/");
        Init();

        TxtName.setText(GetName);
        TxtMail.setText(GetMail.replace("&","."));
        SetImage();
        BtnChageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PassDatatoChangeAccount();
                startActivity(intentPassdata);

            }
        });

        return jview;
    }


    public void Init(){
        TxtName=(TextView) jview.findViewById(R.id.txtName);
        TxtMail=(TextView) jview.findViewById(R.id.txtMail);
        ImgAvata=(ImageView) jview.findViewById(R.id.imgAvata);
        BtnChageInfo=(Button) jview.findViewById(R.id.btnchangeInfo);
    }

    public void SetImage(){
        firebase.child("Avata").child(GetMail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                byte[] getImage= Base64.decode(dataSnapshot.getValue().toString(), Base64.DEFAULT);
                Bitmap bmp= BitmapFactory.decodeByteArray(getImage, 0, getImage.length);
                ImgAvata.setImageBitmap(bmp);
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



    public void PassDatatoChangeAccount()
    {
        intentPassdata=new Intent(getActivity(), ChangeAccount.class);
        intentPassdata.putExtra("MailUser",GetMail);
        intentPassdata.putExtra("NameUser",GetName);
    }

    public void GiveUserfromChoose(){
        bundleGiveMailfromMenu=this.getArguments();
        if(bundleGiveMailfromMenu!=null) {
            GetMail = bundleGiveMailfromMenu.getString("MailforFindFriend");
            //GetMail=GetMail.replace("&", ".");
            GetName=bundleGiveMailfromMenu.getString("NameforFindFriend");
            GetName=GetName.toLowerCase();
        }
    }

}
