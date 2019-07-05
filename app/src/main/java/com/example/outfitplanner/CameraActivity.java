package com.example.outfitplanner;

import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CameraActivity extends AppCompatActivity {

    Camera camera;
    ShowCamera showCamera;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        frameLayout = findViewById(R.id.frameLayout);
        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);

    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
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