package com.example.lehoanghan.appcalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_forgot_password)
public class ForgotPasswordActivity extends AppCompatActivity implements Validator.ValidationListener {
    private static Animation sShakeAnimation;

    @NotEmpty
    @Email
    @ViewById(R.id.activity_forgot_password_et_email)
    EditText etEmail;

    @ViewById(R.id.activity_forgot_password_tv_back)
    TextView tvBack;

    @ViewById(R.id.activity_forgot_password_tv_submit)
    TextView tvSubmit;

    @ViewById(R.id.activity_forgot_password_ll_forgot)
    LinearLayout llForgot;

    private Firebase firebase;

    private Validator validator;

    @Click(R.id.activity_forgot_password_tv_submit)
    void setTvSubmit() {
        validator.validate();
        String getEmailId = etEmail.getText().toString();
        try {
            Toast.makeText(getApplicationContext(), "Get Forgot Password.",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
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
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://appcalendar.firebaseio.com/");
        sShakeAnimation =
                AnimationUtils.loadAnimation(getApplication(), R.anim.animation_shake);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "Yay, we got it right", Toast.LENGTH_SHORT).show();
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

