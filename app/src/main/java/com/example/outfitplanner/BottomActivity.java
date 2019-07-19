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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.OpenCVLoader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BottomActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String currentPhotoPathBottom="";
    String topColor, bottomColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OpenCVLoader.initDebug();
        setContentView(R.layout.activity_bottom);
        Intent intent = getIntent();
        topColor = intent.getStringExtra("topColor");
    }

    public void chooseBottom(View view){
        Intent takePicture=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager())!=null){
            File photoFile=null;
            try{
                photoFile=createImageFile();
            }catch(IOException ix){
                //Error
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
        String imageFileName="BOT_"+timeStamp+"_";
//        fileName+=imageFileName;
        File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFileName,".jpg",storageDir);
        currentPhotoPathBottom= image.getAbsolutePath();
        return image;
    }

    Bitmap imageBottom;
    String encodedImageBottom, urlEncodedImageBottom;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);
        File imgFile = new  File(currentPhotoPathBottom);
        if(resultCode==RESULT_OK) {
            if(imgFile.exists()) {
                imageBottom = BitmapFactory.decodeFile(currentPhotoPathBottom);
                ImageView iv = findViewById(R.id.bottomImageViewB);
                iv.setImageBitmap(imageBottom);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBottom.compress(Bitmap.CompressFormat.PNG, 0, baos);
                byte[] imagedata = baos.toByteArray();
                encodedImageBottom = Base64.encodeToString(imagedata, Base64.DEFAULT);
                try {
                    urlEncodedImageBottom = URLEncoder.encode(encodedImageBottom, "utf-8");
                } catch (UnsupportedEncodingException e) { e.printStackTrace();}
                JSONObject json = new JSONObject();
                try {
                    json.put("data",urlEncodedImageBottom);
                    json.put("auth","");
                } catch (JSONException e){ e.printStackTrace();}
                String url = "https://outfitplanner-api.herokuapp.com";
                RequestQueue queue = Volley.newRequestQueue(BottomActivity.this);
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new com.android.volley.Response.Listener<String>() {
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                bottomColor = response;
                            }
                        }, new com.android.volley.Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BottomActivity.this,"That didn't work, do it again.", Toast.LENGTH_SHORT).show();
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
//                Mat mat = new Mat();
//                Bitmap bmp32 = imageBottom.copy(Bitmap.Config.ARGB_8888, true);
//                Utils.bitmapToMat(imageBottom, mat);
//                int x = imageBottom.getHeight()/2;
//                int y = imageBottom.getWidth()/2;
//                int colour = imageBottom.getPixel(x, y);
//                int red = Color.red(colour);
//                int blue = Color.blue(colour);
//                int green = Color.green(colour);
//                bottomColor = ColorUtils.getColorNameFromRgb(red, green, blue);
//                Log.i("topColor",topColor);
//                Log.i("bottomColor",bottomColor);
            }
        }
    }

    public void goToTop(View view){
        Intent intent = new Intent(this, TopActivity.class);
        intent.putExtra("topColor",topColor);
        intent.putExtra("bottomColor", bottomColor);
        startActivity(intent);
    }

    public void goToResult(View view){
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("topColor",topColor);
        intent.putExtra("bottomColor", bottomColor);
        startActivity(intent);
    }
}