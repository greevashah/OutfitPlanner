package com.example.outfitplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView tv = findViewById(R.id.resultViewR);
        Intent intent = getIntent();
        String topColor = intent.getStringExtra("topColor");
        String bottomColor = intent.getStringExtra("bottomColor");
        String result = "Top: " + topColor + "\n" + "Bottom: " + bottomColor;
        tv.setText(result);
    }

    public void goToBottom(View view){
        Intent intent = new Intent(this, BottomActivity.class);
        startActivity(intent);
    }

    public void goToMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void exitMain(View view){
        finish();
        System.exit(0);
    }
}
