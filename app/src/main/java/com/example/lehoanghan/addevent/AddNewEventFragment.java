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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.activity_add_event)
public class AddNewEventFragment extends Fragment {

    private View contentView;
    private String dateSeclect;
    public AddNewEventFragment() {
    }

    @ViewById(R.id.activity_add_event_et_event_name)
    EditText etEventName;
    @ViewById(R.id.activity_add_event_tv_set_day_from)
    TextView tvSetDayFrom;
    @ViewById(R.id.activity_add_event_tv_set_time_from)
    TextView tvSetTimeFrom;
    @ViewById(R.id.activity_add_event_tv_set_day_to)
    TextView tvSetDayTo;
    @ViewById(R.id.activity_add_event_tv_set_time_to)
    Button tvSetTimeTo;
    @ViewById(R.id.activity_add_event_actv_place)
    AutoCompleteTextView actvPlace;
    @ViewById(R.id.activity_add_event_mactv_add_friend)
    AutoCompleteTextView mactvAddFriend;
    @ViewById(R.id.activity_add_event_spn_repeat)
    Spinner spnRepeat;

    @AfterViews
    void init() {
        tvSetDayFrom.setText(dateSeclect);
        setDataforAdress();
        setDataforRepeat();
    }

    @Click(R.id.activity_add_event_btn_set_day_from)
    void btnSetDayFromClicked() {
        DialogFragment newFragment = new SelectDateFragment(tvSetDayFrom);
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    @Click(R.id.activity_add_event_btn_set_time_from)
    void btnSetTimeFromChecked() {
        DialogFragment newFragment = new SelectTimeFragment(tvSetTimeFrom);
        newFragment.show(getFragmentManager(), "TimePicker");
    }

    @Click(R.id.activity_add_event_btn_set_day_to)
    void btnSetDayToChecked() {
        DialogFragment newFragment = new SelectDateFragment(tvSetDayTo);
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    @Click(R.id.activity_add_event_btn_set_time_to)
    void btnSetTimeToClicked() {
        DialogFragment newFragment = new SelectTimeFragment(tvSetTimeTo);
        newFragment.show(getFragmentManager(), "TimePicker");
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
