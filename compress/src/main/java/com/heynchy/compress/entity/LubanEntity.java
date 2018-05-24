package com.heynchy.compress.entity;

/**
 * @author CHY
 *         Create at 2018/5/24 14:57.
 */

public class LubanEntity {
    private int thumbW;  //宽度的压缩参数
    private int thumbH;  //高度的压缩参数
    private double size; //压缩后的文件大小
    private int angle;   // 图片旋转的角度

    public LubanEntity() {
    }


    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public int getThumbH() {
        return thumbH;
    }

    public void setThumbH(int thumbH) {
        this.thumbH = thumbH;
    }

    public int getThumbW() {
        return thumbW;
    }

    public void setThumbW(int thumbW) {
        this.thumbW = thumbW;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}
