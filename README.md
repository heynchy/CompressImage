# CompressImage
## 集成图片的质量压缩，像素压缩以及基于Luban算法的图片压缩
#### 质量压缩：从质量层面进行相关压缩（原图片可通过该工具压缩至小于期望大小的图片）
#### 像素压缩：设定固定的长和宽，然后进行压缩
#### 基于Luban算法的压缩： 类比微信将图片压缩至不失真的图片大小
Demo:
----
### 1. 像素压缩：从质量层面进行相关压缩

<p>
   <img src="https://github.com/heynchy/CompressImage/blob/master/ScreenShot/1.gif" width="350" alt="Screenshot"/>
</p>

### 2. 质量压缩： 设定固定的长和宽，然后进行压缩

<p>
   <img src="https://github.com/heynchy/CompressImage/blob/master/ScreenShot/2.gif" width="350" alt="Screenshot"/>
</p>

### 3. 基于Luban算法的压缩：类比微信将图片压缩至不失真的图片大小

<p>
   <img src="https://github.com/heynchy/CompressImage/blob/master/ScreenShot/3.gif" width="350" alt="Screenshot"/>
</p> 

## Usage
###  Add dependency
#### Step 1
 在你的项目的根目录的build.gradle中添加如下信息：
```groovy
  	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
#### Step 2 
然后在添加相关的依赖关系（不是根目录）
```groovy
   dependencies {
       implementation 'com.github.heynchy:CompressImage:v0.1.2'
   }
```
## 代码中的设置
    注意：对相关图片进行压缩时要确保手机的存储权限已经获得
### 1. 质量压缩 (注意使用时要确保运行在主线程中，包括处理返回信息时)
```java
   // TODO 进行质量压缩
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
```
### 2. 像素（尺寸）压缩
```java
  // TODO 进行像素压缩
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
```
### 3.基于Luban算法的图片压缩（可得到体积小，图片不失真的图片，类比于微信）
```java
   // TODO 进行Luban算法压缩
   /**
    * 参数解析：
    * filePath: 要压缩图片的绝对路径
    * savePath: 压缩图片后的保存路径
    */
    CompressImage.getInstance().imageLubrnCompress(filePath, savePath, new CompressLubanListener() {
        @Override
        public void onCompressLubanSuccessed(String imgPath, Bitmap bitmap) {
            /**
             * 返回值: imgPath----压缩后图片的绝对路径
             *        bitmap----返回的图片
             */
             mImagePressIv.setImageBitmap(bitmap);
             mMassPressTv.setText("压缩后质量： " + FileSizeUtil.getFileOrFilesSize(imgPath));
        }

        @Override
        public void onCompressLubanFailed(String imgPath, String msg) {
            /**
             * 返回值: imgPath----原图片的绝对路径
             *        msg----返回的错误信息
             */
             Toast.makeText(LubanImageActivity.this, msg, Toast.LENGTH_LONG).show();
        }

   });
```
感谢以下相关项目：
https://github.com/Curzibn/Luban

License
------
    Copyright 2017 heynchy
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

