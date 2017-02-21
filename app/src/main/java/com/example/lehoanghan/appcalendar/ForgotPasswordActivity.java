package com.example.lehoanghan.appcalendar;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.utilities.Utilities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.regex.Pattern;

@EActivity(R.layout.activity_forgot_password)
public class ForgotPasswordActivity extends AppCompatActivity {
    private static Animation sShakeAnimation;

    @ViewById(R.id.activity_forgot_password_et_email)
    EditText etEmail;

    @ViewById(R.id.activity_forgot_password_tv_back)
    TextView tvBack;

    @ViewById(R.id.activity_forgot_password_tv_submit)
    TextView tvSubmit;

    @ViewById(R.id.activity_forgot_password_ll_forgot)
    LinearLayout llForgot;

    @Click(R.id.activity_forgot_password_tv_submit)
    void setTvSubmit() {
        String getEmailId = etEmail.getText().toString();
        try {
            if (getEmailId.equals("") || getEmailId.length() == 0) {
                Toast.makeText(getApplicationContext(),
                        "Please enter your Email ID",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Get Forgot Password.",
                        Toast.LENGTH_SHORT).show();
            }
            llForgot.startAnimation(sShakeAnimation);
        } catch (Exception e)
        {

        }
    }

    @Click(R.id.activity_forgot_password_tv_back)
    void setTvBack() {
        Intent myintent = new Intent(ForgotPasswordActivity.this, LoginActivity_.class);
        startActivity(myintent);
    }

    @AfterViews
    public void afterView() {
        initView();
    }

    private void initView() {

    }
}
