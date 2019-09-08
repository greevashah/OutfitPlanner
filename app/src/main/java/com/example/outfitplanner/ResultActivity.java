package com.example.outfitplanner;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    String topColor, bottomColor, result;
    byte[] topByte, bottomByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView tv = findViewById(R.id.resultViewR);
        Intent intent = getIntent();
        topColor = intent.getStringExtra("topColor");
        bottomColor = intent.getStringExtra("bottomColor");
        topByte = intent.getByteArrayExtra("topByte");
        bottomByte = intent.getByteArrayExtra("bottomByte");
        ColorDictionary cd = new ColorDictionary();
        String verdict = cd.GetResult(topColor, bottomColor);
        if(topColor!=null && bottomColor!=null)
            result = "Topwear: " + topColor + "\n\nBottomwear: " + bottomColor + "\n\nSuggestion:" + verdict;
        else if(topColor==null && bottomColor!=null)
            result = "Topwear: Please capture top again\n\nBottomwear: " + bottomColor;
        else if(bottomColor==null && topColor!=null)
            result = "Topwear: " + topColor + "\n\nBottomwear: Please capture top again";
        else
            result = "Please try again.";
        tv.setText(result);
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
        Intent intent = new Intent(this, BottomActivity.class);
        intent.putExtra("topColor", topColor);
        intent.putExtra("bottomColor", bottomColor);
        intent.putExtra("topByte", topByte);
        intent.putExtra("bottomByte", bottomByte);
        startActivity(intent);
    }
}
