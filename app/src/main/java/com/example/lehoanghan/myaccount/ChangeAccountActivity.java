package com.example.lehoanghan.myaccount;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lehoanghan.appcalendar.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by lehoanghan on 5/15/2016.
 */
@EActivity(R.layout.activity_change_account)

public class ChangeAccountActivity extends AppCompatActivity implements Validator.ValidationListener {
    private static Animation sShakeAnimation;

    @NotEmpty
    @ViewById(R.id.activity_change_account_et_name)
    EditText etName;

    @ViewById(R.id.activity_change_account_tv_mail_value)
    TextView tvMail;

    @NotEmpty
    @ViewById(R.id.activity_change_account_et_old_password)
    EditText etOldPass;

    @Password(min = 6, scheme = Password.Scheme.ANY)
    @ViewById(R.id.activity_change_account_et_password)
    EditText etNewPass;

    @ConfirmPassword
    @ViewById(R.id.activity_change_account_et_confirm_password)
    EditText etConfpass;

    @ViewById(R.id.activity_change_account_btn_save)
    Button btnSave;

    @ViewById(R.id.activity_change_account_btn_clear)
    Button btnClear;

    @ViewById(R.id.activity_change_account_ll_main)
    LinearLayout linearLayout;


    private String nameUser;

    private String mailUser;

    private Firebase firebase;

    private Validator validator;

    @Click(R.id.activity_change_account_btn_save)
    void setBtnSave() {
        validator.validate();
    }

    @Click(R.id.activity_change_account_btn_clear)
    void setBtnClear() {
        etName.setText("");
        etOldPass.setText("");
        etNewPass.setText("");
        etConfpass.setText("");
    }

    @AfterViews
    void afterView() {
        // google firebase
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://appcalendar.firebaseio.com/");

        //give date user
        giveDataUser();

        //load annimation
        sShakeAnimation =
                AnimationUtils.loadAnimation(getApplication(), R.anim.animation_shake);

        //annotation saripaar
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

//    public void selectImage() {
//        final CharSequence[] OPTIONS = {"Take photo", "Choose from Gallery", "Cancel"};
//
//        final AlertDialog.Builder BUILDER = new AlertDialog.Builder(ChangeAccountActivity.this);
//        BUILDER.setTitle("Add Photo");
//        BUILDER.setItems(OPTIONS, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (OPTIONS[which].equals("Take photo")) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(
//                            android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                    startActivityForResult(intent, 1);
//                } else if (OPTIONS[which].equals("Choose from Gallery")) {
//                    Intent intent = new Intent(Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, 2);
//                } else if (OPTIONS[which].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        BUILDER.show();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 1) {
//                File f = new File(Environment.getExternalStorageDirectory().toString());
//                for (File temp : f.listFiles()) {
//                    if (temp.getName().equals("temp.jpg")) {
//                        f = temp;
//                        break;
//                    }
//                }
//                try {
//                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//                    aBitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//                    ivAvatar.setImageBitmap(aBitmap);
//                    String path = android.os.Environment.getExternalStorageDirectory()
//                            + File.separator + "Phoenix" + File.separator + "default";
//                    f.delete();
//                    OutputStream outFile = null;
//                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
//                    try {
//                        outFile = new FileOutputStream(file);
//                        aBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
//                        outFile.flush();
//                        outFile.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else if (requestCode == 2) {
//                Uri selectedImage = data.getData();
//                String[] filePath = {MediaStore.Images.Media.DATA};
//                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                String picturePath = c.getString(columnIndex);
//                c.close();
//                aBitmap = (BitmapFactory.decodeFile(picturePath));
//                ivAvatar.setImageBitmap(aBitmap);
//            }
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_back, menu);
        setTitle("Change Account");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backfromChangeAccount:
//                Intent intent = new Intent(this, NavigationActivity.class);
//                intent.putExtra("NameUser", nameUser);
//                intent.putExtra("MailUser", mailUser);
//                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
//
//    public String covertBitmaptoString() {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        if (aBitmap != null) {
//            aBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//        } else {
//            bitmapFirebase.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//        }
//        byte[] array = outputStream.toByteArray();
//        String picFirebase = Base64.encodeToString(array, Base64.DEFAULT);
//        return picFirebase;
//    }

    public void checkFirebase() {
        firebase.changePassword(mailUser.replace("&", "."),
                etOldPass.getText().toString(),
                etNewPass.getText().toString(),
                new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),
                                "Your Information has been changed",
                                Toast.LENGTH_LONG).show();
                        firebase.child("User").child(mailUser).setValue(etName.getText().toString());
                        finish();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(),
                                firebaseError.getMessage().toString(),
                                Toast.LENGTH_LONG).show();
                        linearLayout.setAnimation(sShakeAnimation);
                    }
                });
//                firebase.child("Avata").child(mailUser).setValue(covertBitmaptoString());
    }

    public void giveDataUser() {
        mailUser = getIntent().getStringExtra("MailUser");
        nameUser = getIntent().getStringExtra("NameUser");
        etName.setText(nameUser);
        tvMail.setText(mailUser.toString().replace("&", "."));
    }

    @Override
    public void onValidationSucceeded() {
        checkFirebase();
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
                linearLayout.startAnimation(sShakeAnimation);
            }
        }
    }
}
