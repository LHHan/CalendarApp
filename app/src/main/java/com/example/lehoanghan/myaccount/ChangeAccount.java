package com.example.lehoanghan.myaccount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.choosemenu.Menu_Choose;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by lehoanghan on 5/15/2016.
 */
public class ChangeAccount extends AppCompatActivity {

    private ImageView ImgAvata;
    private EditText EdtName,EdtPassChange,EdtComPassChange,EdtOldPass;
    private TextView TxtMail;
    private Button BtnChangeAvata, BtnSave, BtnClear;
    private String NameUser, MailUser;
    private Firebase firebase;
    private Bitmap bitmap, bitmapFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_account);
        Firebase.setAndroidContext(this);
        firebase =new Firebase("https://appcalendar.firebaseio.com/");
        GivedataUser();
        Init();
        SetImage();
       BtnChangeAvata.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               SelectImage();
           }
       });

        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        BtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdtName.setText("");
                EdtOldPass.setText("");
                EdtPassChange.setText("");
                EdtComPassChange.setText("");
            }
        });
    }

    public void Init(){
        ImgAvata=(ImageView) findViewById(R.id.imgAvataChange);
        EdtName=(EditText) findViewById(R.id.edtNameChange);
        EdtName.setText(NameUser);
        TxtMail=(TextView) findViewById(R.id.txtMailChange);
        TxtMail.setText(MailUser.toString().replace("&","."));
        EdtPassChange=(EditText) findViewById(R.id.edtPassChange);
        EdtComPassChange=(EditText) findViewById(R.id.edtComPassChange);
        EdtOldPass=(EditText) findViewById(R.id.edtOldPass);
        BtnChangeAvata=(Button) findViewById(R.id.btnChangePic);
        BtnSave=(Button) findViewById(R.id.btnSave);
        BtnClear=(Button) findViewById(R.id.btnClear);
    }

    public void SetImage(){
        firebase.child("Avata").child(MailUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                byte[] image = Base64.decode(dataSnapshot.getValue().toString(), Base64.DEFAULT);
                bitmapFirebase = BitmapFactory.decodeByteArray(image, 0, image.length);
                ImgAvata.setImageBitmap(bitmapFirebase);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public void SelectImage(){
        final CharSequence[] options={"Take photo","Choose from Gallery","Cancel"};

        final AlertDialog.Builder buider=new AlertDialog.Builder(ChangeAccount.this);
        buider.setTitle("Add Photo");
        buider.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Take photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[which].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                } else if (options[which].equals("Cancel"))
                    dialog.dismiss();

            }
        });
        buider.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),bitmapOptions);
                    ImgAvata.setImageBitmap(bitmap);
                    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                bitmap = (BitmapFactory.decodeFile(picturePath));
                ImgAvata.setImageBitmap(bitmap);
            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_back,menu);
        setTitle("Change Account");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.backfromChangeAccount:
                Intent intent=new Intent(this, Menu_Choose.class);
                intent.putExtra("NameUser",NameUser);
                intent.putExtra("MailUser",MailUser);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public String CovertBitmaptoString(){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        if(bitmap!=null)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        else
        bitmapFirebase.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        byte[]array=outputStream.toByteArray();
        String PicFirebase= Base64.encodeToString(array,Base64.DEFAULT);
        return PicFirebase;
    }


    public void CheckData(){
        if(EdtName.getText().toString().compareTo("")==0||EdtOldPass.getText().toString().compareTo("")==0||
                EdtPassChange.getText().toString().compareTo("")==0||EdtComPassChange.getText().toString().compareTo("")==0)
            Toast.makeText(getApplicationContext(),"Entry Data, You need to fill out full data",Toast.LENGTH_LONG);
        else
        {
            if(EdtPassChange.getText().toString().compareTo(EdtComPassChange.getText().toString())!=0) {
                Toast.makeText(getApplicationContext(), "Error Comfirm Pass", Toast.LENGTH_LONG);
                EdtPassChange.setText("");
                EdtComPassChange.setText("");
            }
            else
            {
                firebase.changePassword(MailUser.replace("&","."), EdtOldPass.getText().toString(), EdtPassChange.getText().toString(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), firebaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

                firebase.child("User").child(MailUser).setValue(EdtName.getText().toString());
                firebase.child("Avata").child(MailUser).setValue(CovertBitmaptoString());
            }
        }
    }

    public void GivedataUser(){
        MailUser=getIntent().getStringExtra("MailUser");
        NameUser=getIntent().getStringExtra("NameUser");
    }
}
