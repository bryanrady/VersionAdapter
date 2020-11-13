package com.bryanrady.versionadapter.v10;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bryanrady.versionadapter.BuildConfig;
import com.bryanrady.versionadapter.MainActivity;
import com.bryanrady.versionadapter.R;
import com.bryanrady.versionadapter.util.BitmapUtil;
import com.bryanrady.versionadapter.util.UriToPathUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CompatV10Activity extends AppCompatActivity {

    private Button btn_camera;
    private Button btn_album;
    private ImageView iv;
    private Button btn_get_picture_from_album;
    private Button btn_add_picture_to_album;
    private Button btn_download_file;
    private Button btn_pick_file;

    private static final int REQUEST_OPEN_CAMERA = 101;
    private static final int REQUEST_OPEN_ALBUM = 102;
    private static final int REQUEST_PICK_FILE = 103;

    private Uri mImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compat_v10);

        btn_camera = findViewById(R.id.btn_camera);
        btn_album = findViewById(R.id.btn_album);
        iv = findViewById(R.id.iv);
        btn_get_picture_from_album = findViewById(R.id.btn_get_picture_from_album);
        btn_add_picture_to_album = findViewById(R.id.btn_add_picture_to_album);
        btn_download_file = findViewById(R.id.btn_download_file);
        btn_pick_file = findViewById(R.id.btn_pick_file);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
            }
        });
        btn_get_picture_from_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompatV10Activity.this, AlbumRecyclerActivity.class);
                startActivity(intent);
            }
        });
        btn_add_picture_to_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_xcross_bg);
                String displayName = "ic_xcross_bg.png";
                String mimeType = "image/png";
                addBitmapToAlbum(bitmap, displayName, mimeType, Bitmap.CompressFormat.PNG);
            }
        });
        btn_download_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://seugs.seu.edu.cn/_upload/article/files/93/8a/0294630d4fc9969bef643dfffaee/d2f3e96c-6e6e-4e59-a596-cee870ac3151.doc";
                downloadFile(url);
            }
        });
        btn_pick_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFile();
            }
        });

    }

    //targetSdk    29   30

//    imagePath = Environment.getExternalStorageDirectory()
//    imagePath = getExternalFilesDir(null)
//
//    8.0 手机	相机  正常显示
//    mFilePath==/storage/emulated/0/compat_v10/temp/1605081839545.jpg
//    imageUri==content://com.bryanrady.versionadapter.provider/external/compat_v10/temp/1605081839545.jpg
//    8.0 手机	相册  正常显示
//    filePath==/storage/emulated/0/Pictures/Screenshot/Screenshot_20200701-183653.png

//    10.0模拟器	相机  正常显示
//    mFilePath==/storage/emulated/0/Android/data/com.bryanrady.versionadapter/files/compat_v10/temp/1605160927681.jpg
//    imageUri==content://com.bryanrady.versionadapter.provider/external_files/compat_v10/temp/1605160927681.jpg
//
//    10.0模拟器	相册  无法显示    相册中的图片无法返回，无法根据这个外部存储路径访问到图片
//    filePath==/storage/emulated/0/DCIM/Camera/IMG_20201111_074926.jpg
//    Unable to decode stream: java.io.FileNotFoundException: /storage/emulated/0/DCIM/Camera/IMG_20201111_074926.jpg: open failed: EACCES (Permission denied)
//    10.0无法访问应用外部存储目录之外的文件，所以无法通过直接访问 这个路径 得到图片
//
//    11.0模拟器 相机  无法显示
//    mFilePath==/storage/emulated/0/Android/data/com.bryanrady.versionadapter/files/compat_v10/temp/1605160522619.jpg
//    imageUri==content://com.bryanrady.versionadapter.provider/external_files/compat_v10/temp/1605160522619.jpg
//
//    11.0模拟器 相册  相册里面没有图片


    private void openCamera(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }

        String imagePath;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//            //10.0之后，采用分区存储方式，其实分区存储方式适配所有版本，所以可以把在外置卡目录下创建文件存储的方式废弃
//            imagePath = getExternalFilesDir(null) + "/compat_v10/temp/" + System.currentTimeMillis() + ".jpg";
//        } else{
//            imagePath = Environment.getExternalStorageDirectory() + "/compat_v10/temp/" + System.currentTimeMillis() + ".jpg";
//        }
        imagePath = getExternalFilesDir("picture") + "/" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(imagePath);
        if (!imageFile.getParentFile().exists()){
            imageFile.getParentFile().mkdirs();
        }

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
            imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", imageFile);
            captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }else{
            imageUri = Uri.fromFile(imageFile);
        }

        mImageUri = imageUri;
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(captureIntent, REQUEST_OPEN_CAMERA);

    }

    private void openAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_OPEN_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_OPEN_CAMERA:
                    //data为null 表示是相机返回
                    Log.d("wangqingbin","mImageUri=="+mImageUri);
                    //    iv.setImageURI(mImageUri);

                    Bitmap bitmap = imageUriToBitmap(mImageUri);
                    iv.setImageBitmap(bitmap);
                    break;
                case REQUEST_OPEN_ALBUM:
                    if (data != null && data.getData() != null){
                        Uri imageUri = data.getData();
                        Log.d("wangqingbin","imageUri=="+imageUri);

                        //1.直接通过加载uri显示图片
                        iv.setImageURI(imageUri);

                        //2.将uri转换成path,在转换成bitmap进行加载，这需要注意path是不是在应用的专属外部存储目录中
//                        String filePath = UriToPathUtil.getImageAbsolutePath(this, imageUri);
//                        Log.d("wangqingbin","filePath=="+filePath);
//                        bitmap = BitmapUtil.decodeSampledBitmapFromFilePath(filePath, 200, 200);
//                        iv.setImageBitmap(bitmap);

                        //3.将uri转换成bitmap再进行加载
//                        Bitmap bitmap = imageUriToBitmap(imageUri);
//                        iv.setImageBitmap(bitmap);
                    }
                    break;
                case REQUEST_PICK_FILE:
                    if (data != null && data.getData() != null){
                        Uri fileUri = data.getData();
                        String fileName = getFileNameByUri(fileUri);
                        copyFileToExternalFilesDir(fileUri, fileName);
                    }
                    break;
            }
        }
    }

    /**
     * 根据uri得到文件名称
     * @param uri
     * @return
     */
    private String getFileNameByUri(Uri uri){
        String fileName = String.valueOf(System.currentTimeMillis());
        if (uri != null){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null){
                cursor.moveToFirst();
                fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));
                cursor.close();
            }
        }
        return fileName;
    }

    /**
     * 将文件复制到应用的专属外部存储目录 /files/file
     * @param uri
     * @param fileName
     */
    private void copyFileToExternalFilesDir(Uri uri, String fileName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    if (is != null){
                        File file = new File(getExternalFilesDir("other"), fileName);
                        FileOutputStream fos = new FileOutputStream(file);
                        bis = new BufferedInputStream(is);
                        bos = new BufferedOutputStream(fos);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bis.read(buffer)) != -1){
                            bos.write(buffer, 0, len);
                            bos.flush();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CompatV10Activity.this, "Copy file into external files succeeded.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bos != null){
                        try {
                            bos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (bis != null){
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }


    /**
     * 将一张Bitmap添加到相册中
     * @param bitmap        图片显示名称
     * @param displayName   图片mime类型
     * @param mimeType
     * @param compressFormat
     */
    private void addBitmapToAlbum(Bitmap bitmap, String displayName, String mimeType, Bitmap.CompressFormat compressFormat){
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        //图片存储路径
//        Android 10中新增了一个RELATIVE_PATH常量，表示文件存储的相对路径，可选值有DIRECTORY_DCIM、DIRECTORY_PICTURES、
//        DIRECTORY_MOVIES、DIRECTORY_MUSIC等，分别表示相册、图片、电影、音乐等目录。而在之前的系统版本中并没有RELATIVE_PATH，
//        所以我们要使用DATA常量（已在Android 10中废弃），并拼装出一个文件存储的绝对路径才行。
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
        }else{
            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + displayName;
            values.put(MediaStore.MediaColumns.DATA, filePath);
        }
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            if (uri != null){
                OutputStream os = getContentResolver().openOutputStream(uri);
                if (os != null){
                    //将Bitmap转换为输出流
                    bitmap.compress(compressFormat, 100, os);
                    os.close();
                    bitmap.recycle();
                    Toast.makeText(this, "Add bitmap to album succeeded.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将图片文件添加到相册中（公共文件夹）
     * @param imageFile     图片文件
     * @param displayName   图片mime类型
     * @param mimeType
     * @param compressFormat
     */
    private void addBitmapToAlbum(File imageFile, String displayName, String mimeType, Bitmap.CompressFormat compressFormat){
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        //图片存储路径
//        Android 10中新增了一个RELATIVE_PATH常量，表示文件存储的相对路径，可选值有DIRECTORY_DCIM、DIRECTORY_PICTURES、
//        DIRECTORY_MOVIES、DIRECTORY_MUSIC等，分别表示相册、图片、电影、音乐等目录。而在之前的系统版本中并没有RELATIVE_PATH，
//        所以我们要使用DATA常量（已在Android 10中废弃），并拼装出一个文件存储的绝对路径才行。
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
        }else{
            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + displayName;
            values.put(MediaStore.MediaColumns.DATA, filePath);
        }
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            if (uri != null){
                FileInputStream fis = new FileInputStream(imageFile);
                OutputStream os = getContentResolver().openOutputStream(uri);

                bis = new BufferedInputStream(fis);
                bos = new BufferedOutputStream(os);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = bis.read(buffer)) != -1){
                    bos.write(buffer, 0, len);
                    bos.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将图片对应的流写到相册中
     * @param is
     * @param displayName
     * @param mimeType
     */
    private void writeInputStreamToAlbum(InputStream is, String displayName, String mimeType){
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
        }else{
            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + displayName;
            values.put(MediaStore.MediaColumns.DATA, filePath);
        }
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            if (uri != null){
                OutputStream os = getContentResolver().openOutputStream(uri);
                if (is != null && os!= null){
                    bis = new BufferedInputStream(is);
                    bos = new BufferedOutputStream(os);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        bos.write(buffer, 0, len);
                        bos.flush();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 将uri转换成bitmap
     * @param uri
     * @return
     */
    private Bitmap imageUriToBitmap(Uri uri){
        ParcelFileDescriptor pfd = null;
        try {
            Bitmap bitmap = null;
            pfd = getContentResolver().openFileDescriptor(uri, "r");
            if (pfd != null){
                //1.转换uri为bitmap类型
                FileDescriptor fd = pfd.getFileDescriptor();
                bitmap = BitmapFactory.decodeFileDescriptor(fd);

                //2.旋转图片    拍照才旋转
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
                    ExifInterface exifInterface = new ExifInterface(fd);
                    bitmap = rotateBitmap(exifInterface, bitmap);
                }
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                pfd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Bitmap rotateBitmap(ExifInterface exifInterface, Bitmap originalBitmap){
        int degree;
        if(exifInterface != null){
            degree = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (degree){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
            Log.d("wangqingbin", degree + "");
            if(degree != 0){
                Matrix matrix = new Matrix();
                matrix.postRotate(degree);
                Bitmap rotateBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix,true);
                return rotateBitmap;
            }
        }
        return originalBitmap;
    }

    /**
     * 下载文件到Download目录
     * @param downloadUrl
     */
    private void downloadFile(String downloadUrl){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            downloadFileAndroidQ(downloadUrl);
        }else{
            downloadFileAndroidQFollowing(downloadUrl);
        }
    }

    /**
     * Android 10以下文件下载 下载pdf、doc文件，或者下载APK安装包等等
     * @param downloadUrl
     */
    private void downloadFileAndroidQFollowing(String downloadUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    URL url = new URL(downloadUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        if (is != null) {
                            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                            FileOutputStream fos = new FileOutputStream(file);

                            bis = new BufferedInputStream(is);
                            bos = new BufferedOutputStream(fos);
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = bis.read(buffer)) != -1){
                                bos.write(buffer, 0, len);
                                bos.flush();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bos != null){
                        try {
                            bos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (bis != null){
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 代码只能在Android 10或更高的系统版本上运行，因为MediaStore.Downloads是Android 10中新增的API。
     * 至于Android 9及以下的系统版本，仍然使用之前的代码来进行文件下载。
     * @param downloadUrl
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void downloadFileAndroidQ(String downloadUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    URL url = new URL(downloadUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    if (connection.getResponseCode() == 200){
                        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));

                        ContentValues values = new ContentValues();
                        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
                        Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);

                        InputStream is = connection.getInputStream();
                        OutputStream os = getContentResolver().openOutputStream(uri);
                        if (is != null && os != null){
                            bis = new BufferedInputStream(is);
                            bos = new BufferedOutputStream(os);
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = bis.read(buffer)) != -1){
                                bos.write(buffer, 0, len);
                                bos .flush();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CompatV10Activity.this, "Android Q download file succeeded.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bos != null){
                        try {
                            bos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (bis != null){
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }

    /**
     * 打开文件选择器
     * 如果我们要读取SD卡上非图片、音频、视频类的文件，比如说打开一个PDF文件，这个时候就不能再使用MediaStore API了，而是要使用文件选择器.
     * 但是，我们不能再像之前的写法那样，自己写一个文件浏览器，然后从中选取文件，而是必须要使用手机系统中内置的文件选择器
     */
    private void pickFile(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, REQUEST_PICK_FILE);
        }
    }

}
