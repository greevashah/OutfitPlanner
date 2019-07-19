package com.example.outfitplanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TopActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 1;
    public String currentPhotoPathTop=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
//        if(imageTop != null){
//            setTopImage();
//        }
    }

    public void chooseTop(View view){
        Intent takePicture=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager())!=null){
            File photoFile=null;
            try{
                photoFile=createImageFile();
            }catch(IOException ix){
                Log.i( "Info","Error");
            }
            if(photoFile!=null){
                Uri photoURI= FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile);
//                fileName+=photoURI.toString();
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(takePicture, REQUEST_TAKE_PHOTO);
            }
        }
        //start camera module

        //save the image

        //set the image in imageView
    }
    private File createImageFile()throws IOException {
        //Create file name
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="TOP_"+timeStamp+"_";
//        fileName+=imageFileName;
        File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFileName,".jpg",storageDir);
        currentPhotoPathTop = image.getAbsolutePath();
        return image;
    }

    Bitmap imageTop;
    String encodedImageTop, topColor, urlEncodedImageTop;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File imgFile=new File(currentPhotoPathTop);
        if(resultCode==RESULT_OK) {
            if(imgFile.exists()) {
                imageTop = BitmapFactory.decodeFile(currentPhotoPathTop);
                ImageView iv = findViewById(R.id.topImageViewT);
                iv.setImageBitmap(imageTop);
                uploadImage(imageTop);
            }
        }
    }

    public void uploadImage(Bitmap imageTop) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageTop.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] imagedata = baos.toByteArray();
        encodedImageTop = Base64.encodeToString(imagedata, Base64.DEFAULT);
        try {
            urlEncodedImageTop = URLEncoder.encode(encodedImageTop, "utf-8");
        } catch (UnsupportedEncodingException e) { e.printStackTrace();}
        Log.i("Url",urlEncodedImageTop);
        JSONObject json = new JSONObject();
        try {
            json.put("data",urlEncodedImageTop);
            json.put("auth","");
        } catch (JSONException e){ e.printStackTrace();}
        String url = "https://outfitplanner-api.herokuapp.com";
        RequestQueue queue = Volley.newRequestQueue(TopActivity.this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    public void onResponse(String response) {
                        topColor = response;
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TopActivity.this,"That didn't work, do it again.", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(stringRequest);
    }

    public void goToBottom(View view){
        Intent intent = new Intent(this, BottomActivity.class);
        intent.putExtra("topColor",topColor);
        startActivity(intent);
    }

    public void goToMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}