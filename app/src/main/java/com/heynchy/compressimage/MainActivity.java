package com.heynchy.compressimage;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mMassTv;
    private TextView mPixTv;
    private TextView mLubanTv;
    private boolean mHasPermission;  // 是否有内存卡读取权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        /*******************************************/
        // 由于Android 6.0之后存储权限需要主动获取，所以在使用该压缩工具之前，要保证你的APP已获取存储权限
        mHasPermission = PermissionUtil.hasStoragePermission(this);
        if (mHasPermission) {
            Toast.makeText(this, "已获得内存卡权限", Toast.LENGTH_SHORT).show();
        } else {
            /**
             * 主要针对 Android 6.0之后需主动获取权限
             */
            PermissionUtil.getStoragePermissions(this);
        }
        /*********************************************/
        mMassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 跳转至图片的质量压缩界面
                if (mHasPermission) {
                    MassImageActivity.start(MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, "没有内存卡权限", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mPixTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 跳转至图片的像素压缩界面
                if (mHasPermission) {
                    PixImageActivity.start(MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, "没有内存卡权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mLubanTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 跳转至图片的Luban算法的压缩界面
                if (mHasPermission) {
                    LubanImageActivity.start(MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, "没有内存卡权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        mMassTv = (TextView) findViewById(R.id.tv_mass);
        mLubanTv = (TextView) findViewById(R.id.tv_luban);
        mPixTv = (TextView) findViewById(R.id.tv_pix);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (1 == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "获取权限成功", Toast.LENGTH_SHORT).show();
            } else {
                
                Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
