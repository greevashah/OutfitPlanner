package com.example.outfitplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BottomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
    }

    public void chooseBottom(View view){
        //start camera module

        //save the image

        //set the image in imageView
    }

    public void goToTop(View view){
        Intent intent = new Intent(this, BottomActivity.class);
        startActivity(intent);
    }

    public void goToResult(View view){
        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }
}
