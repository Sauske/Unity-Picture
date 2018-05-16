package com.fcamera.flibrary;

import android.Manifest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2017/12/4.
 */

public class FCameraActivity extends AppCompatActivity {
    // tag
    private static final String TAG = "FCameraActivity";

    // 权限
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    // 请求权限数量
    private int mRequestPermissionCount = 0;

    // 参数
    private FCameraParam mCameraParam;

    // 相机
    private CameraView mCameraView;

    // 返回ImageView
    private ImageView mCloseIView;

    // 切换摄像头ImageView
    private ImageView mSwitchIView;

    // 闪光灯 ImageView
    private ImageView mFlashIView;

    // 图片视频确认ImageView
    private ImageView mSureIView;

    // 图片视频取消ImageView
    private ImageView mCancelIV;

    // 照相和录像ImageButton
    private ImageButton mTakeIButton;

    // 照片展示
    private ImageView mPictureView;

    // 视频展示
    private VideoView mVideoView;

    // 数据
    // 预览bitmap实例
    private Bitmap mPreviewBitmap;

    //
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fcamera_activity);

        // 获取参数
        mCameraParam = FCameraParam.GetParam();

        // 获取相机实例
        mCameraView = findViewById(R.id.camera);
        mCameraView.addCallback(mCameraCallback);

        // 退出
        mCloseIView = findViewById(R.id.image_back);
        mCloseIView.setOnClickListener(mOnClickListener);

        // 切换摄像头
        mSwitchIView = findViewById(R.id.image_switch);
        mSwitchIView.setOnClickListener(mOnClickListener);

        // 闪光灯
        mFlashIView = findViewById(R.id.image_flash);
        mFlashIView.setOnClickListener(mOnClickListener);

        // 确认按钮
        mSureIView = findViewById(R.id.image_sure);
        mSureIView.setOnClickListener(mOnClickListener);

        // 取消按钮
        mCancelIV= findViewById(R.id.image_cancel);
        mCancelIV.setOnClickListener(mOnClickListener);

        // 照相录视频按钮
        mTakeIButton = findViewById(R.id.image_take);
        mTakeIButton.setOnClickListener(mOnClickListener);

        // 照片预览
        mPictureView = findViewById(R.id.picture_preview);

        // 视频阅览
        mVideoView = findViewById(R.id.video_preview);

        // 初始化
        Init();

        // 检查权限
        CheckPermission();
    }

    // 初始化
    private void Init()
    {
        // 禁止自动旋转屏幕
        if(!mCameraParam.IsAutoRotation)
        {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

        // 初始化默认界面
        if(mCameraParam.OperationType == FCameraParam.FOperationType.RECORDER)
        {
            InitRecordUI();
        } else {
            InitCaptureUI();
        }
    }

    // 初始化拍照界面
    private void InitCaptureUI()
    {
        // 隐藏内容
        mCloseIView.setVisibility(View.VISIBLE);
        mSwitchIView.setVisibility(View.VISIBLE);
        mFlashIView.setVisibility(View.VISIBLE);
        mTakeIButton.setVisibility(View.VISIBLE);

        // 显示内容
        mSureIView.setVisibility(View.INVISIBLE);
        mCancelIV.setVisibility(View.INVISIBLE);
        mPictureView.setVisibility(View.INVISIBLE);
        mVideoView.setVisibility(View.INVISIBLE);
    }

    // 初始化录像界面
    private void InitRecordUI()
    {
        // 隐藏内容
        mCloseIView.setVisibility(View.VISIBLE);
        mSwitchIView.setVisibility(View.VISIBLE);
        mFlashIView.setVisibility(View.VISIBLE);
        mTakeIButton.setVisibility(View.VISIBLE);

        // 显示内容
        mSureIView.setVisibility(View.INVISIBLE);
        mCancelIV.setVisibility(View.INVISIBLE);
        mPictureView.setVisibility(View.INVISIBLE);
        mVideoView.setVisibility(View.INVISIBLE);
    }

    // 初始化拍照预览界面
    private void InitPicturePreviewUI()
    {
        // 隐藏内容
        mCloseIView.setVisibility(View.INVISIBLE);
        mSwitchIView.setVisibility(View.INVISIBLE);
        mFlashIView.setVisibility(View.INVISIBLE);
        mTakeIButton.setVisibility(View.INVISIBLE);
        mVideoView.setVisibility(View.INVISIBLE);

        // 显示内容
        mSureIView.setVisibility(View.VISIBLE);
        mCancelIV.setVisibility(View.VISIBLE);
        mPictureView.setVisibility(View.VISIBLE);
    }

    // 设置预览界面显示
    private void SetPicturePreview(Bitmap bitmap)
    {
        mPreviewBitmap = bitmap;
        mPictureView.setImageBitmap(bitmap);
    }

    // 关闭预览界面
    private void HidePicturePreview(boolean isSave)
    {
        if(isSave)
        {
            finish();

            GetHandler().post(new Runnable() {
                @Override
                public void run() {
                    SavePicture();
                }
            });
        }
        else{
            InitCaptureUI();
        }
    }

    private void SavePicture()
    {
        // 保存图片格式
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        if(mCameraParam.CaptureParam.Format == FCameraParam.ImageFormat.JPEG)
        {
            format = Bitmap.CompressFormat.JPEG;
        }
        else if(mCameraParam.CaptureParam.Format == FCameraParam.ImageFormat.PNG){
            format = Bitmap.CompressFormat.PNG;
        }

        // 保存图片
        boolean succeed = false;

        // 如果压缩就先压缩在保存
        if(mCameraParam.CaptureParam.IsCompression)
        {
            // 保存临时图片到本地
            succeed = FCameraUtils.SaveBitmapToPath(mPreviewBitmap,
                    mCameraParam.CaptureParam.GetCompressPath(), format, mCameraParam.CaptureParam.Quality);

            if(succeed)
            {
                // 压缩图片
                Luban.with(this)
                        .load(mCameraParam.CaptureParam.GetCompressPath())
                        .ignoreBy(100)
                        .setTargetDir(FCameraParam.GetDirPath(mCameraParam.CaptureParam.FilePath))
                        .setCompressListener(new OnCompressListener(){
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(File file) {
                                // 重命名图片
                                File oldFile = new File(mCameraParam.CaptureParam.FilePath);
                                if (oldFile.isFile() && oldFile.exists()) {
                                    oldFile.delete();
                                }

                                file.renameTo(new File(mCameraParam.CaptureParam.FilePath));

                                // 删除源图片
                                File uFile = new File(mCameraParam.CaptureParam.GetCompressPath());
                                if (uFile.exists()) {
                                    uFile.delete();
                                }

                                mCameraParam.Listener.onPictureTaken();

                                DestoryHandler();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "Luban onError " + e.toString());
                                mCameraParam.Listener.onSavePictureException();

                                DestoryHandler();
                            }
                        }).launch();
            }
            else{
                mCameraParam.Listener.onSavePictureException();
            }
        }
        else{
            succeed = FCameraUtils.SaveBitmapToPath(mPreviewBitmap,
                    mCameraParam.CaptureParam.FilePath, format, mCameraParam.CaptureParam.Quality);

            if(succeed)
            {
                mCameraParam.Listener.onPictureTaken();
            }
            else{
                mCameraParam.Listener.onSavePictureException();
            }

            DestoryHandler();
        }
    }

    // 初始化视频预览界面
    private void InitVideoPreviewUI()
    {
        // 隐藏内容
        mCloseIView.setVisibility(View.INVISIBLE);
        mSwitchIView.setVisibility(View.INVISIBLE);
        mFlashIView.setVisibility(View.INVISIBLE);
        mTakeIButton.setVisibility(View.INVISIBLE);
        mPictureView.setVisibility(View.INVISIBLE);

        // 显示内容
        mSureIView.setVisibility(View.VISIBLE);
        mCancelIV.setVisibility(View.VISIBLE);
        mVideoView.setVisibility(View.VISIBLE);
    }

    // 打开相机开始预览
    private void CameraStart()
    {
        mCameraView.start();
    }

    // 拍照结果
    private void CaptureResult(Bitmap bitmap)
    {

        Matrix matrix = new Matrix();

        if(mCameraParam.Orientation == FCameraParam.ScreenOrientation.Vertical)
        {
            matrix.setRotate(90);
        }
        else{
            matrix.setRotate(270);
        }

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        InitPicturePreviewUI();
        SetPicturePreview(bitmap);
    }

    // 点击事件
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.image_take)
        {
            if (mCameraView != null) {
                mCameraView.takePicture();
            }
        }
        else if(id == R.id.image_back)
        {
            finish();
            mCameraParam.Listener.onFCameraError();
        }
        else if(id == R.id.image_sure)
        {
            HidePicturePreview(true);
        }
        else if(id == R.id.image_cancel)
        {
            HidePicturePreview(false);
        }
        }
    };

    // 相机回调事件
    private CameraView.Callback mCameraCallback = new CameraView.Callback() {
        @Override
        public void onCameraOpened(CameraView cameraView) {
            super.onCameraOpened(cameraView);
            mCameraView.setAspectRatio(AspectRatio.of(mCameraParam.CaptureParam.Width, mCameraParam.CaptureParam.Height));
            mCameraView.setPictureSize(mCameraParam.CaptureParam.Width, mCameraParam.CaptureParam.Height);
            mCameraView.setAutoFocus(true);
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            super.onCameraClosed(cameraView);
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            // 拍照完成处理
            CaptureResult(BitmapFactory.decodeByteArray(data, 0, data.length));
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCameraView != null)
        {
            mCameraView.start();
        }
    }

    @Override
    protected void onPause() {
        if(mCameraView != null)
        {
            mCameraView.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    // 权限获取操作
    private void CheckPermission()
    {
        mRequestPermissionCount = 0;

        // 权限检查
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            List<String> pList = new ArrayList();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                pList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            {
                pList.add(Manifest.permission.RECORD_AUDIO);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                pList.add(Manifest.permission.CAMERA);
            }

            mRequestPermissionCount = pList.size();
            if(mRequestPermissionCount > 0)
            {
                String[] permissions = new String[mRequestPermissionCount];
                for(int i = 0; i < mRequestPermissionCount; i++){
                    permissions[i] = pList.get(i);
                }

                ActivityCompat.requestPermissions(FCameraActivity.this, permissions, REQUEST_CAMERA_PERMISSION);
            }
            else{
                CameraStart();
            }
        } else {
            CameraStart();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CAMERA_PERMISSION) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(permissions.length == mRequestPermissionCount && grantResults.length == mRequestPermissionCount)
                {
                    for(int i = 0; i < grantResults.length; i++)
                    {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            // 权限获取失败,提示去设置里修改
                            finish();
                            return;
                        }
                    }

                    // 权限获取成功,初始化相机
                    CameraStart();
                }
            }
        }
    }

    // 后台线程
    private Handler GetHandler()
    {
        if (handler == null) {
            HandlerThread thread = new HandlerThread("fcameraHandler");
            thread.start();
            handler = new Handler(thread.getLooper());
        }
        return handler;
    }

    // 销毁
    private void DestoryHandler()
    {
        if (handler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                handler.getLooper().quitSafely();
            } else {
                handler.getLooper().quit();
            }
            handler = null;
        }
    }

}
