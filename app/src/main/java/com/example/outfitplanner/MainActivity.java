package com.example.outfitplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    }

    public void goToTop(View view){
        finish();
        Intent intent = new Intent(this, TopActivity.class);
        startActivity(intent);
    }

    public void exitMain(View view){
        finish();
    }
}
