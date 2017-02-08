package com.example.lehoanghan.addevent;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


public class SelectTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    private TextView txt;
    public SelectTimeFragment(TextView a) { txt=a;}
    public SelectTimeFragment(){
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour,minute,true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(hourOfDay, minute);
        StringBuilder Date = new StringBuilder().append(hourOfDay).append(":").append(minute);
        //TextView TxtSetDayTo=(TextView)getActivity().findViewById(link);
        txt.setText(Date);
    }
}
