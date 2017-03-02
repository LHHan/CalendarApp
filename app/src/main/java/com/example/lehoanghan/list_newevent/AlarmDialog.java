package com.example.lehoanghan.list_newevent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.choosemenu.NavigationActivity;

import org.androidannotations.annotations.EActivity;

/**
 * Created by lehoanghan on 6/15/2016.
 */
@EActivity
public class AlarmDialog extends Activity {
    private String strType = "Type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.dialog_alarm);
        final Intent INTENTSERVICE = new Intent(getBaseContext(), RingtonePlayingService.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Event");
        builder.setMessage("You have a new event: \r\nname's Event: "
                + getIntent().getStringExtra("name")
                + ".\r\nDate: " + getIntent().getStringExtra("Date") + ".\r\nTime: "
                + getIntent().getStringExtra("Time")
                + ".\r\nPlace: " + getIntent().getStringExtra("Place") + ".");
        builder.setIcon(R.drawable.ic_alarm);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                strType = "OK";
                dialog.cancel();
                INTENTSERVICE.putExtra("Type", strType);
                getBaseContext().startService(INTENTSERVICE);
                Intent intent = new Intent(AlarmDialog.this, NavigationActivity.class);
                intent.putExtra("NameUser", getIntent().getStringExtra("NameUser").toString());
                Log.e("name", getIntent().getStringExtra("NameUser"));
                intent.putExtra("MailUser", getIntent().getStringExtra("MailUser").toString());
                Log.e("mail", getIntent().getStringExtra("MailUser"));
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        INTENTSERVICE.putExtra("Type", strType);
        getBaseContext().startService(INTENTSERVICE);
    }
}
