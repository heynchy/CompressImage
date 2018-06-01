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
import com.heynchy.compress.compressinterface.CompressMassListener;

import java.io.File;

/**
 * 图片的质量压缩
 */

public class MassImageActivity extends AppCompatActivity {

    private TextView mMassTv;         // 压缩前质量显示
    private TextView mMassPressTv;    // 压缩后质量显示
    private ImageView mImageIv;       // 原图片
    private ImageView mImagePressIv;  // 压缩后的图片
    private TextView mClickTv;        // 点击按钮

    public static void start(Context context) {
        Intent intent = new Intent(context, MassImageActivity.class);
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
            Toast.makeText(MassImageActivity.this, "要压缩的文件不存在", Toast.LENGTH_SHORT).show();
        }
        mClickTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 进行质量压缩
                final String savePath = Environment.getExternalStorageDirectory() + "/1.1.jpg";
                /**
                 *   确保该方法执行在主线程中
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 参数解析：
                         * filePath: 要压缩图片的绝对路径
                         * savePath: 压缩图片后的保存路径
                         * maxSize: (例如1024)期望压缩的图片<= maxsize;单位为 KB
                         */
                        CompressImage.getInstance().imageMassCompress(filePath, savePath, 1024,
                                new CompressMassListener() {
                                    @Override
                                    public void onCompressMassSuccessed(final String imgPath) {
                                        // 返回值: imgPath----压缩后图片的绝对路径
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                /**
                                                 * 执行相关的UI操作确保在主线程中进行
                                                 */
                                                Bitmap bitmap = PermissionUtil.getLoacalBitmap(imgPath);
                                                mImagePressIv.setImageBitmap(bitmap);
                                                mMassPressTv.setText("压缩后质量： " + FileSizeUtil.getFileOrFilesSize(imgPath));
                                            }
                                        });

                                    }

                                    @Override
                                    public void onCompressMassFailed(final String imgPath, final String msg) {
                                        /**
                                         * 返回值: imgPath----原图片的绝对路径
                                         *  msg----返回的错误信息
                                         */
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                /**
                                                 * 执行相关的UI操作确保在主线程中进行
                                                 */
                                                Toast.makeText(MassImageActivity.this, msg, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
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
    }


}
