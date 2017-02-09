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

public class LoginActivity extends Activity {

    private EditText etMail, etPass;
    private Button btnLogin, btnRegister;
    private Firebase firebase;
    private String strName;
    private Intent intent;
    private int check = 0;
    private String contentMail = "", contentPass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        giveData();
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://appcalendar.firebaseio.com/");
        Init();
    }

    private void displayKeyboard() {
        if (etMail.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etMail, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void Init() {
        etMail = (EditText) findViewById(R.id.activity_login_et_gmail);
        etMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayKeyboard();
            }
        });
        etPass = (EditText) findViewById(R.id.activity_login_et_password);
        btnLogin = (Button) findViewById(R.id.activity_login_btn_login);
        btnRegister = (Button) findViewById(R.id.activity_login_btn_register);
        Log.e("Gmail", contentMail);
        Log.e("Pass", contentPass);
        if (contentMail.compareTo("") != 0 && contentPass.compareTo("") != 0) {
            etMail.setText(contentMail);
            etPass.setText(contentPass);
        }
        btnLogin.setOnClickListener(new MyEvent());
        btnRegister.setOnClickListener(new MyEvent());

    }


    private class MyEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_login_btn_login:
                    doLogin();
                    break;
                case R.id.activity_login_btn_register:
                    doRegister();
                    break;
            }
        }
    }

    public void doLogin() {
        firebase.authWithPassword(etMail.getText().toString(), etPass.getText().toString(), new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                //String message = authData.getUid();
                getNameUser();
                check = 1;
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setIcon(R.drawable.warning);
                if (firebaseError.getMessage().toString().compareTo("The specified password is incorrect.") == 0) {
                    alert.setMessage(firebaseError.getMessage().toString());
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etPass.setText("");
                        }
                    }).show();
                } else if ((firebaseError.getMessage().toString().compareTo("Due to another authentication attempt, this authentication attempt was aborted before it could complete.") == 0) ||
                        (firebaseError.getMessage().toString().compareTo("There was an exception while connecting to the authentication server: No peer certificate")) == 0) {
                    alert.setMessage(firebaseError.getMessage().toString() + "You need check about network");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                } else if (firebaseError.getMessage().toString().compareTo("The active or pending auth credentials were superseded by another call to auth") == 0) {
                    alert.setMessage(firebaseError.getMessage().toString() + "Wait for some minutes. Your network is loading");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                } else //The specified email address is invalid
                {
                    alert.setMessage(firebaseError.getMessage().toString() + " You need create an account?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            startActivity(intent);
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }
            }
        });

    }

    public void doRegister() {
        intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Accept");
        builder.setMessage("Do you want to exit?");
        builder.setIcon(R.drawable.warning);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                finish();
//                System.exit(0);
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
        final String str = etMail.getText().toString().replace(".", "&");
        firebase.child("User").child(str).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                strName = dataSnapshot.getValue(String.class);
                strName = strName.toUpperCase();
                intent = new Intent(LoginActivity.this, Menu_Choose.class);
                intent.putExtra("NameUser", strName);
                intent.putExtra("MailUser", str);
                startActivity(intent);
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
