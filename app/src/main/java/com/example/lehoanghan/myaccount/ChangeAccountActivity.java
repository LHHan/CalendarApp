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
public class ChangeAccountActivity extends AppCompatActivity {

    private ImageView ivAvatar;
    private EditText etName, etPassChange, etComPassChange, etOldPass;
    private TextView tvMail;
    private Button btnChangeAvatar, btnSave, btnClear;
    private String nameUser, mailUser;
    private Firebase firebase;
    private Bitmap bitmap, bitmapFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account);
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://appcalendar.firebaseio.com/");
        giveDataUser();
        Init();
        setImage();
        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText("");
                etOldPass.setText("");
                etPassChange.setText("");
                etComPassChange.setText("");
            }
        });
    }

    public void Init() {
        ivAvatar = (ImageView) findViewById(R.id.activity_change_account_iv_avatar);
        etName = (EditText) findViewById(R.id.activity_change_account_et_name);
        etName.setText(nameUser);
        tvMail = (TextView) findViewById(R.id.activity_change_account_tv_mail);
        tvMail.setText(mailUser.toString().replace("&", "."));
        etPassChange = (EditText) findViewById(R.id.activity_change_account_et_password);
        etComPassChange = (EditText) findViewById(R.id.activity_change_account_et_confirm_password);
        etOldPass = (EditText) findViewById(R.id.activity_change_account_et_old_password);
        btnChangeAvatar = (Button) findViewById(R.id.activity_change_account_btn_change_avatar);
        btnSave = (Button) findViewById(R.id.activity_change_account_btn_save);
        btnClear = (Button) findViewById(R.id.activity_change_account_btn_clear);
    }

    public void setImage() {
        firebase.child("Avatar").child(mailUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                byte[] image = Base64.decode(dataSnapshot.getValue().toString(), Base64.DEFAULT);
                bitmapFirebase = BitmapFactory.decodeByteArray(image, 0, image.length);
                ivAvatar.setImageBitmap(bitmapFirebase);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public void selectImage() {
        final CharSequence[] options = {"Take photo", "Choose from Gallery", "Cancel"};

        final AlertDialog.Builder buider = new AlertDialog.Builder(ChangeAccountActivity.this);
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
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    ivAvatar.setImageBitmap(bitmap);
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
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                bitmap = (BitmapFactory.decodeFile(picturePath));
                ivAvatar.setImageBitmap(bitmap);
            }

        }

    }


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
                Intent intent = new Intent(this, Menu_Choose.class);
                intent.putExtra("NameUser", nameUser);
                intent.putExtra("MailUser", mailUser);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public String covertBitmaptoString() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (bitmap != null)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        else
            bitmapFirebase.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] array = outputStream.toByteArray();
        String PicFirebase = Base64.encodeToString(array, Base64.DEFAULT);
        return PicFirebase;
    }


    public void checkData() {
        if (etName.getText().toString().compareTo("") == 0 || etOldPass.getText().toString().compareTo("") == 0 ||
                etPassChange.getText().toString().compareTo("") == 0 || etComPassChange.getText().toString().compareTo("") == 0)
            Toast.makeText(getApplicationContext(), "Entry Data, You need to fill out full data", Toast.LENGTH_LONG);
        else {
            if (etPassChange.getText().toString().compareTo(etComPassChange.getText().toString()) != 0) {
                Toast.makeText(getApplicationContext(), "Error Comfirm Pass", Toast.LENGTH_LONG);
                etPassChange.setText("");
                etComPassChange.setText("");
            } else {
                firebase.changePassword(mailUser.replace("&", "."), etOldPass.getText().toString(), etPassChange.getText().toString(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), firebaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

                firebase.child("User").child(mailUser).setValue(etName.getText().toString());
                firebase.child("Avata").child(mailUser).setValue(covertBitmaptoString());
            }
        }
    }

    public void giveDataUser() {
        mailUser = getIntent().getStringExtra("MailUser");
        nameUser = getIntent().getStringExtra("NameUser");
    }
}
