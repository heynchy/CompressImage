package com.heynchy.compress.compressinterface;

/**
 * @author CHY
 *         Create at 2018/5/24 15:21.
 */

public interface CompressMassListener {
    /**
     * 质量压缩成功
     *
     * @param imgPath 压缩图片的路径
     */
    void onCompressMassSuccessed(String imgPath);

    /**
     * 质量压缩失败
     *
     * @param imgPath 压缩失败的图片
     * @param msg     失败的原因
     */
    void onCompressMassFailed(String imgPath, String msg);
}
