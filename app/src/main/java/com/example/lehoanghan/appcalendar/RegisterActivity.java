package com.example.lehoanghan.appcalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lehoanghan.base.BaseActivity;
import com.example.lehoanghan.utils.AppUtil;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by lehoanghan on 4/15/2016.
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity implements Validator.ValidationListener {
    private static Animation sShakeAnimation;

    @ViewById(R.id.activity_regiter_ll_root)
    LinearLayout llRegister;

    @NotEmpty
    @ViewById(R.id.activity_register_et_username)
    EditText etName;

    @NotEmpty
    @Email(message = "Pleae enter a valid your Email ID ")
    @ViewById(R.id.activity_register_et_email)
    EditText etMail;

    @NotEmpty
    @Password(scheme = Password.Scheme.ANY)
    @ViewById(R.id.activity_register_et_password)
    EditText etPassword;

    @ConfirmPassword
    @ViewById(R.id.activity_register_et_confirmpassword)
    EditText etConfPassword;

    @ViewById(R.id.activity_register_rl_register)
    RelativeLayout rlRegister;

    @Checked
    @ViewById(R.id.activity_register_cb_agree_register)
    CheckBox cbRegister;

    @ViewById(R.id.activity_register_rl_login_here)
    RelativeLayout rlLoginHere;

    @ViewById(R.id.activity_register_v_email_underline)
    View vEmailUnderline;
    @ViewById(R.id.activity_register_v_username_underline)
    View vUserNameUnderline;
    @ViewById(R.id.activity_register_v_password_underline)
    View vPasswordUnderline;
    @ViewById(R.id.activity_register_v_confirmpassword_underline)
    View vConfirmPasswordUnderline;

    private Firebase aFirebase;
    private Validator validator;
    private AlertDialog.Builder alertDialog;
    private boolean isValidateSuccess;

    @AfterViews
    public void afterView() {
        /*Set Status bar color*/
        AppUtil.changeStatusBarColor(this);
        //Using firebase
        Firebase.setAndroidContext(this);
        aFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        //Set validator
        validator = new Validator(this);
        validator.setValidationListener(this);
        //load animation
        sShakeAnimation =
                AnimationUtils.loadAnimation(getApplication(), R.anim.animation_shake);
    }

    @Click(R.id.activity_register_rl_register)
    void rlRegisterClick() {
        validator.validate();
        if (isValidateSuccess) {
            showProgressLoading();
            saveDatabase();
        }
    }

    @Click(R.id.activity_register_rl_login_here)
    void rlLoginHereClick() {
        onBackPressed();
        LoginActivity_.intent(this).start();
        overridePendingTransition(R.anim.animation_left_enter,R.anim.animation_right_exit);
    }

    //Hide soft keyboard when you touch outside
    @Touch(R.id.activity_regiter_ll_root)
    void llRootTouch() {
        AppUtil.hideSoftKeyboard(this);
    }

    @TextChange(R.id.activity_register_et_username)
    void etUserNameChange() {
        validator.validate();
    }

    @TextChange(R.id.activity_register_et_email)
    void etGmailChange() {
        validator.validate();
    }

    @TextChange(R.id.activity_register_et_password)
    void etPasswordChange() {
        validator.validate();
    }

    @TextChange(R.id.activity_register_et_confirmpassword)
    void etConfirmPasswordChange() {
        validator.validate();
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
        vUserNameUnderline.setBackgroundColor(Color.CYAN);
        vConfirmPasswordUnderline.setBackgroundColor(Color.CYAN);
        vEmailUnderline.setBackgroundColor(Color.CYAN);
        vPasswordUnderline.setBackgroundColor(Color.CYAN);

        rlRegister.setEnabled(true);
        isValidateSuccess = true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        vUserNameUnderline.setBackgroundColor(Color.WHITE);
        vEmailUnderline.setBackgroundColor(Color.WHITE);
        vPasswordUnderline.setBackgroundColor(Color.WHITE);
        vConfirmPasswordUnderline.setBackgroundColor(Color.WHITE);

        for (ValidationError error : errors) {
            View contentView = error.getView();
            rlRegister.setEnabled(false);

            if (contentView instanceof EditText) {
                if (contentView.equals(etName)) {
                    if (!((EditText) contentView).getText().toString().equals("")) {
                        vUserNameUnderline.setBackgroundColor(Color.RED);
                    }
                }

                if (contentView.equals(etMail)) {
                    if (!((EditText) contentView).getText().toString().equals("")) {
                        vEmailUnderline.setBackgroundColor(Color.RED);
                    }
                }

                if (contentView.equals(etPassword)) {
                    if (!((EditText) contentView).getText().toString().equals("")) {
                        vPasswordUnderline.setBackgroundColor(Color.RED);
                    }
                }

                if (contentView.equals(etConfPassword)) {
                    if (!((EditText) contentView).getText().toString().equals("")) {
                        vConfirmPasswordUnderline.setBackgroundColor(Color.RED);
                    }
                }
            }
        }
        isValidateSuccess = false;
    }

    public byte[] convertImage() {
        Bitmap aBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.smile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        aBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public String covertArraytoString() {
        return Base64.encodeToString(convertImage(), Base64.DEFAULT);
    }

    private void saveDatabase() {
        aFirebase.createUser(etMail.getText().toString(), etPassword.getText().toString(),
                new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> stringObjectMap) {
                        hideProgressLoading();
                        //aUser.setjId(stringObjectMap.get("uid").toString());
                        String mail = etMail.getText().toString().replace(".", "&");
                        aFirebase.child("User").child(mail).setValue(etName.getText().toString());
                        aFirebase.child("Avata").child(mail).setValue(covertArraytoString());
                        alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                        alertDialog.setMessage("Congratulate, do you want to login?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent =
                                        new Intent(RegisterActivity.this, LoginActivity_.class);
                                intent.putExtra("MailUser", etMail.getText().toString());
                                intent.putExtra("Password", etPassword.getText().toString());
                                startActivity(intent);
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(),
                                firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                        etPassword.setText("");
                        etConfPassword.setText("");
                    }
                });
    }
}
