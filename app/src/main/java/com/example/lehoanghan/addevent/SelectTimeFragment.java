package com.example.lehoanghan.addevent;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class SelectTimeFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    private TextView tvSelectTime;

    public SelectTimeFragment(TextView a) {
        tvSelectTime = a;
    }

    public SelectTimeFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar CALENDAR = Calendar.getInstance();
        int hour = CALENDAR.get(Calendar.HOUR_OF_DAY);
        int minute = CALENDAR.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(hourOfDay, minute);
        StringBuilder sbDate = new StringBuilder().append(hourOfDay).append(":").append(minute);
        //TextView TxtSetDayTo=(TextView)getActivity().findViewById(link);
        tvSelectTime.setText(sbDate);
    }
}
