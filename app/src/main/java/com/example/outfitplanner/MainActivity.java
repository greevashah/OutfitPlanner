package com.example.outfitplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    String topColor, bottomColor;
    byte[] topByte, bottomByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OpenCVLoader.initDebug();
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        topColor = intent.getStringExtra("topColor");
        bottomColor = intent.getStringExtra("bottomColor");
        topByte = intent.getByteArrayExtra("topByte");
        bottomByte = intent.getByteArrayExtra("bottomByte");
    }

    public void goToTop(View view){
        Intent intent = new Intent(this, TopActivity.class);
        intent.putExtra("topColor", topColor);
        intent.putExtra("bottomColor", bottomColor);
        intent.putExtra("topByte", topByte);
        intent.putExtra("bottomByte", bottomByte);
        startActivity(intent);
    }

    public void exitMain(View view){
        finish();
        System.exit(0);
    }
}
