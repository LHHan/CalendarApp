package com.example.lehoanghan.appcalendar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lehoanghan.base.BaseActivity;
import com.example.lehoanghan.utils.AppUtil;
import com.firebase.client.Firebase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_forgot_password)
public class ForgotPasswordActivity extends BaseActivity implements Validator.ValidationListener {
    @NotEmpty
    @Email
    @ViewById(R.id.activity_forgot_password_et_email)
    EditText etEmail;

    @ViewById(R.id.activity_forgot_password_rl_back)
    RelativeLayout rlBack;

    @ViewById(R.id.activity_forgot_password_rl_submit)
    RelativeLayout rlSubmit;

    @ViewById(R.id.activity_forgot_password_ll_root)
    LinearLayout llRoot;

    @ViewById(R.id.activity_forgot_password_v_mail_underline)
    View vMailUnderline;

    private Validator validator;
    private boolean isValidateSuccess;

    @AfterViews
    void init() {
         /*Set Status bar color*/
        AppUtil.changeStatusBarColor(this);
        //using google firebase
        Firebase.setAndroidContext(this);
        Firebase firebase = new Firebase("https://appcalendar.firebaseio.com/");
        //using validator saripaar
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Click(R.id.activity_forgot_password_rl_submit)
    void rlSubmitClick() {
        showProgressLoading();
        if (isValidateSuccess) {
            Toast.makeText(this, "Yay, we got it right", Toast.LENGTH_SHORT).show();
        }
        hideProgressLoading();
    }

    @Click(R.id.activity_forgot_password_rl_back)
    void rlBackClick() {
        LoginActivity_.intent(this).start();
        overridePendingTransition(R.anim.animation_right_enter, R.anim.animation_left_exit);
    }

    @TextChange(R.id.activity_forgot_password_et_email)
    void etEmailChange() {
        validator.validate();
    }

    //Hide soft keyboard when you touch outside
    @Touch(R.id.activity_forgot_password_ll_root)
    void llRootTouch() {
        AppUtil.hideSoftKeyboard(this);
    }


    @Override
    public void showProgressLoading() {
        super.showProgressLoading();
    }

    @Override
    public void hideProgressLoading() {
        super.hideProgressLoading();
    }


    @Override
    public void onValidationSucceeded() {
        vMailUnderline.setBackgroundColor(Color.CYAN);
        rlSubmit.setEnabled(true);
        isValidateSuccess = true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        vMailUnderline.setBackgroundColor(Color.WHITE);
        for (ValidationError error : errors) {
            View contentView = error.getView();

            rlSubmit.setEnabled(false);

            if (contentView instanceof EditText) {
                if (contentView.equals(etEmail)) {
                    if (!((EditText) contentView).getText().toString().equals("")) {
                        vMailUnderline.setBackgroundColor(Color.RED);
                    }
                }
            }
        }
        isValidateSuccess = false;
    }
}
