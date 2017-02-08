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

public class Login extends Activity {

    private EditText EdtGmail,EdtPass;
    private Button BtnLogin, BtnSign;
    private Firebase firebase;
    private String strName;
    private Intent intent;
    private int Check=0;
    private String gGmail="",gPass="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GiveData();
        Firebase.setAndroidContext(this);
        firebase=new Firebase("https://appcalendar.firebaseio.com/");
        Init();
    }

    private void displayKeyboard () {
        if (EdtGmail.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(EdtGmail, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void Init() {
        EdtGmail=(EditText)findViewById(R.id.edtGmail);
        EdtGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayKeyboard();
            }
        });
        EdtPass = (EditText) findViewById(R.id.edtPass);
        BtnLogin=(Button)findViewById(R.id.btnLogin);
        BtnSign=(Button)findViewById(R.id.btnSign);
        Log.e("Gmail", gGmail);
        Log.e("Pass",gPass);
        if(gGmail.compareTo("")!=0&&gPass.compareTo("")!=0) {
            EdtGmail.setText(gGmail);
            EdtPass.setText(gPass);
        }
        BtnLogin.setOnClickListener(new MyEvent());
        BtnSign.setOnClickListener(new MyEvent());

    }


    private class MyEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnLogin:
                    DoLogin();
                    break;
                case R.id.btnSign:
                    DoSignin();
                    break;
            }
        }
    }
        public void DoLogin() {
            firebase.authWithPassword(EdtGmail.getText().toString(), EdtPass.getText().toString(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    //String message = authData.getUid();
                    getNameUser();
                    Check = 1;
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                        final AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
                        alert.setIcon(R.drawable.warning);
                    if(firebaseError.getMessage().toString().compareTo("The specified password is incorrect.")==0) {
                        alert.setMessage(firebaseError.getMessage().toString());
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EdtPass.setText("");
                            }
                        }).show();
                    }
                    else
                        if((firebaseError.getMessage().toString().compareTo("Due to another authentication attempt, this authentication attempt was aborted before it could complete.")==0)||
                                (firebaseError.getMessage().toString().compareTo("There was an exception while connecting to the authentication server: No peer certificate"))==0 ){
                            alert.setMessage(firebaseError.getMessage().toString()+"You need check about network");
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                        }
                        else
                        if(firebaseError.getMessage().toString().compareTo("The active or pending auth credentials were superseded by another call to auth")==0){
                            alert.setMessage(firebaseError.getMessage().toString()+"Wait for some minutes. Your network is loading");
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                        }
                        else //The specified email address is invalid
                        {
                            alert.setMessage(firebaseError.getMessage().toString() + " You need create an account?");
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    intent = new Intent(Login.this, Singin.class);
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

        public void DoSignin() {
            intent=new Intent(Login.this,Singin.class);
            startActivity(intent);
        }

    @Override
    public void onBackPressed() {
        Thoat();
    }

    public void Thoat() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Accept");
        builder.setMessage("You want to exit?");
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
        final String str=EdtGmail.getText().toString().replace(".","&");
        firebase.child("User").child(str).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                strName = dataSnapshot.getValue(String.class);
                strName = strName.toUpperCase();
                intent = new Intent(Login.this, Menu_Choose.class);
                intent.putExtra("NameUser", strName);
                intent.putExtra("MailUser", str);
                startActivity(intent);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void GiveData(){
        if(getIntent().getExtras()!=null) {
            gGmail = getIntent().getStringExtra("MailUser");
            gPass = getIntent().getStringExtra("Password");
        }
        else{
            gGmail="";
            gPass="";
        }
    }

}
