package com.example.lehoanghan.appcalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.lehoanghan.choosemenu.Menu_Choose;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {
    @ViewById(R.id.activity_login_et_gmail)
    EditText etMail;

    @ViewById(R.id.activity_login_et_password)
    EditText etPass;

    @ViewById(R.id.activity_login_btn_register)
    Button btnRegister;

    @ViewById(R.id.activity_login_btn_login)
    Button btnLogin;

    private Firebase aFirebase;

    private String strName;

    private Intent myIntent;

    private int check = 0;

    private String contentMail = "";

    private String contentPass = "";

    @Click(R.id.activity_login_btn_login)
    public void btnLogin() {
        doLogin();
    }

    @Click(R.id.activity_login_btn_register)
    public void btnRegister() {
        doRegister();
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        giveData();
//        Firebase.setAndroidContext(this);
//        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
//        aInit();
//    }
    @AfterViews
    public void afterViews() {
        giveData();
        Firebase.setAndroidContext(this);
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        aInit();
    }

    private void displayKeyboard() {
        if (etMail.requestFocus()) {
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etMail, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void aInit() {
        etMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayKeyboard();
            }
        });
        Log.e("Gmail", contentMail);
        Log.e("Pass", contentPass);
        if (contentMail.compareTo("") != 0 && contentPass.compareTo("") != 0) {
            etMail.setText(contentMail);
            etPass.setText(contentPass);
        }
//        btnLogin.setOnClickListener(new MyEvent());
//        btnRegister.setOnClickListener(new MyEvent());
    }

//    private class MyEvent implements View.OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.activity_login_btn_login:
//                    doLogin();
//                    break;
//                case R.id.activity_login_btn_register:
//                    doRegister();
//                    break;
//            }
//        }
//    }

    public void doLogin() {
        aFirebase.authWithPassword(etMail.getText().toString(), etPass.getText().toString(),
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        //String message = authData.getUid();
                        getNameUser();
                        check = 1;
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        final AlertDialog.Builder ALERT =
                                new AlertDialog.Builder(LoginActivity.this);
                        ALERT.setIcon(R.drawable.ic_warning);
                        if (firebaseError.getMessage().toString()
                                .compareTo("The specified password is incorrect.") == 0) {
                            ALERT.setMessage(firebaseError.getMessage().toString());
                            ALERT.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPass.setText("");
                                }
                            }).show();
                        } else if ((firebaseError.getMessage().toString()
                                .compareTo("Due to another authentication attempt," +
                                        "this authentication attempt was aborted " +
                                        "before it could complete.") == 0) ||
                                (firebaseError.getMessage().toString()
                                        .compareTo("There was an exception while connecting to " +
                                                "the authentication server:" +
                                                " No peer certificate")) == 0) {
                            ALERT.setMessage(firebaseError.getMessage().toString()
                                    + "You need check about network");
                            ALERT.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                        } else if (firebaseError.getMessage().toString()
                                .compareTo("The active or pending " +
                                        "auth credentials were superseded" +
                                        " by another call to auth") == 0) {
                            ALERT.setMessage(firebaseError.getMessage().toString() +
                                    "Wait for some minutes. Your network is loading");
                            ALERT.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                        } else { //The specified email address is invalid
                            ALERT.setMessage(firebaseError.getMessage().toString() +
                                    " You need create an account?");
                            ALERT.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myIntent =
                                            new Intent(LoginActivity.this, RegisterActivity.class);
                                    startActivity(myIntent);
                                }
                            });
                            ALERT.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                        }
                    }
                });

    }

    public void doRegister() {
        myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
        aExit();
    }

    public void aExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Accept");
        builder.setMessage("Do you want to Exit?");
        builder.setIcon(R.drawable.ic_warning);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                finish();
//                System.aExit(0);
                moveTaskToBack(true);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public void getNameUser() {
        final String STRMAIL = etMail.getText().toString().replace(".", "&");
        aFirebase.child("User").child(STRMAIL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                strName = dataSnapshot.getValue(String.class);
                strName = strName.toUpperCase();
                myIntent = new Intent(LoginActivity.this, Menu_Choose.class);
                myIntent.putExtra("NameUser", strName);
                myIntent.putExtra("MailUser", STRMAIL);
                startActivity(myIntent);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void giveData() {
        if (getIntent().getExtras() != null) {
            contentMail = getIntent().getStringExtra("MailUser");
            contentPass = getIntent().getStringExtra("Password");
        } else {
            contentMail = "";
            contentPass = "";
        }
    }

}
