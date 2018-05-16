package com.fcamera.flibrary;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/12/7.
 */

public class FCameraUtils {

    /**
     * 保存图片到路径
     * @param bitmap
     * @param filePath
     * @param format
     */
    public static boolean SaveBitmapToPath(Bitmap bitmap, String filePath, Bitmap.CompressFormat format, int quality)
    {
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            BufferedOutputStream buff = new BufferedOutputStream(outputStream);
            bitmap.compress(format, quality, buff);
            buff.flush();
            buff.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
