package com.fcamera.flibrary;

/**
 * Created by Administrator on 2017/12/7.
 */

public interface FCameraListener {
    void onCloseFCamera();
    void onPictureTaken();
    void onFCameraError();
    void onSavePictureException();
}
