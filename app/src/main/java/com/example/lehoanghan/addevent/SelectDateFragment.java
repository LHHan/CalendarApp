package com.example.lehoanghan.addevent;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SelectDateFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private TextView tvSelectDate;

    public SelectDateFragment(TextView a) {
        tvSelectDate = a;
    }

    public SelectDateFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar CALENDAR = Calendar.getInstance();
        int yy = CALENDAR.get(Calendar.YEAR);
        int mm = CALENDAR.get(Calendar.MONTH);
        int dd = CALENDAR.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = sdf.format(c.getTime());
        //TextView TxtSetDayTo=(TextView)getActivity().findViewById(link);
        tvSelectDate.setText(strDate);
        /*Intent intent = new Intent(getActivity().getBaseContext(), AddNewEventFragment.class);
        intent.putExtra("Date", Date);
        startActivity(intent);*/
    }

}

