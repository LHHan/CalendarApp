package com.example.lehoanghan.addevent;

import android.app.Dialog;
import android.app.DialogFragment;
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

import com.example.lehoanghan.appcalendar.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_add_event)
public class AddEventActivity extends AppCompatActivity implements Validator.ValidationListener {
    @NotEmpty
    @ViewById(R.id.activity_add_event_et_event_name)
    EditText etEventName;

    @NotEmpty
    @ViewById(R.id.activity_add_event_et_description)
    EditText etDescription;

    @NotEmpty
    @ViewById(R.id.activity_add_event_et_alarm)
    EditText etAlarm;

    @NotEmpty
    @ViewById(R.id.activity_add_event_tv_set_day_from)
    TextView tvSetDayFrom;

    @NotEmpty
    @ViewById(R.id.activity_add_event_tv_set_time_from)
    TextView tvSetTimeFrom;

    @NotEmpty
    @ViewById(R.id.activity_add_event_tv_set_day_to)
    TextView tvSetDayTo;

    @NotEmpty
    @ViewById(R.id.activity_add_event_tv_set_time_to)
    TextView tvSetTimeTo;

    @NotEmpty
    @ViewById(R.id.activity_add_event_btn_set_day_from)
    Button btnSetDayFrom;

    @NotEmpty
    @ViewById(R.id.activity_add_event_btn_set_time_from)
    Button btnSetTimeFrom;

    @NotEmpty
    @ViewById(R.id.activity_add_event_btn_set_day_to)
    Button btnSetDayTo;

    @NotEmpty
    @ViewById(R.id.activity_add_event_btn_set_time_to)
    Button btnSetTimeTo;

    @NotEmpty
    @ViewById(R.id.activity_add_event_actv_place)
    AutoCompleteTextView actvPlace;

    @NotEmpty
    @ViewById(R.id.activity_add_event_mactv_add_friend)
    MultiAutoCompleteTextView mactvAddfriend;

    @NotEmpty
    @ViewById(R.id.activity_add_event_spn_repeat)
    Spinner spnRepeat;

    @ViewById(R.id.activity_add_event_btn_find_friend)
    Button btnFind;

    private String dateSeclect;

    private String mailUser;

    private String nameUser;

    private String toDay;

    private Firebase firebaseFriend;

    private List<String> listFriendinFirebase;

    private MenuInflater menuInflater;

    private Menu contentMenu;

    private Validator validator;

    @Click(R.id.activity_add_event_btn_set_day_from)
    void setBtnSetDayFrom() {
        DialogFragment newFragment = new SelectDateFragment(tvSetDayFrom);
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    @Click(R.id.activity_add_event_btn_set_time_from)
    void setBtnSetTimeFrom() {
        DialogFragment newFragment = new SelectDateFragment(tvSetDayTo);
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    @Click(R.id.activity_add_event_btn_set_day_to)
    void setBtnSetDayTo() {
        DialogFragment newFragment = new SelectTimeFragment(tvSetTimeFrom);
        newFragment.show(getFragmentManager(), "TimePicker");
    }

    @Click(R.id.activity_add_event_btn_set_time_to)
    void setBtnSetTimeTo() {
        DialogFragment newFragment = new SelectTimeFragment(tvSetTimeTo);
        newFragment.show(getFragmentManager(), "TimePicker");
    }

    @Click(R.id.activity_add_event_btn_find_friend)
    void setBtnFind() {
        final Dialog DIALOG = new Dialog(AddEventActivity.this);
        DIALOG.setContentView(R.layout.dialog_add_friend);
        DIALOG.setTitle("My friend");
        DIALOG.setCancelable(true);
        final ListView LISTMYFRIENDDIALOG =
                (ListView) DIALOG.findViewById(R.id.dialog_add_friend_lv_main);
        final List<String> LISTFRIEND = new ArrayList<String>();
        firebaseFriend.child("My_friend").child(mailUser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                            LISTFRIEND.add(snapShot.getKey().toString().replace("&", "."));
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getApplicationContext(),
                                R.layout.adapter_place, LISTFRIEND);
                        LISTMYFRIENDDIALOG.setAdapter(arrayAdapter);
                        DIALOG.show();
                        LISTMYFRIENDDIALOG.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent,
                                                            View view,
                                                            int position, long id) {
                                        mactvAddfriend.setText(mactvAddfriend.getText()
                                                + LISTFRIEND.get(position) + "," + " ");
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });

    }

    @AfterViews
    void afterViews() {
        getDateSelectFromHome();
        Firebase.setAndroidContext(this);
        firebaseFriend = new Firebase("https://appcalendar.firebaseio.com/");
        tvSetDayFrom.setText(dateSeclect);
        setDataForPlace();
        setDataForRepeat();
        setDataForAddfriend();
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    public void setDataForPlace() {
        String[] place = getResources().getStringArray(R.array.District);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.adapter_place, place);
        actvPlace.setAdapter(arrayAdapter);
    }

    public void setDataForAddfriend() {
        final List<String> LISTFRIEND = new ArrayList<String>();
        firebaseFriend.child("My_friend").child(mailUser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                            LISTFRIEND.add(snapShot.getKey().toString().replace("&", "."));
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getApplicationContext(),
                                android.R.layout.simple_spinner_item, LISTFRIEND);
                        mactvAddfriend.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                        mactvAddfriend.setThreshold(1);
                        mactvAddfriend.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
    }

    public void setDataForRepeat() {
        final String[] REPEAT = getResources().getStringArray(R.array.Repeat);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, REPEAT);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnRepeat.setAdapter(arrayAdapter);
    }

    public void getDateSelectFromHome() {
        dateSeclect = getIntent().getStringExtra("ChangeDate");
        mailUser = getIntent().getStringExtra("MailUser");
        nameUser = getIntent().getStringExtra("NameUser");
        toDay = getIntent().getStringExtra("ToDay");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater = getMenuInflater();
        this.contentMenu = menu;
        menuInflater.inflate(R.menu.menu_add_event, menu);
        setTitle("Add Event");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_event_done:
                //saveEventinFirebase();
                validator.validate();
                break;
            case R.id.menu_add_event_back:
//                Intent intent = new Intent(AddEventActivity.this, NavigationActivity.class);
//                intent.putExtra("NameUserfromAddEvent", nameUser);
//                intent.putExtra("NameUser", nameUser);
//                intent.putExtra("MailUser", mailUser);
//                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveEventinFirebase() {
        String listinvite = "";
        String[] listFriendInvite = mactvAddfriend.getText().toString().split(",");
        listFriendinFirebase = new ArrayList<String>();
        listFriendinFirebase.add(mailUser);
        for (int i = 0; i < listFriendInvite.length; i++) {
            listFriendinFirebase.add(listFriendInvite[i].trim().replace(".", "&"));
        }
        for (int i = 0; i < listFriendinFirebase.size(); i++) {
            if (i == (listFriendinFirebase.size() - 1)) {
                listinvite += listFriendinFirebase.get(i);
            } else {
                listinvite += listFriendinFirebase.get(i) + ",";
            }
        }
        Map<String, String> eventValue = new Hashtable<String, String>();
        eventValue.put("nameEvent", etEventName.getText().toString());
        eventValue.put("dateFrom", tvSetDayFrom.getText().toString());
        eventValue.put("timeFrom", tvSetTimeFrom.getText().toString());
        eventValue.put("dateTo", tvSetDayTo.getText().toString());
        eventValue.put("timeTo", tvSetTimeTo.getText().toString());
        eventValue.put("description", etDescription.getText().toString());
        eventValue.put("place", actvPlace.getText().toString());
        eventValue.put("friendInvite", listinvite);
        eventValue.put("alarm", etAlarm.getText().toString());
        eventValue.put("repeat", spnRepeat.getSelectedItem().toString());

        for (String mail : listFriendinFirebase) {
            firebaseFriend.child("Event").child(mail)
                    .child("New_Event").push().setValue(eventValue);
        }
        //entryActivity();
        Toast.makeText(getApplicationContext(), "Congratulation", Toast.LENGTH_LONG).show();
    }

    public void checkDay() {
        String error = null;
        if (toDay.replace("-", "a").compareTo(tvSetDayFrom.getText()
                .toString().replace("-", "a")) <= 0) {
            if (tvSetDayFrom.getText().toString().replace("-", "a").compareTo(
                    tvSetDayTo.getText().toString().replace("-", "a")) < 0) {
                saveEventinFirebase();
                entryActivity();
            } else if (tvSetDayFrom.getText().toString().replace("-", "a").compareTo(
                    tvSetDayTo.getText().toString().replace("-", "a")) == 0) {
                if (tvSetTimeFrom.getText().toString().replace(":", "a")
                        .compareTo(tvSetTimeTo.getText().toString().replace(":", "a")) < 0) {
                    saveEventinFirebase();
                    entryActivity();
                } else {
                    error = "error about time to ";
                }
            } else {
                error = "error about date to";
            }
        } else {
            error = "error about date from";
        }
        if (error != null) {
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
        }
    }

    public void entryActivity() {
        etEventName.setText("");
        tvSetDayFrom.setText("");
        tvSetTimeFrom.setText("");
        tvSetDayTo.setText("");
        tvSetTimeTo.setText("");
        etDescription.setText("");
        actvPlace.setText("");
        mactvAddfriend.setText("");
        etAlarm.setText("");
        spnRepeat.setSelection(0);
    }

    @Override
    public void onValidationSucceeded() {
        checkDay();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View contentView = error.getView();
            String message = error.getCollatedErrorMessage(this);

            //Display error message
            if (contentView instanceof EditText) {
                ((EditText) contentView).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}



