package com.heynchy.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.heynchy.compress.compressUtill.FileSizeUtil;
import com.heynchy.compress.compressUtill.LubanUtil;
import com.heynchy.compress.compressinterface.CompressInterface;
import com.heynchy.compress.compressinterface.CompressLubanListener;
import com.heynchy.compress.compressinterface.CompressMassListener;
import com.heynchy.compress.compressinterface.CompressPixListener;
import com.heynchy.compress.entity.LubanEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * CompressImageUtil
 *
 * @author CHY  2017/3/17.
 *         图片压缩工具类
 */
public class CompressImage implements CompressInterface {

    public static CompressImage compressImageUtil;
    /**
     * 获取实例
     *
     * @return
     */
    public static CompressImage getInstance() {
        if (compressImageUtil == null) {
            synchronized (CompressImage.class) {
                if (compressImageUtil == null) {
                    compressImageUtil = new CompressImage();
                }
            }
        }
        return compressImageUtil;
    }
    /**
     * 质量压缩实现
     *
     * @param imagePath
     * @param outPath
     * @param maxSize
     * @param listener
     */
    @Override
    public void imageMassCompress(String imagePath, String outPath, int maxSize,
                                  CompressMassListener listener) {
        File file = new File(imagePath);
        if (file == null || !file.exists() || !file.isFile()) {
            //如果文件不存在，则不做任何处理
            listener.onCompressMassFailed(imagePath, "要压缩的文件不存在");
            return;
        }
        try {
            long size = FileSizeUtil.getFileSize(file);
            if (size / 1024f <= maxSize) {
                return;
            }
        } catch (Exception e) {
            Log.i("Exception", "文件异常！");
        }
        compressImageByQuality(imagePath, outPath, maxSize, listener);
    }

    /**
     * 像素压缩实现
     *
     * @param imagePath
     * @param maxWidth
     * @param maxHeight
     * @param listener
     */
    @Override
    public void imagePixCompress(String imagePath, float maxWidth, float maxHeight, CompressPixListener listener) {
        File file = new File(imagePath);
        if (file == null || !file.exists() || !file.isFile()) {
            //如果文件不存在，则不做任何处理
            listener.onCompressPixFailed(imagePath, "要压缩的文件不存在");
            return;
        }
        imagePixelCompress(imagePath, maxWidth, maxHeight, listener);
    }

    /**
     * Lubrn 算法的实现
     *
     * @param imagePath
     * @param savePath
     * @param listener
     */
    @Override
    public void imageLubrnCompress(String imagePath, String savePath, CompressLubanListener listener) {
        LubanEntity entity = LubanUtil.getLubrnValue(imagePath);
        if (entity != null) {
            Bitmap thbBitmap = LubanUtil.compress(imagePath, entity.getThumbW(), entity.getThumbH());
            if (thbBitmap != null) {
                thbBitmap = LubanUtil.rotatingImage(entity.getAngle(), thbBitmap);
                LubanUtil.saveImage(savePath, thbBitmap, (long) entity.getSize());
                listener.onCompressLubanSuccessed(savePath, thbBitmap);
            } else {
                listener.onCompressLubanFailed(imagePath, "压缩失败");
            }
        } else {
            listener.onCompressLubanFailed(imagePath, "压缩失败");
        }
    }

    /**
     * 多线程压缩图片的质量
     */
    private void compressImageByQuality(final String filePath, final String outPath, final int maxSize,
                                        final CompressMassListener listener) {
        final Bitmap bitmap = getBitmap(filePath);
        if (bitmap == null) {
            listener.onCompressMassFailed(filePath, "要压缩的文件不存在!");
            return;
        }
        new Thread(new Runnable() {//开启多线程进行压缩处理
            @Override
            public void run() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int options = 100;
                //质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);

                //循环判断如果压缩后图片是否大于指定大小maxSize(单位KB),大于继续压缩
                while (baos.toByteArray().length / 1024 > 1024) {
                    baos.reset();//重置baos即让下一次的写入覆盖之前的内容
                    options -= 10;//图片质量每次减少10
                    if (options < 0) options = 0;//如果图片质量小于0，则将图片的质量压缩到最小值
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//将压缩后的图片保存到baos中
                    if (options == 0) break;//如果图片的质量已降到最低则，不再进行压缩
                }
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();//回收内存中的图片
                }
                try {
                    FileOutputStream fos = new FileOutputStream(new File(outPath));//将压缩后的图片保存的本地上指定路径中
                    fos.write(baos.toByteArray());
                    fos.flush();
                    fos.close();
                    listener.onCompressMassSuccessed(outPath);
                } catch (Exception e) {
                    listener.onCompressMassFailed(filePath, "质量压缩失败!");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 像素压缩
     *
     * @param filePath
     */
    private void imagePixelCompress(final String filePath, float maxWidth, float maxHeight,
                                    final CompressPixListener listener) {
        final Bitmap bitmap = getBitmap(filePath);
        if (bitmap == null) {
            listener.onCompressPixFailed(filePath, "要压缩的文件不存在!");
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false;
        int width = options.outWidth;
        int height = options.outHeight;
        int samplingRate = 1;
        if (width > height && width > maxWidth) {
            // 如果图片宽大于高并且大于目标宽度，则以宽度标准压缩
            samplingRate = (int) (width / maxWidth);
        } else if (height > width && height > maxHeight) {
            // 如果图片高大于宽并且大于目标高度，则以高度标准压缩
            samplingRate = (int) (height / maxHeight);
        }
        options.inSampleSize = samplingRate;     //设置采样率
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        options.inPurgeable = true;                         // 同时设置才会有效
        options.inInputShareable = true;                    //当系统内存不够时候图片自动被回收
        Bitmap bitmap1 = BitmapFactory.decodeFile(filePath, options);
        if (bitmap1 != null) {
            saveBitmap(filePath, bitmap1);
            listener.onCompressPixSuccessed(filePath, bitmap);
        } else {
            listener.onCompressPixFailed(filePath, "压缩失败");
        }
    }

    /**
     * 根据本地路径获取本地图片
     *
     * @param imgPath
     * @return
     */
    public Bitmap getBitmap(String imgPath) {
        try {
            FileInputStream fos = new FileInputStream(imgPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // Do not compress
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }


    /**
     * 保存bitmap到本地的指定路径下
     *
     * @param savePath
     * @param bitmap
     */
    public void saveBitmap(String savePath, Bitmap bitmap) {
        File f = new File(savePath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
