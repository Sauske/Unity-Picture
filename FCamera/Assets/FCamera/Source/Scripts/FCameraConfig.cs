using UnityEngine;
using System.Collections;

namespace FCamera
{
    // 图片格式
    public enum ImageFormat
    {
        PNG,
        JPEG,
    }

    // 视频格式
    public enum VideoFormat
    {
        MP4,
        AVI,
    }

    // 屏幕朝向
    public enum ScreenOrientation
    {
        Horizontal,
        Vertical
    }


    public class FCameraConfig
    {
        // 照片格式
        public static ImageFormat IFormat = ImageFormat.JPEG;
        // 图片品质
        public static int IQuality = 100;
        // 图片尺寸
        public static int IWidth = 1080;
        public static int IHeight = 720;
        // 图片是否压缩
        public static bool ICompression = true;

        // 视频格式
        public static VideoFormat VFormat = VideoFormat.MP4;
        // 视频分辨率
        public static int VWidth = 1080;
        public static int VHeight = 720;

        // 默认屏幕朝向
        public static ScreenOrientation screenOrientation = ScreenOrientation.Horizontal;

        // 是否自动翻转屏幕
        public static bool ScreenAutoRotation = false;
    }

}
