package com.example.lehoanghan.appcalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Created by lehoanghan on 4/15/2016.
 */
public class RegisterActivity extends Activity {

    private EditText etName, etPass, etMail, etConfPass;
    private Button btnCreate, btnClear;
    private Firebase firebase;
    private User user;
    private AlertDialog.Builder alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://appcalendar.firebaseio.com/");
        Init();
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInform();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText("");
                etMail.setText("");
                etPass.setText("");
                etConfPass.setText("");
            }
        });

    }

    public void Init() {
        etName = (EditText) findViewById(R.id.activity_register_et_username);
        etMail = (EditText) findViewById(R.id.activity_register_et_gmail);
        etPass = (EditText) findViewById(R.id.activity_register_et_password);
        etConfPass = (EditText) findViewById(R.id.activity_register_et_confirmpassword);
        btnCreate = (Button) findViewById(R.id.activity_register_btn_create);
        btnClear = (Button) findViewById(R.id.activity_register_btn_clear);
    }

    public void CheckPass() {
        if (etConfPass.getText().toString().compareTo(etPass.getText().toString()) != 0) {
            Toast.makeText(getApplicationContext(), "Enter Conpass error, Enter Conpass again, please", Toast.LENGTH_SHORT).show();
            etPass.setText("");
            etConfPass.setText("");
        } else {
            SaveDb();
        }
    }

    public void CheckInform() {
        if ((etName.getText().toString() == "") || (etMail.getText().toString() == "") || (etPass.getText().toString() == "") || (etConfPass.getText().toString() == "")) {
            Toast.makeText(getApplicationContext(), "You need fill out all inform", Toast.LENGTH_SHORT).show();
        } else {
            CheckPass();
        }
    }

    public void SaveUser() {
        user = new User();
        user.setjName(etName.getText().toString());
        user.setjMail(etMail.getText().toString());
        user.setjPass(etPass.getText().toString());
    }

    public byte[] ConvertImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.smile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] array = outputStream.toByteArray();
        return array;
    }

    public String CovertArraytoString() {
        String PicFirebase = Base64.encodeToString(ConvertImage(), Base64.DEFAULT);
        return PicFirebase;
    }


    public void SaveDb() {

        firebase.createUser(etMail.getText().toString(), etPass.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                //user.setjID(stringObjectMap.get("uid").toString());
                String mail = etMail.getText().toString().replace(".", "&");
                firebase.child("User").child(mail).setValue(etName.getText().toString());
                firebase.child("Avata").child(mail).setValue(CovertArraytoString());
                alert = new AlertDialog.Builder(RegisterActivity.this);
                alert.setMessage("Congratulate, do you want to login?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("MailUser", etMail.getText().toString());
                        Log.e("EditGmail", etMail.getText().toString());
                        intent.putExtra("Password", etPass.getText().toString());
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), firebaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
                etPass.setText("");
                etConfPass.setText("");
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
