package com.heynchy.compress.compressinterface;

/**
 * CompressInterface
 *
 * @author lenovo  2017/3/17.
 *         Function Describe
 * @modify lenovo  2017/3/17.
 * Function Describe
 */
public interface CompressInterface {

    /**
     * 质量压缩的接口
     *
     * @param imagePath
     * @param outPath
     * @param maxSize
     * @param listener
     */
    void imageMassCompress(String imagePath, String outPath, int maxSize, CompressMassListener listener);

    /**
     * 像素压缩的接口
     *
     * @param imagePath
     * @param maxWidth
     * @param maxHeight
     * @param listener
     */
    void imagePixCompress(String imagePath,String savePath, float maxWidth, float maxHeight, CompressPixListener listener);

    /**
     * Lubrn压缩算法的接口
     *
     * @param imagePath
     * @param savePath
     * @param listener
     */
    void imageLubrnCompress(String imagePath, String savePath, CompressLubanListener listener);


}
