package com.heynchy.compress.compressinterface;

import android.graphics.Bitmap;

/**
 * @author CHY
 *         Create at 2018/5/24 15:21.
 */

public interface CompressPixListener {
    /**
     * 像素压缩成功
     *
     * @param imgPath 压缩图片的路径
     */
    void onCompressPixSuccessed(String imgPath, Bitmap bitmap);
    /**
     * 质量压缩失败
     *
     * @param imgPath 压缩失败的图片
     * @param msg     失败的原因
     */
    void onCompressPixFailed(String imgPath, String msg);
}
