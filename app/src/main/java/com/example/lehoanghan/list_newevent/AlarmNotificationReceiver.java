package com.example.lehoanghan.list_newevent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context.getApplicationContext(),"OK",Toast.LENGTH_LONG);
        Intent in = new Intent(context, AlarmDialog.class);
        in.putExtra("Name", intent.getStringExtra("Name"));
        in.putExtra("Date", intent.getStringExtra("Date"));
        in.putExtra("Time", intent.getStringExtra("Time"));
        in.putExtra("Place", intent.getStringExtra("Place"));
        in.putExtra("NameUser", intent.getStringExtra("NameUser"));
        Log.e("Name", intent.getStringExtra("NameUser"));
        in.putExtra("MailUser", intent.getStringExtra("MailUser"));
        Log.e("Mail", intent.getStringExtra("MailUser"));
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(in);

    }
}
