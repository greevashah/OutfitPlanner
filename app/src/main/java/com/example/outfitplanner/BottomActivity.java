package com.example.outfitplanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Region;
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

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BottomActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String currentPhotoPathBottom="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OpenCVLoader.initDebug();
        setContentView(R.layout.activity_bottom);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File imgFile = new  File(currentPhotoPathBottom);
        if(resultCode==RESULT_OK) {
            if(imgFile.exists()) {
                Bitmap imageBottom = BitmapFactory.decodeFile(currentPhotoPathBottom);
                ImageView iv = (ImageView) findViewById(R.id.bottomImageViewB);
                iv.setImageBitmap(imageBottom);
                Mat mat = new Mat();
//                Bitmap bmp32 = imageBottom.copy(Bitmap.Config.ARGB_8888, true);
                Utils.bitmapToMat(imageBottom, mat);
            }
        }
    }

    public void goToTop(View view){
        Intent intent = new Intent(this, TopActivity.class);
        startActivity(intent);
    }

    public void goToResult(View view){
        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }
}