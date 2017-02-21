package com.example.lehoanghan.appcalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lehoanghan.choosemenu.Menu_Choose;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {
    private static Animation sShakeAnimation;

    @ViewById(R.id.activity_login_rl_main)
    RelativeLayout rlLogin;

    @ViewById(R.id.activity_login_ll_main)
    LinearLayout llLoin;

    @ViewById(R.id.activity_login_et_gmail)
    EditText etMail;

    @ViewById(R.id.activity_login_et_password)
    EditText etPass;

    @ViewById(R.id.activity_login_tv_register)
    TextView tvRegister;

    @ViewById(R.id.activity_login_btn_login)
    Button btnLogin;

    @ViewById(R.id.activity_login_cb_show_hide_password)
    CheckBox cbShowPassword;

    private Firebase aFirebase;

    private String strName;

    private Intent myIntent;

    private int check = 0;

    private String contentMail = "";

    private String contentPass = "";

    @Click(R.id.activity_login_btn_login)
    void setBtnLogin() {
        doLogin();
    }

    @Click(R.id.activity_login_tv_register)
    void setTvRegister() {
        myIntent = new Intent(LoginActivity.this, RegisterActivity_.class);
        startActivity(myIntent);
    }

//    @Click(R.id.activity_login_tv_forgot_password)
//    void setTvForgotPassword() {
//
//    }

    @CheckedChange(R.id.activity_login_cb_show_hide_password)
    void setCbShowPassword(CompoundButton button, boolean isChecked) {
        if (isChecked) {
            cbShowPassword.setText(R.string.hide_password); // Change checkbox text
            etPass.setInputType(InputType.TYPE_CLASS_TEXT);
            etPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//showpass
        } else {
            cbShowPassword.setText(R.string.show_password); // change checkbox text
            etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());//hide pass
        }
    }

    @AfterViews
    public void afterViews() {
        giveData();
        Firebase.setAndroidContext(this);
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        initView();

    }

    private void displayKeyboard() {
        if (etMail.requestFocus()) {
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etMail, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void initView() {
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
        //Load shake animation
        sShakeAnimation =
                AnimationUtils.loadAnimation(getApplication(), R.anim.animation_shake);
    }

    public void doLogin() {
        aFirebase.authWithPassword(etMail.getText().toString(), etPass.getText().toString(),
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        getNameUser();
                        check = 1;
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        final AlertDialog.Builder ALERT =
                                new AlertDialog.Builder(LoginActivity.this);
                        ALERT.setIcon(R.drawable.ic_warning);
                        if (etMail.equals("") || etPass.equals("")) {
                            llLoin.startAnimation(sShakeAnimation);
                            Toast.makeText(getApplicationContext(),
                                    "Enter your email id and password, please",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (firebaseError.getMessage().toString()
                                .compareTo("The specified password is incorrect.") == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "The specified password is incorrect.",
                                    Toast.LENGTH_SHORT).show();
                            etPass.setText("");
                            llLoin.startAnimation(sShakeAnimation);
                        } else if ((firebaseError.getMessage().toString()
                                .compareTo("Due to another authentication attempt," +
                                        "this authentication attempt was aborted " +
                                        "before it could complete.") == 0) ||
                                (firebaseError.getMessage().toString()
                                        .compareTo("There was an exception while connecting to " +
                                                "the authentication server:" +
                                                " No peer certificate")) == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "You need check about network", Toast.LENGTH_SHORT).show();
                            llLoin.startAnimation(sShakeAnimation);
                        } else if (firebaseError.getMessage().toString()
                                .compareTo("The active or pending " +
                                        "auth credentials were superseded" +
                                        " by another call to auth") == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "Wait for some minutes. Your network is loading",
                                    Toast.LENGTH_SHORT).show();
                            llLoin.startAnimation(sShakeAnimation);
                        } else if (firebaseError.getMessage().toString()
                                .compareTo("The specified email address is invalid") == 0) {
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
                            llLoin.startAnimation(sShakeAnimation);
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        exitView();
    }

    public void exitView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Accept");
        builder.setMessage("Do you want to Exit?");
        builder.setIcon(R.drawable.ic_warning);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                finish();
//                System.exitView(0);
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
