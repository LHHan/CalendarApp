package com.example.lehoanghan.addevent;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lehoanghan.addevent.SelectDateFragment;
import com.example.lehoanghan.addevent.SelectTimeFragment;
import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.choosemenu.Menu_Choose;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AddEvent extends AppCompatActivity {


    private EditText EdtNameEvent,EdtDecription,EdtAlarm;
    private TextView TxtSetDayFrom, TxtSetTimeFrom, TxtSetDayTo, TxtSetTimeTo;
    private Button BtnSetDayFrom, BtnSetTimeFrom, BtnSetDayTo, BtnSetTimeTo, BtnFind;
    private AutoCompleteTextView ActAdress;
    private MultiAutoCompleteTextView ActAddfriend;
    private Spinner SpnRepeat;
    private String DateSeclect,MailUser,NameUser,ToDay;
    private Firebase firebasefriend;
    private List<String> listFriendinFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GetDateSelectFromHome();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        Firebase.setAndroidContext(this);
        firebasefriend = new Firebase("https://appcalendar.firebaseio.com/");
        Init();
        TxtSetDayFrom.setText(DateSeclect);

        BtnSetDayFrom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment(TxtSetDayFrom);
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        BtnSetDayTo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment(TxtSetDayTo);
                newFragment.show(getFragmentManager(), "DatePicker");
               /* Bundle bundle=getActivity().getIntent().getExtras();
                if(bundle!=null)
                {
                    TxtSetDayTo.setText(bundle.getBundle("Date").toString());
                }*/
            }
        });

        BtnSetTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectTimeFragment(TxtSetTimeFrom);
                newFragment.show(getFragmentManager(), "TimePicker");
            }
        });

        BtnSetTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectTimeFragment(TxtSetTimeTo);
                newFragment.show(getFragmentManager(), "TimePicker");
            }
        });

        SetdataforAdress();
        SetdataforRepeat();
        SetDataforAddfriend();
        ButtonAddFriend();

    }



    //init value for component
    public void Init() {
        EdtNameEvent = (EditText) findViewById(R.id.edtEventName);
        EdtDecription=(EditText) findViewById(R.id.edtDescription);
        EdtAlarm=(EditText) findViewById(R.id.edtAlarm);
        TxtSetDayFrom = (TextView) findViewById(R.id.txtSetDayFrom);
        TxtSetTimeFrom = (TextView) findViewById(R.id.txtSetTimeFrom);
        TxtSetDayTo = (TextView) findViewById(R.id.txtSetDayTo);
        TxtSetTimeTo = (TextView) findViewById(R.id.txtSetTimeTo);
        BtnSetDayFrom = (Button) findViewById(R.id.btnSetDayFrom);
        BtnSetTimeFrom = (Button) findViewById(R.id.btnsetTimeFrom);
        BtnSetDayTo = (Button) findViewById(R.id.btnSetDayTo);
        BtnSetTimeTo = (Button) findViewById(R.id.btnsetTimeTo);
        ActAdress = (AutoCompleteTextView) findViewById(R.id.actAdress);
        ActAddfriend = (MultiAutoCompleteTextView) findViewById(R.id.actaddfriend);
        SpnRepeat = (Spinner) findViewById(R.id.spnRepeat);
        BtnFind=(Button) findViewById(R.id.btnfindfriend);
    }

    public void SetdataforAdress() {
        String[] address = getResources().getStringArray(R.array.QuanTPHCM);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item_address, address);
        ActAdress.setAdapter(arrayAdapter);
    }
    public void SetDataforAddfriend()
    {
        final List<String> listFriend=new ArrayList<String>();
        firebasefriend.child("My_friend").child(MailUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren())
                    listFriend.add(Snapshot.getKey().toString().replace("&", "."));

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listFriend);
                ActAddfriend.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                ActAddfriend.setThreshold(1);
                ActAddfriend.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void SetdataforRepeat() {
        final String[] repeat = getResources().getStringArray(R.array.Repeat);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, repeat);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        SpnRepeat.setAdapter(arrayAdapter);
    }
    public void GetDateSelectFromHome() {
        DateSeclect = getIntent().getStringExtra("ChangeDate");
        MailUser = getIntent().getStringExtra("MailUser");
        NameUser = getIntent().getStringExtra("NameUser");
        ToDay = getIntent().getStringExtra("ToDay");
    }

    private MenuInflater menuInflater;
    private Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater=getMenuInflater();
        this.menu=menu;
        menuInflater.inflate(R.menu.menu_add_newevent, menu);
        setTitle("Add Event");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.done:
                //SaveEventinFirebase();
                CheckEntry();
                break;
            case R.id.backfromEvent:
                Intent intent=new Intent(AddEvent.this, Menu_Choose.class);
                intent.putExtra("NameUserfromAddEvent",NameUser);
                intent.putExtra("NameUser",NameUser);
                intent.putExtra("MailUser",MailUser);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    public void ButtonAddFriend(){
        BtnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(AddEvent.this);
                dialog.setContentView(R.layout.dialog_addfriend);
                dialog.setTitle("My friend");
                dialog.setCancelable(true);
                final ListView listMyFriendDialog = (ListView) dialog.findViewById(R.id.lstButtonAdd);
                final List<String> listFriend = new ArrayList<String>();
                firebasefriend.child("My_friend").child(MailUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot Snapshot : dataSnapshot.getChildren())
                            listFriend.add(Snapshot.getKey().toString().replace("&", "."));

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_address, listFriend);
                        listMyFriendDialog.setAdapter(arrayAdapter);
                        dialog.show();
                        listMyFriendDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ActAddfriend.setText(ActAddfriend.getText() + listFriend.get(position) + "," + " ");
                            }
                        });
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        });
    }
    public void SaveEventinFirebase(){
        String listinvite="";
        String[] listFriendInvite= ActAddfriend.getText().toString().split(",");
        listFriendinFirebase=new ArrayList<String>();
        listFriendinFirebase.add(MailUser);
        for(int i=0;i<listFriendInvite.length;i++)
        {
            listFriendinFirebase.add(listFriendInvite[i].trim().replace(".", "&"));
        }

        for(int i=0;i<listFriendinFirebase.size();i++)
        {
            if(i==(listFriendinFirebase.size()-1))
            {
                listinvite+=listFriendinFirebase.get(i);
            }
            else
            listinvite+=listFriendinFirebase.get(i)+",";
        }
        /*EventValue eventValue=new EventValue(EdtNameEvent.getText().toString(),TxtSetDayFrom.getText().toString(),
                TxtSetTimeFrom.getText().toString(),TxtSetDayTo.getText().toString(),TxtSetTimeTo.getText().toString(),
                EdtDecription.getText().toString(), ActAdress.getText().toString(),ActAddfriend.getText().toString(),
                EdtAlarm.getText().toString(), SpnRepeat.getSelectedItem().toString());*/
        Map<String,String> eventValue=new Hashtable<String,String>();
        eventValue.put("NameEvent",EdtNameEvent.getText().toString());
        eventValue.put("DateFrom",TxtSetDayFrom.getText().toString());
        eventValue.put("TimeFrom",TxtSetTimeFrom.getText().toString());
        eventValue.put("DateTo",TxtSetDayTo.getText().toString());
        eventValue.put("TimeTo", TxtSetTimeTo.getText().toString());
        eventValue.put("Description",EdtDecription.getText().toString());
        eventValue.put("Place",ActAdress.getText().toString());
        eventValue.put("FriendInvite",listinvite);
        eventValue.put("Alarm",EdtAlarm.getText().toString());
        eventValue.put("Repeat",SpnRepeat.getSelectedItem().toString());

        //Map<String,Map<String,String>> mapEvent=new Hashtable<String, Map<String, String>>();
        //mapEvent.put(EdtNameEvent.getText().toString(),eventValue);
//        Map<String,Object> mapEvent=new HashMap<String,Object>();
//        mapEvent.put(EdtNameEvent.getText().toString() + ", " + TxtSetDayFrom.getText().toString() + ", " + TxtSetTimeFrom.getText().toString(),
//                new EventValue(EdtNameEvent.getText().toString(), TxtSetDayFrom.getText().toString(),
//                        TxtSetTimeFrom.getText().toString(), TxtSetDayTo.getText().toString(), TxtSetTimeTo.getText().toString(),
//                        EdtDecription.getText().toString(), ActAdress.getText().toString(), ActAddfriend.getText().toString(),
//                        EdtAlarm.getText().toString(), SpnRepeat.getSelectedItem().toString()));

        for(String mail:listFriendinFirebase){
            firebasefriend.child("Event").child(mail).child("New_Event").push().setValue(eventValue);
        }
        //Entry();
        Toast.makeText(getApplicationContext(),"Congratulation",Toast.LENGTH_LONG).show();
    }

    public void CheckNgay(){
        String error=null;
       if(ToDay.replace("-","a").compareTo(TxtSetDayFrom.getText().toString().replace("-","a"))<=0)
        {
            if(TxtSetDayFrom.getText().toString().replace("-","a").compareTo(
                    TxtSetDayTo.getText().toString().replace("-","a"))<0) {
                SaveEventinFirebase();
                Entry();
            }

            else if(TxtSetDayFrom.getText().toString().replace("-","a").compareTo(
                    TxtSetDayTo.getText().toString().replace("-","a"))==0)
            {
                if(TxtSetTimeFrom.getText().toString().replace(":","a").compareTo(TxtSetTimeTo.getText().toString().replace(":","a"))<0)
                {
                    SaveEventinFirebase();
                    Entry();
                }
                else
                    error="error about time to ";
            }
            else
                error="error about date to";

        }
        else
        {
            error="error about date from";
        }
        if(error!=null)
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
    }

    public void CheckEntry(){
        String error="";
        if((EdtNameEvent.getText().toString().compareTo("")==0)||(TxtSetDayFrom.getText().toString().compareTo("")==0)||
                (TxtSetTimeFrom.getText().toString().compareTo("")==0)||(TxtSetDayTo.getText().toString().compareTo("")==0)||
                (TxtSetTimeTo.getText().toString().compareTo("")==0)||(EdtDecription.getText().toString().compareTo("")==0)||
                (ActAdress.getText().toString().compareTo("")==0)||
                (EdtAlarm.getText().toString().compareTo("")==0)|| (SpnRepeat.getSelectedItem().toString().compareTo("")==0)) {
            error = "error entry data";
        }
        else
        {
            CheckNgay();
        }
        if(error.compareTo("")!=0)
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
    }

    public void Entry(){
        EdtNameEvent.setText("");
        TxtSetDayFrom.setText("");
        TxtSetTimeFrom.setText("");
        TxtSetDayTo.setText("");
        TxtSetTimeTo.setText("");
        EdtDecription.setText("");
        ActAdress.setText("");
        ActAddfriend.setText("");
        EdtAlarm.setText("");
        SpnRepeat.setSelection(0);
    }

}



