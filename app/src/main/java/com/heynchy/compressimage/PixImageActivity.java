package com.heynchy.compressimage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.heynchy.compress.CompressImage;
import com.heynchy.compress.compressUtil.FileSizeUtil;
import com.heynchy.compress.compressinterface.CompressPixListener;

import java.io.File;

/**
 * 图片像素（尺寸）压缩的实现
 */

public class PixImageActivity extends AppCompatActivity {

    private TextView mMassTv;         // 压缩前质量显示
    private TextView mMassPressTv;    // 压缩后质量显示
    private ImageView mImageIv;       // 原图片
    private ImageView mImagePressIv;  // 压缩后的图片
    private TextView mClickTv;        // 点击按钮

    public static void start(Context context) {
        Intent intent = new Intent(context, PixImageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass);
        initView();
        /**
         * 首先默认手机的根目录下有名为“1.jpg”的文件
         */
        final String filePath = Environment.getExternalStorageDirectory() + "/1.jpg";
        File file = new File(filePath);
        if (file.exists()) {
            mImageIv.setImageBitmap(CompressImage.getInstance().getBitmap(filePath));
            mMassTv.setText("压缩前质量：" + FileSizeUtil.getFileOrFilesSize(filePath));
        } else {
            Toast.makeText(PixImageActivity.this, "要压缩的文件不存在", Toast.LENGTH_SHORT).show();
        }
        mClickTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 进行像素压缩
                final String savePath = Environment.getExternalStorageDirectory() + "/2.1.jpg";
                /**
                 * 参数解析：
                 * filePath: 要压缩图片的绝对路径
                 * savePath: 压缩图片后的保存路径
                 * maxWidth: 期望压缩后图片的宽度（像素值）
                 * maxHeight: 期望压缩后图片的高度（像素值）
                 */
                CompressImage.getInstance().imagePixCompress(filePath, savePath, 100, 100,
                        new CompressPixListener() {
                            @Override
                            public void onCompressPixSuccessed(String imgPath, Bitmap bitmap) {
                                /**
                                 * 返回值: imgPath----压缩后图片的绝对路径
                                 *        bitmap----返回的图片
                                 */
                                mImagePressIv.setImageBitmap(bitmap);
                                mMassPressTv.setText("压缩后质量： " + FileSizeUtil.getFileOrFilesSize(imgPath));
                            }

                            @Override
                            public void onCompressPixFailed(String imgPath, String msg) {
                                /**
                                 * 返回值: imgPath----原图片的绝对路径
                                 *        msg----返回的错误信息
                                 */
                                Toast.makeText(PixImageActivity.this, "压缩失败", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void initView() {
        mMassTv = (TextView) findViewById(R.id.tv_mass);
        mMassPressTv = (TextView) findViewById(R.id.tv_press_mass);
        mImageIv = (ImageView) findViewById(R.id.iv_image);
        mImagePressIv = (ImageView) findViewById(R.id.iv_press_image);
        mClickTv = (TextView) findViewById(R.id.tv_click);
        mClickTv.setText("点击进行像素（尺寸）压缩");
    }

}
