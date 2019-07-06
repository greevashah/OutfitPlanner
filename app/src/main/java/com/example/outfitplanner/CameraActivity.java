package com.example.outfitplanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CameraActivity extends AppCompatActivity {
    int f;
    Camera camera;
    ShowCamera showCamera;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Intent intent = getIntent();
        f = intent.getIntExtra("flag",0);
        frameLayout = findViewById(R.id.frameLayout);
        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);

    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            /*if(f==1) {
                Toast.makeText(getApplicationContext(), "Picture Taken",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, TopActivity.class);
                intent.putExtra("image_arr", data);
                setResult(RESULT_OK, intent);
                startActivity(intent);
                camera.stopPreview();
                if (camera != null) {
                    camera.release();
                }
                finish();
            }
            else {
                ImageView iv = findViewById(R.id.bottomImageViewB);
                Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);
                iv.setImageBitmap(picture);
            }*/
            File picture_file = getOutputMediaFile();
            if(picture_file == null) {
                return;
            }
            else {
                try{
                    FileOutputStream fos = new FileOutputStream((picture_file));
                    fos.write(data);
                    fos.close();
                    camera.startPreview();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)){
            return null;
        }
        else {
            File folder_gui = new File(Environment.getExternalStorageDirectory()+File.separator+"OutfitPlanner");
            if(!folder_gui.exists()) {
                folder_gui.mkdir();
            }
        File outputFile = new File(folder_gui,"temp.jpg");
            return outputFile;
        }
    }

    public void captureImage(View view) {
        if(camera!=null) {
            camera.takePicture(null, null, mPictureCallback);
        }
    }
}