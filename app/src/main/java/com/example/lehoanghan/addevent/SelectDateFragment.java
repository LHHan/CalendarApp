package com.example.lehoanghan.addevent;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private TextView txt;

    public SelectDateFragment(TextView a) {
        txt = a;
    }

    public SelectDateFragment() {
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String Date = sdf.format(c.getTime());
        //TextView TxtSetDayTo=(TextView)getActivity().findViewById(link);
        txt.setText(Date);
        /*Intent intent = new Intent(getActivity().getBaseContext(), AddNewEventActivity.class);
        intent.putExtra("Date", Date);
        startActivity(intent);*/
    }


}

