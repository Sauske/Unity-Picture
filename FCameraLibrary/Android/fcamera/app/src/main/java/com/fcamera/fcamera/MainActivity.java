package com.fcamera.fcamera;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fcamera.flibrary.FCameraActivity;
import com.fcamera.flibrary.FCameraListener;
import com.fcamera.flibrary.FCameraParam;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FCAMERA_TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OckTakePicture(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void OckTakePicture(View view)
    {
        FCameraParam cameraParam = new FCameraParam();
        cameraParam.CreateCaptureParam(FCameraParam.ImageFormat.JPEG, 100, 1280, 720, true,
                Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "TEST.JPEG");
        cameraParam.OperationType = FCameraParam.FOperationType.CAPTURE;
        cameraParam.Orientation = FCameraParam.ScreenOrientation.Vertical;
        cameraParam.IsAutoRotation = false;
        cameraParam.Listener = new FCameraListener() {
            @Override
            public void onCloseFCamera() {
                Log.e(TAG, "onCloseFCamera");
            }

            @Override
            public void onPictureTaken() {
                Log.e(TAG, "onPictureTaken");
            }

            @Override
            public void onFCameraError() {
                Log.e(TAG, "onFCameraError");
            }

            @Override
            public void onSavePictureException() {
                Log.e(TAG, "onSavePictureException");
            }
        };

        FCameraParam.SetParam(cameraParam);

        startActivityForResult(new Intent(MainActivity.this, FCameraActivity.class), 100);
    }

    public void OckRecord(View view)
    {

    }
}
