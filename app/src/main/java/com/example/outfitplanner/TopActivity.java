package com.example.outfitplanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.UnsupportedEncodingException;

public class TopActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 1;
    Bitmap imageTop;
    String topColor, bottomColor;
    byte[] topByte, bottomByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        Intent intent = getIntent();
        topColor = intent.getStringExtra("topColor");
        bottomColor = intent.getStringExtra("bottomColor");
        topByte = intent.getByteArrayExtra("topByte");
        bottomByte = intent.getByteArrayExtra("bottomByte");
        if(topByte!=null) {
            imageTop = BitmapFactory.decodeByteArray(topByte, 0, topByte.length);
            ImageView iv = findViewById(R.id.topImageViewT);
            iv.setImageBitmap(imageTop);
        }
    }

    public void chooseTop(View view){
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }else {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePicture.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePicture, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /*public File createImageFile()throws IOException {
        //Create file name
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="TOP_"+timeStamp+"_";
//        fileName+=imageFileName;
        File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFileName,".jpg",storageDir);
        currentPhotoPathTop = image.getAbsolutePath();
        return image;
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        File imgFile=new File(currentPhotoPathTop);
        if(requestCode==REQUEST_TAKE_PHOTO && resultCode==RESULT_OK) {
//            if(imgFile.exists()) {
            Bundle extras = data.getExtras();
            imageTop =(Bitmap) extras.get("data");
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            imageTop = Bitmap.createBitmap(imageTop, 0, 0, imageTop.getWidth(), imageTop.getHeight(), matrix, true);
            ImageView iv = findViewById(R.id.topImageViewT);
            iv.setImageBitmap(imageTop);
            String encodedString;
            try{
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageTop.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                topByte = baos.toByteArray();
                encodedString = Base64.encodeToString(topByte, Base64.DEFAULT);
                Toast.makeText(TopActivity.this, "Wait for the image to upload", Toast.LENGTH_LONG);
                uploadImage(encodedString);
            } catch (Exception e) {
                Log.e("File issues", "Issues in File part");
                e.printStackTrace();
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
                    Toast.makeText(TopActivity.this, "Image uploaded",Toast.LENGTH_LONG).show();
                    TextView tv = findViewById(R.id.colorTextViewB);
                    String result = "Top color: "+response;
                    tv.setText(result);
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
                public byte[] getBody() {
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
                        topColor = new String(response.data);
                        Log.i("respo",topColor);
                    }
                    return Response.success(topColor, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            requestQueue.add(stringRequest);
        } catch (JSONException e){ e.printStackTrace();}
    }

    public void goToBottom(View view){
        finish();
        Intent intent = new Intent(this, BottomActivity.class);
        intent.putExtra("topColor", topColor);
        intent.putExtra("bottomColor", bottomColor);
        intent.putExtra("topByte", topByte);
        intent.putExtra("bottomByte", bottomByte);
        startActivity(intent);
    }

    public void goToMain(View view){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}