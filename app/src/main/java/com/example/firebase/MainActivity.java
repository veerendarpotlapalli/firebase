package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText name,phone,address,trans,price ;
    Button add,register;
    ImageView img;
    Uri filepath;
    Bitmap bitmap; // used for allow us to manipulate pixels of image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView) findViewById(R.id.imageView);
        add = (Button) findViewById(R.id.btnadd);
        register = (Button) findViewById(R.id.btnregister);
        name = (EditText) findViewById(R.id.txtname);
        phone = (EditText) findViewById(R.id.txtphone);
        address = (EditText) findViewById(R.id.txtaddress);
        trans = (EditText) findViewById(R.id.txttrans);
        price = (EditText) findViewById(R.id.txtprice);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  Dexter.withActivity(MainActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Select Image File"),1);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                                permissionToken.continuePermissionRequest();

                            }
                        }).check();

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadtofirebase();
            }
        });
    }

    private void uploadtofirebase()  // here we are having many items to insert in firebase,so to insert them all at once we have to creat a class to hold the all items into an single object to insert data easily
    {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();

        name = (EditText) findViewById(R.id.txtname);
        phone = (EditText) findViewById(R.id.txtphone);
        address = (EditText) findViewById(R.id.txtaddress);
        trans = (EditText) findViewById(R.id.txttrans);
        price = (EditText) findViewById(R.id.txtprice);

        FirebaseStorage storage = FirebaseStorage.getInstance();   // for image purpose
        StorageReference uploader = storage.getReference("image1"+new Random().nextInt(1000000));
        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @SuppressLint("ResourceType")
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                DatabaseReference root = db.getReference("users");

                                dataholder obj = new dataholder(name.getText().toString(),phone.getText().toString(),address.getText().toString(),price.getText().toString(),trans.getText().toString(),uri.toString());
                                root.child(phone.getText().toString()).setValue(obj);

                                name.setText("");
                                phone.setText("");
                                trans.setText("");
                                address.setText("");
                                price.setText("");
                                img.setImageResource(R.id.imageView);
                                Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot)
                    {
                        dialog.setMessage("Uploaded");
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);  // here data in filepath are converted into inputstream and then making it accessable to bitmap
                bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);

            }catch (Exception e){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}