package com.heynchy.compress.compressinterface;

import android.graphics.Bitmap;

/**
 * @author CHY
 *         Create at 2018/5/24 15:21.
 */

public interface CompressLubanListener {
    /**
     * 压缩失败
     *
     * @param imgPath 压缩失败的图片
     * @param msg     失败的原因
     */
    void onCompressLubanFailed(String imgPath, String msg);

    /**
     * luban压缩成功
     *
     * @param imgPath 压缩图片的路径
     */
    void onCompressLubanSuccessed(String imgPath, Bitmap bitmap);
}
