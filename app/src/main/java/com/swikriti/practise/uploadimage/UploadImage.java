package com.swikriti.practise.uploadimage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    Uri selectedImage;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonChoose=(Button) findViewById(R.id.buttonChoose);
        buttonUpload=(Button)findViewById(R.id.buttonUpload);
        imageView=(ImageView) findViewById(R.id.imageView);
        editText=(EditText)findViewById(R.id.editTextName);
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            selectImage(UploadImage.this);
        }
        if (v == buttonUpload) {
            //uploadImage();
            Toast.makeText(this, "to-be-handeled", Toast.LENGTH_LONG).show();
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
                            Bitmap bitmap = null;
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
}
