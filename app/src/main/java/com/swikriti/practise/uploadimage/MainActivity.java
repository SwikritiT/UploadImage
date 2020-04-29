//package com.swikriti.practise.uploadimage;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Base64;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//
//import net.gotev.uploadservice.MultipartUploadRequest;
//import net.gotev.uploadservice.UploadNotificationConfig;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//    private Button buttonChoose;
//    private Button buttonUpload;
//    private ImageView imageView;
//    private EditText editText;
//    String encoded_string;
//    private int PICK_IMAGE_REQUEST = 1;
//    private static final int STORAGE_PERMISSION_CODE = 123;
//    private Bitmap bitmap;
//    private Uri filePath;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        //Requesting storage permission
//        requestStoragePermission();
//
//        //Initializing views
//        buttonChoose = (Button) findViewById(R.id.buttonChoose);
//        buttonUpload = (Button) findViewById(R.id.buttonUpload);
//        imageView = (ImageView) findViewById(R.id.imageView);
//        editText = (EditText) findViewById(R.id.editTextName);
//
//        //Setting clicklistener
//        buttonChoose.setOnClickListener(this);
//        buttonUpload.setOnClickListener(this);
//    }
//    /*
//    * This is the method responsible for image upload
//    * We need the full image path and the name for the image in this method
//    * */
//    public void uploadMultipart() {
//        //getting name for the image
//        final String name = editText.getText().toString().trim();
//
//        //getting the actual path of the image
//        final String path = getPath(filePath);
//        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.UPLOAD_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String error=jsonObject.getString("error");
//
//                            Log.e ( "response", "" + response.toString() );
//                            if(error.equals("0")){
//                                Toast.makeText(MainActivity.this, " successfully added", Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(MainActivity.this, "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> map = new HashMap<>();
//                map.put("image", path);
//                map.put("name", name);
//
//
//                return map;
//            }
//        };
//        RequestQueue requestQueue= Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//
////        //Uploading code
////        try {
////
//////            String uploadId = UUID.randomUUID().toString();
//////
//////            //Creating a multi part request
//////            new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_URL)
//////                    .addFileToUpload(path, "image") //Adding file
//////                    .addParameter("name", name) //Adding text parameter to the request
//////                    .setNotificationConfig(new UploadNotificationConfig())
//////                    .setMaxRetries(2)
//////                    .startUpload(); //Starting the upload
////
////        } catch (Exception exc) {
////            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
////        }
//    }
//    //method to show file chooser
//    private void showFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }
//    //handling the image chooser activity result
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imageView.setImageBitmap(bitmap);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    //method to get the file path from uri
//    public String getPath(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor.close();
//
//        cursor = getContentResolver().query(
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//
//        return path;
//    }
//    //Requesting permission
//    private void requestStoragePermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//            return;
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            //If the user has denied the permission previously your code will come to this block
//            //Here you can explain why you need this permission
//            //Explain here why you need this permission
//        }
//        //And finally ask for the permission
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//    }
//    //This method will be called when the user will tap on allow or deny
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        //Checking the request code of our request
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//
//            //If permission is granted
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //Displaying a toast
//                Toast.makeText(this, "Permission granted.", Toast.LENGTH_LONG).show();
//            } else {
//                //Displaying another toast if permission is not granted
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        if (v == buttonChoose) {
//            showFileChooser();
//        }
//        if (v == buttonUpload) {
//            uploadMultipart();
//        }
//    }
//
//
//    }
//
