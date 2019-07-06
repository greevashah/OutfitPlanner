package com.example.outfitplanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class BottomActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
    }

    public void chooseBottom(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1); //calls function onActivityResult directly
        }
        //start camera module

        //save the image

        //set the image in imageView
    }

    Bitmap imageBottom;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            ImageView iv = findViewById(R.id.bottomImageViewB);
            Bundle extras = data.getExtras();
            imageBottom = (Bitmap) extras.get("data");
            iv.setImageBitmap(imageBottom);
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