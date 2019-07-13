package com.example.outfitplanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;
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
        String imageFileName="TOP_"+timeStamp+"_";
//        fileName+=imageFileName;
        File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFileName,".jpg",storageDir);
        currentPhotoPathTop = image.getAbsolutePath();
        return image;
    }

    Bitmap imageTop;
    String topColor;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File imgFile=new File(currentPhotoPathTop);
        if(resultCode==RESULT_OK) {
            if(imgFile.exists()) {
                imageTop = BitmapFactory.decodeFile(currentPhotoPathTop);
                ImageView iv = findViewById(R.id.topImageViewT);
                iv.setImageBitmap(imageTop);
                Mat mat = new Mat();
//                Bitmap bmp32 = imageBottom.copy(Bitmap.Config.ARGB_8888, true);
//                Utils.bitmapToMat(imageTop, mat);
                int x = imageTop.getHeight()/2;
                int y = imageTop.getWidth()/2;
                int colour = imageTop.getPixel(x, y);
                int red = Color.red(colour);
                int blue = Color.blue(colour);
                int green = Color.green(colour);
                topColor = ColorUtils.getColorNameFromRgb(red, green, blue);
            }
        }
    }

//    public void setTopImage(){
//        Bitmap imageTop = BitmapFactory.decodeFile(currentPhotoPathTop);
//        ImageView iv = (ImageView) findViewById(R.id.topImageViewT);
//        iv.setImageBitmap(imageTop);
//    }

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