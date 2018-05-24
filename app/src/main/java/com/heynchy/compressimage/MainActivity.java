package com.heynchy.compressimage;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.heynchy.compress.CompressImage;
import com.heynchy.compress.compressinterface.CompressLubanListener;
import com.heynchy.compress.compressinterface.CompressMassListener;
import com.heynchy.compress.compressinterface.CompressPixListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String filePath = Environment.getExternalStorageDirectory() + "/yueyeya/1.jpg";
        final String filePath2 = Environment.getExternalStorageDirectory() + "/yueyeya/2.jpg";
        final String filePath3 = Environment.getExternalStorageDirectory() + "/yueyeya/3.jpg";
        final String filePath4 = Environment.getExternalStorageDirectory() + "/yueyeya/4.jpg";
        final String filePath5 = Environment.getExternalStorageDirectory() + "/yueyeya/5.jpg";
        final String filePath6 = Environment.getExternalStorageDirectory() + "/yueyeya/6.jpg";

        CompressImage.getInstance().imageLubrnCompress(filePath, filePath4, new CompressLubanListener() {
            @Override
            public void onCompressLubanFailed(String imgPath, String msg) {
                Log.i("heyn", "onCompressLubanFailed");
            }

            @Override
            public void onCompressLubanSuccessed(String imgPath, Bitmap bitmap) {
                Log.i("heyn", "onCompressLubanSuccessed");
            }
        });
        CompressImage.getInstance().imageMassCompress(filePath2, filePath5, 1024, new CompressMassListener() {
            @Override
            public void onCompressMassSuccessed(String imgPath) {
                Log.i("heyn", "onCompressMassSuccessed");
            }

            @Override
            public void onCompressMassFailed(String imgPath, String msg) {
                Log.i("heyn", "onCompressMassFailed");
            }
        });
        CompressImage.getInstance().imagePixCompress(filePath3, 500, 500, new CompressPixListener() {
            @Override
            public void onCompressPixSuccessed(String imgPath, Bitmap bitmap) {
                Log.i("heyn", "onCompressPixSuccessed");
            }

            @Override
            public void onCompressPixFailed(String imgPath, String msg) {
                Log.i("heyn", "onCompressPixFailed");
            }
        });
    }
}
