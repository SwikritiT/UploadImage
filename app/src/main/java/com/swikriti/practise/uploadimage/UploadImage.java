package com.swikriti.practise.uploadimage;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by DELL on 4/29/2020.
 */

public class UploadImage extends AppCompatActivity implements View.OnClickListener {
    private Button buttonChoose;
    private Button buttonUpload;
    private ImageView imageView;
    private EditText editText;
    private TextView test, test1;
    Uri selectedImage;
    private static final int STORAGE_PERMISSION_CODE = 123;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestStoragePermission();
        buttonChoose=(Button) findViewById(R.id.buttonChoose);
        buttonUpload=(Button)findViewById(R.id.buttonUpload);
        imageView=(ImageView) findViewById(R.id.imageView);
        editText=(EditText)findViewById(R.id.editTextName);
        test=(TextView) findViewById(R.id.test);
        test1=(TextView) findViewById(R.id.test1);
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            selectImage(UploadImage.this);
        }
        if (v == buttonUpload) {
            uploadImage();

        }


    }
    private  void selectImage(Context context){
        final CharSequence[] options={"Take Photo","Choose from Gallery","Cancel"};
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setTitle("Choose a picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(options[item].equals("Take Photo")){
                    Intent takePicture=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture,0);

                }
                else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }
    public void uploadImage(){
        final String name = "hello";
        //getting the actual path of the image editText.getText().toString().trim();
        final String path = getPath(selectedImage);
        test.setText("hi");
       //test1.setText(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_CANCELED){
            switch (requestCode){
                case 0:
                    if(resultCode==RESULT_OK && data!=null){
                        Bitmap selectedImage1=(Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage1);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Bitmap bitmap;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                                imageView.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            Cursor cursor = getContentResolver().query(selectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                cursor.close();
//                            }
                        }

                    }
                    break;
            }

        }
    }
    //method to get the file path from uri
    public String getPath(Uri uri) {

        Cursor cursor = getContentResolver().query(uri,null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    //ask permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
//            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            Toast.makeText(this, "You won't be able to use this app without granting permission", Toast.LENGTH_LONG).show();
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
       // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
    }
}
