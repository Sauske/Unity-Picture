package com.fcamera.flibrary;

import java.io.File;

/**
 * Created by Administrator on 2017/12/6.
 */

public class FCameraParam {

    /** 操作类型 */
    public static class FOperationType
    {
        public static final int CAPTURE = 0;
        public static final int RECORDER = 1;
        public static final int BOTH = 2;
    }

    /** 图片格式 */
    public static class ImageFormat
    {
        public static final int PNG = 0;
        public static final int JPEG = 1;
    }

    /** 视频格式 */
    public static class VideoFormat
    {
        public static final int MP4 = 0;
        public static final int AVI = 1;
    }

    /** 屏幕朝向 */
    public static class ScreenOrientation
    {
        public static final int Horizontal = 0;
        public static final int Vertical = 1;
    }

    /** 照相参数 */
    public class FCaptureParam
    {
        // 照片格式
        public int Format = ImageFormat.JPEG;
        // 照片品质
        public int Quality = 100;
        // 照片宽
        public int Width = 1080;
        // 照片高
        public int Height = 720;
        // 是否压缩
        public boolean IsCompression = true;
        // 文件路径
        public String FilePath = "";


        // 压缩路径
        public String GetCompressPath()
        {
            String fn = "";
            if(Format == ImageFormat.JPEG)
            {
                fn = "fcamera.jpeg";
            }
            else if(Format == ImageFormat.PNG){
                fn = "fcamera.png";
            }
            return FCameraParam.GetDirPath(FilePath) + File.separator + fn;
        }
    }

    /** 录像参数 */
    public class FRecordParam
    {
        // 录像格式
        public int Format = VideoFormat.MP4;
        // 视频宽
        public int Width = 1080;
        // 视频高
        public int Height = 720;
        // 文件路径
        public String FilePath = "";
    }

    /** 外部设置参数 */
    // 照相参数
    public FCaptureParam CaptureParam = null;
    // 录像参数
    public FRecordParam RecordParam = null;
    // 操作类型
    public int OperationType = FOperationType.BOTH;
    // 屏幕默认方向
    public int Orientation = ScreenOrientation.Horizontal;
    // 屏幕是否自动旋转
    public boolean IsAutoRotation = false;
    // 回调监听
    public FCameraListener Listener = null;

    // 创建照相参数
    public void CreateCaptureParam(int format, int quality, int width, int height, boolean isCompression, String filePath)
    {
        FCaptureParam param = new FCaptureParam();
        param.Format = format;
        param.Quality = quality;
        param.Width = width;
        param.Height = height;
        param.IsCompression = isCompression;
        param.FilePath = filePath;

        CaptureParam = param;
    }

    // 创建录像参数
    public void CreateRecordParam(int format, int width, int height)
    {
        FRecordParam param = new FRecordParam();
        param.Format = format;
        param.Width = width;
        param.Height = height;

        RecordParam = param;
    }

    // 文件所在文件夹路径
    public static String GetDirPath(String filePath) {
        File file = new File(filePath);
        return file.getParent();
    }

    // 文件名称
    public static String GetFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }

    // 相机全局数据
    // 相机参数
    private static FCameraParam cameraParam = null;
    // 设置相机参数
    public static void SetParam(FCameraParam param)
    {
        cameraParam = param;
    }

    // 获取相机参数
    public static FCameraParam GetParam()
    {
        return cameraParam;
    }
}
