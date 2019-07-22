package com.example.outfitplanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
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
    public File createImageFile()throws IOException {
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
        File imgFile=new File(currentPhotoPathTop);
        if(resultCode==RESULT_OK) {
            if(imgFile.exists()) {
                imageTop = BitmapFactory.decodeFile(currentPhotoPathTop);
                ImageView iv = findViewById(R.id.topImageViewT);
                iv.setImageBitmap(imageTop);
                String encodedString;
                try{
                    final InputStream inStreamImage = new FileInputStream(currentPhotoPathTop);
                    byte[] bytes;
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    while ((bytesRead = inStreamImage.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                    bytes = output.toByteArray();
                    encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
                    uploadImage(encodedString);
                } catch (Exception e) {
                    Log.e("File issues", "Issues in File part");
                    e.printStackTrace();
                }
            }
        }
    }

    public void uploadImage(String encodedImageTop) {
        Log.i("Url",encodedImageTop);
        JSONObject json = new JSONObject();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(TopActivity.this);
            json.put("data",encodedImageTop);
            json.put("auth","");
            final String jsonString = json.toString();
            String url = "https://outfitplanner-api.herokuapp.com";
            Log.i("Volley","starting API stuff");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
                @Override
                public void onResponse(String response){
                    Log.i("LOG VOLLEY","reposnse "+response);
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    Log.e("LOG ERROR",error.toString());
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
                    String responseString="";
                    if (response != null) {

//                        responseString = String.valueOf(response.data);
                        responseString = new String(response.data);
                        Log.i("respo",responseString);

                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            requestQueue.add(stringRequest);
        } catch (JSONException e){ e.printStackTrace();}


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