package com.example.lehoanghan.addevent;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lehoanghan.appcalendar.R;

public class AddNewEventActivity extends Fragment {
    private Activity activityRoot;

    private EditText etEventName;

    private TextView tvSetDayFrom;

    private TextView tvSetTimeFrom;

    private TextView tvSetDayTo;

    private TextView tvSetTimeTo;

    private Button btnSetDayFrom;

    private Button btnSetTimeFrom;

    private Button btnSetDayTo;

    private Button btnSetTimeTo;

    private Button btnFind;

    private AutoCompleteTextView actvPlace;

    private AutoCompleteTextView actvAddFriend;

    private Spinner spnRepeat;

    private View contentView;

    private String dateSeclect;

    public AddNewEventActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDateSelectFromMenuChoose();
        contentView = inflater.inflate(R.layout.activity_add_event, container, false);
        aInit();
        tvSetDayFrom.setText(dateSeclect);
        btnSetDayFrom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment(tvSetDayFrom);
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        btnSetDayTo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment(tvSetDayTo);
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        btnSetTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectTimeFragment(tvSetTimeFrom);
                newFragment.show(getFragmentManager(), "TimePicker");
            }
        });
        btnSetTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectTimeFragment(tvSetTimeTo);
                newFragment.show(getFragmentManager(), "TimePicker");
            }
        });
        setDataforAdress();
        setDataforRepeat();
        return contentView;
    }

    //aInit value for component
    public void aInit() {
        etEventName =
                (EditText) contentView.findViewById(R.id.activity_add_event_et_event_name);
        tvSetDayFrom =
                (TextView) contentView.findViewById(R.id.activity_add_event_tv_set_day_from);
        tvSetTimeFrom =
                (TextView) contentView.findViewById(R.id.activity_add_event_tv_set_time_from);
        tvSetDayTo =
                (TextView) contentView.findViewById(R.id.activity_add_event_tv_set_day_to);
        tvSetTimeTo =
                (TextView) contentView.findViewById(R.id.activity_add_event_tv_set_time_to);
        btnSetDayFrom =
                (Button) contentView.findViewById(R.id.activity_add_event_btn_set_day_from);
        btnSetTimeFrom =
                (Button) contentView.findViewById(R.id.activity_add_event_btn_set_time_from);
        btnSetDayTo =
                (Button) contentView.findViewById(R.id.activity_add_event_btn_set_day_to);
        btnSetTimeTo =
                (Button) contentView.findViewById(R.id.activity_add_event_btn_set_time_to);
        actvPlace =
                (AutoCompleteTextView) contentView.findViewById(R.id.activity_add_event_actv_place);
        actvAddFriend =
                (AutoCompleteTextView) contentView.findViewById(
                        R.id.activity_add_event_mactv_add_friend);
        spnRepeat =
                (Spinner) contentView.findViewById(R.id.activity_add_event_spn_repeat);
    }

    public void setDataforAdress() {
        String[] address = contentView.getResources().getStringArray(R.array.District);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getActivity(), R.layout.adapter_place, address);
        actvPlace.setAdapter(arrayAdapter);
    }

    public void setDataforRepeat() {
        final String[] REPEAT = contentView.getResources().getStringArray(R.array.Repeat);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this.getActivity(),
                        android.R.layout.simple_spinner_item, REPEAT);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnRepeat.setAdapter(arrayAdapter);
    }

    public void getDateSelectFromMenuChoose() {
        dateSeclect = getArguments().getString("txtsetdateTo");
    }

}
