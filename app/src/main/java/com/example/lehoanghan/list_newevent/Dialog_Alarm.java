package com.example.lehoanghan.list_newevent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.choosemenu.Menu_Choose;

/**
 * Created by lehoanghan on 6/15/2016.
 */
public class Dialog_Alarm extends Activity {
    private String type="Type";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.dialog_alarm);
        final Intent in_service=new Intent(getBaseContext(),RingtonePlayingService.class);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Event");
        builder.setMessage("Ban co 1 event: \r\nName's Event: " + getIntent().getStringExtra("Name") +
                ".\r\nDate: " + getIntent().getStringExtra("Date") + ".\r\nTime: " + getIntent().getStringExtra("Time") +
                ".\r\nPlace: " + getIntent().getStringExtra("Place") + ".");
        builder.setIcon(R.drawable.alarm);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                type = "OK";
                dialog.cancel();
                in_service.putExtra("type", type);
                getBaseContext().startService(in_service);
                Intent intent=new Intent(Dialog_Alarm.this, Menu_Choose.class);
                intent.putExtra("NameUser",getIntent().getStringExtra("NameUser").toString());
                Log.e("Name", getIntent().getStringExtra("NameUser"));
                intent.putExtra("MailUser", getIntent().getStringExtra("MailUser").toString());
                Log.e("Mail", getIntent().getStringExtra("MailUser"));
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        in_service.putExtra("type", type);
        getBaseContext().startService(in_service);
    }
}
