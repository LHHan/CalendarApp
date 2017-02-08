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


public class add_newEvent extends Fragment {
    Activity root;
    public add_newEvent()
    {

    }

    private EditText EdtNameEvent;
    private TextView TxtSetDayFrom,TxtSetTimeFrom, TxtSetDayTo, TxtSetTimeTo;
    private Button BtnSetDayFrom, BtnSetTimeFrom, BtnSetDayTo, BtnSetTimeTo, BtnFind;
    private AutoCompleteTextView ActAdress, ActAddfriend;
    private Spinner SpnRepeat;
    private View jview;
    private String DateSeclect;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       GetDateSelectFromMenuChoose();
        jview= inflater.inflate(R.layout.add_event,container,false);
        Init();
        TxtSetDayFrom.setText(DateSeclect);

        BtnSetDayFrom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment(TxtSetDayFrom);
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

       BtnSetDayTo.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {DialogFragment newFragment = new SelectDateFragment(TxtSetDayTo);newFragment.show(getFragmentManager(), "DatePicker");
               /* Bundle bundle=getActivity().getIntent().getExtras();
                if(bundle!=null)
                {
                    TxtSetDayTo.setText(bundle.getBundle("Date").toString());
                }*/}
       });

        BtnSetTimeFrom.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {DialogFragment newFragment = new SelectTimeFragment(TxtSetTimeFrom);newFragment.show(getFragmentManager(), "TimePicker");}});

        BtnSetTimeTo.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {DialogFragment newFragment = new SelectTimeFragment(TxtSetTimeTo);newFragment.show(getFragmentManager(), "TimePicker");}});

        SetdataforAdress();
        SetdataforRepeat();

        return jview;
    }

    //init value for component
    public void Init() {
        EdtNameEvent=(EditText)jview.findViewById(R.id.edtEventName);
        TxtSetDayFrom=(TextView)jview.findViewById(R.id.txtSetDayFrom);
        TxtSetTimeFrom=(TextView)jview.findViewById(R.id.txtSetTimeFrom);
        TxtSetDayTo=(TextView)jview.findViewById(R.id.txtSetDayTo);
        TxtSetTimeTo=(TextView)jview.findViewById(R.id.txtSetTimeTo);
        BtnSetDayFrom=(Button)jview.findViewById(R.id.btnSetDayFrom);
        BtnSetTimeFrom=(Button)jview.findViewById(R.id.btnsetTimeFrom);
        BtnSetDayTo=(Button)jview.findViewById(R.id.btnSetDayTo);
        BtnSetTimeTo=(Button)jview.findViewById(R.id.btnsetTimeTo);
        ActAdress=(AutoCompleteTextView)jview.findViewById(R.id.actAdress);
        ActAddfriend=(AutoCompleteTextView)jview.findViewById(R.id.actaddfriend);
        SpnRepeat=(Spinner)jview.findViewById(R.id.spnRepeat);
    }

    public void SetdataforAdress() {
        String []address=jview.getResources().getStringArray(R.array.QuanTPHCM);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),R.layout.list_item_address,address);
        ActAdress.setAdapter(arrayAdapter);
    }

    public void SetdataforRepeat() {
        final String []repeat=jview.getResources().getStringArray(R.array.Repeat);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,repeat);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        SpnRepeat.setAdapter(arrayAdapter);
    }

    public void GetDateSelectFromMenuChoose()
    {
        DateSeclect=getArguments().getString("txtsetdateTo");
    }

}
