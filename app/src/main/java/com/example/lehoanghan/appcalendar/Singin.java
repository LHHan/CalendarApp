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
public class Singin extends Activity {

    private EditText EdtName,EdtPass,EdtGmail, EdtConPass;
    private Button BtnCreate, BtnClear;
    private Firebase firebase;
    private User user;
    private AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);
        Firebase.setAndroidContext(this);
        firebase=new Firebase("https://appcalendar.firebaseio.com/");
        Init();
        BtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInform();
            }
        });
        BtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdtName.setText("");
                EdtGmail.setText("");
                EdtPass.setText("");
                EdtConPass.setText("");
            }
        });
        
    }

    public void Init() {
        EdtName=(EditText)findViewById(R.id.edtUser);
        EdtGmail=(EditText)findViewById(R.id.edtGmail);
        EdtPass=(EditText)findViewById(R.id.edtPass);
        EdtConPass=(EditText)findViewById(R.id.edtCPass);
        BtnCreate=(Button)findViewById(R.id.btnCreate);
        BtnClear=(Button)findViewById(R.id.btnClear);
    }

    public void CheckPass() {
        if(EdtConPass.getText().toString().compareTo(EdtPass.getText().toString())!=0)
        {
            Toast.makeText(getApplicationContext(),"Enter Conpass error, Enter Conpass again, please",Toast.LENGTH_SHORT).show();
            EdtPass.setText("");
            EdtConPass.setText("");        }
        else
        {
            SaveDb();
        }
    }

    public void CheckInform() {
        if((EdtName.getText().toString()=="")||(EdtGmail.getText().toString()=="")||(EdtPass.getText().toString()=="")||(EdtConPass.getText().toString()==""))
        {
            Toast.makeText(getApplicationContext(),"You need fill out all inform",Toast.LENGTH_SHORT).show();
        }
        else
        {
            CheckPass();
        }
    }

    public void SaveUser() {
        user=new User();
        user.setjName(EdtName.getText().toString());
        user.setjMail(EdtGmail.getText().toString());
        user.setjPass(EdtPass.getText().toString());
    }

    public byte[] ConvertImage(){
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.smile);
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[]array=outputStream.toByteArray();
        return array;
    }

    public String CovertArraytoString(){
        String PicFirebase= Base64.encodeToString(ConvertImage(),Base64.DEFAULT);
        return PicFirebase;
    }


    public void SaveDb(){

        firebase.createUser(EdtGmail.getText().toString(), EdtPass.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                //user.setjID(stringObjectMap.get("uid").toString());
                String mail=EdtGmail.getText().toString().replace(".","&");
                firebase.child("User").child(mail).setValue(EdtName.getText().toString());
                firebase.child("Avata").child(mail).setValue(CovertArraytoString());
                alert = new AlertDialog.Builder(Singin.this);
                alert.setMessage("Congratulate, do you want to login?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Singin.this, Login.class);
                        intent.putExtra("MailUser",EdtGmail.getText().toString());
                        Log.e("EditGmail",EdtGmail.getText().toString());
                        intent.putExtra("Password",EdtPass.getText().toString());
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
                EdtPass.setText("");
                EdtConPass.setText("");
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_about,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.back:
                Intent intent=new Intent(this,Login.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
