package com.heynchy.compress.compressUtill;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.heynchy.compress.entity.LubanEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author CHY
 *         Create at 2018/5/24 14:46.
 */

public class LubanUtil {
    public static LubanEntity getLubrnValue(String filePath) {
        LubanEntity entity = new LubanEntity();
        File file = new File(filePath);
        if (!file.exists()) {
            // 如果文件不存在
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(filePath, options);
        // 获取旋转角度
        int angle = getImageSpinAngle(filePath);
        // 获取原图片的宽度
        int width = options.outWidth;
        // 获取原图片的高度
        int height = options.outHeight;

        /**
         * 计算两条宽和高的比例值scale（设定为短边/长边）
         * 1. 将短边赋予width,将长边赋予height
         * 2. 压缩宽度值thumbW（)，压缩高度值thumbH
         * 3. 压缩文件的大小size
         */
        double size = 0;
        int thumbW = width % 2 == 1 ? width + 1 : width;
        int thumbH = height % 2 == 1 ? height + 1 : height;
        width = thumbW > thumbH ? thumbH : thumbW;
        height = thumbW > thumbH ? thumbW : thumbH;
        double scale = ((double) width / height);

        /**
         * 根据比例值选择不同的处理方案
         */
        if (scale > 0.5625 && scale <= 1) {
            if (height < 1664) {
                // 如果文件小于150kb则不压缩
                if (file.length() / 1024 < 150) {
                    return null;
                }
                /**
                 * 计算(0.5625, 1]该比例值下高度为(0,1664)压缩后文件的大小
                 * 150应该是原作者选定的合适的值（猜测）
                 */
                size = (width * height) / Math.pow(1664, 2) * 150;
                // 设定取值范围
                size = size < 60 ? 60 : size;
            } else if (height >= 1664 && height < 4990) {
                thumbW = width / 2;
                thumbH = height / 2;
                /**
                 * 计算(0.5625, 1]该比例值下高度为[1664,4990)压缩后文件的大小
                 * 300应该是原作者选定的合适的值（猜测）
                 * 2495可能与4990和缩小倍数2有关
                 */
                size = (thumbW * thumbH) / Math.pow(2495, 2) * 300;
                size = size < 60 ? 60 : size;
            } else if (height >= 4990 && height < 10240) {
                thumbW = width / 4;
                thumbH = height / 4;
                /**
                 * 计算(0.5625, 1]该比例值下高度为[4990,10240)压缩后文件的大小
                 * 300应该是原作者选定的合适的值（猜测）
                 * 2560可能与10240和缩小倍数4有关（猜测）
                 */
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 60 ? 60 : size;
            } else {
                int mutilple = height / 1280 == 0 ? 1 : height / 1280;
                thumbW = width / mutilple;
                thumbH = height / mutilple;
                /**
                 * 计算(0.5625, 1]该比例值下高度为[10240,+oo)压缩后文件的大小
                 * 300应该是原作者选定的合适的值（猜测）
                 * 2560未知来源??????????
                 */
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            }
        } else if (scale > 0.5 && scale <= 0.5625) {
            if (height < 1280 && (file.length() / 1024 < 200)) {
                // 如果文件小于150kb且高度小于1280则不压缩
                return null;
            }
            if (height > 1280) {
                int mutilple = height / 1280 == 0 ? 1 : height / 1280;
                thumbW = width / mutilple;
                thumbH = height / mutilple;
                /**
                 * 计算(0.5, 0.5625]该比例值下高度为[1280,+oo)压缩后文件的大小
                 * 400应该是原作者选定的合适的值（猜测）
                 * 1440.0和2560.0应该是原作者选定的合适的值
                 */
                size = (thumbH * thumbW) / (1440.0 * 2560.0) * 400;
                size = size < 100 ? 100 : size;
            }
        } else {
            // 向上舍入取整
            int mutilple = (int) Math.ceil(height / (1280 / scale));
            thumbW = width / mutilple;
            thumbH = height / mutilple;
            /**
             * 计算(0, 0.5]该比例值下压缩后文件的大小
             * 500应该是原作者选定的合适的值（猜测）
             * 1280和(1280 / scale)应该是原作者选定的合适的值
             */
            size = (thumbH * thumbW) / (1280 * (1280 / scale)) * 500;
            size = size < 100 ? 100 : size;
        }
        // TODO 利用所得参数进行压缩处理
        entity.setSize(size);
        entity.setThumbH(thumbH);
        entity.setThumbW(thumbW);
        entity.setAngle(angle);
        return entity;
    }

    /**
     * 获取该路径下图片的旋转角度
     *
     * @param path
     * @return
     */
    public static int getImageSpinAngle(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle  旋转的角度
     * @param bitmap 目标图片
     * @return
     */
    public static Bitmap rotatingImage(int angle, Bitmap bitmap) {
        //rotate image
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        //create a new image
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
    }

    /**
     * 像素压缩图片
     *
     * @param imagePath
     * @param width
     * @param height
     * @return
     */
    public static Bitmap compress(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int outH = options.outHeight;
        int outW = options.outWidth;
        int inSampleSize = 1;

        if (outH > height || outW > width) {
            int halfH = outH / 2;
            int halfW = outW / 2;

            while ((halfH / inSampleSize) > height && (halfW / inSampleSize) > width) {
                inSampleSize *= 2;
            }
        }

        options.inSampleSize = inSampleSize;

        options.inJustDecodeBounds = false;

        int heightRatio = (int) Math.ceil(options.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(options.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                options.inSampleSize = heightRatio;
            } else {
                options.inSampleSize = widthRatio;
            }
        }
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(imagePath, options);
    }

    /**
     * 保存图片到指定路径
     *
     * @param filePath  储存路径
     * @param bitmap    目标图片
     * @param size     期望大小
     */
    public static  File saveImage(String filePath, Bitmap bitmap, long size) {

        File result = new File(filePath.substring(0, filePath.lastIndexOf("/")));
        if (!result.exists() && !result.mkdirs()) return null;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);

        while (stream.toByteArray().length / 1024 > size && options > 6) {
            stream.reset();
            options -= 6;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);
        }
        bitmap.recycle();
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(stream.toByteArray());
            fos.flush();
            fos.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new File(filePath);
    }
}
