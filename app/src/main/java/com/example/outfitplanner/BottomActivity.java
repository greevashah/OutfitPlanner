package com.example.outfitplanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BottomActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String currentPhotoPathBottom="";
    Bitmap imageBottom;
    String topColor, bottomColor;
    byte[] topByte, bottomByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
        Intent intent = getIntent();
        topColor = intent.getStringExtra("topColor");
        bottomColor = intent.getStringExtra("bottomColor");
        topByte = intent.getByteArrayExtra("topByte");
        bottomByte = intent.getByteArrayExtra("bottomByte");
        TextView tv = findViewById(R.id.colorTextViewB);
        if(bottomByte!=null){
            imageBottom = BitmapFactory.decodeByteArray(bottomByte, 0, bottomByte.length);
            ImageView iv = findViewById(R.id.bottomImageViewB);
            iv.setImageBitmap(imageBottom);
        }
        if(bottomColor!=null){
            String result = "Bottom color: "+bottomColor;
            tv.setText(result);
        }
        else{
            tv.setText("Bottom Apparel");
        }
    }

    public void chooseBottom(View view){
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }else {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePicture.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePicture, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /*private File createImageFile()throws IOException {
        //Create file name
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="BOT_"+timeStamp+"_";
//        fileName+=imageFileName;
        File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFileName,".jpg",storageDir);
        currentPhotoPathBottom= image.getAbsolutePath();
        return image;
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)  {
//        File imgFile = new  File(currentPhotoPathBottom);
        if(requestCode==REQUEST_TAKE_PHOTO && resultCode==RESULT_OK) {
//            if(imgFile.exists()) {
            Bundle extras = data.getExtras();
            imageBottom =(Bitmap) extras.get("data");
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            imageBottom = Bitmap.createBitmap(imageBottom, 0, 0, imageBottom.getWidth(), imageBottom.getHeight(), matrix, true);
            ImageView iv = findViewById(R.id.bottomImageViewB);
            iv.setImageBitmap(imageBottom);
            String encodedString;
            try{
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBottom.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                bottomByte = baos.toByteArray();
                encodedString = Base64.encodeToString(bottomByte, Base64.DEFAULT);
                uploadImage(encodedString);
                } catch (Exception e) {
                    Log.e("File issues", "Issues in File part");
                    e.printStackTrace();
                }
        }
    }

    public void uploadImage(String encodedImageBottom) {
        Log.i("Url",encodedImageBottom);
        JSONObject json = new JSONObject();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(BottomActivity.this);
            json.put("data",encodedImageBottom);
            json.put("auth","");
            final String jsonString = json.toString();
            String url = "https://outfitplanner-api.herokuapp.com";
            Log.i("Volley","starting API stuff");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
                @Override
                public void onResponse(String response){
                    Log.i("LOG VOLLEY","reposnse "+response);
                    Toast.makeText(BottomActivity.this, "Image uploaded",Toast.LENGTH_LONG).show();
                    TextView tv = findViewById(R.id.colorTextViewB);
                    String result = "Bottom color: "+response;
                    tv.setText(result);
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    Log.e("LOG ERROR",error.toString());
                    Toast.makeText(BottomActivity.this, "Capture Image Again",Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return jsonString.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonString, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response != null) {
//                        responseString = String.valueOf(response.data);
                        bottomColor = new String(response.data);
                        Log.i("respo",bottomColor);
                    }
                    return Response.success(bottomColor, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            requestQueue.add(stringRequest);
        } catch (JSONException e){ e.printStackTrace();}
    }

    public void goToTop(View view){
        finish();
        Intent intent = new Intent(this, TopActivity.class);
        intent.putExtra("topColor", topColor);
        intent.putExtra("bottomColor", bottomColor);
        intent.putExtra("topByte", topByte);
        intent.putExtra("bottomByte", bottomByte);
        startActivity(intent);
    }

    public void goToResult(View view){
        finish();
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("topColor", topColor);
        intent.putExtra("bottomColor", bottomColor);
        intent.putExtra("topByte", topByte);
        intent.putExtra("bottomByte", bottomByte);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, TopActivity.class);
        intent.putExtra("topColor", topColor);
        intent.putExtra("bottomColor", bottomColor);
        intent.putExtra("topByte", topByte);
        intent.putExtra("bottomByte", bottomByte);
        startActivity(intent);
    }
}