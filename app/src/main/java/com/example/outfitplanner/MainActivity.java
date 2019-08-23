package com.example.outfitplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
