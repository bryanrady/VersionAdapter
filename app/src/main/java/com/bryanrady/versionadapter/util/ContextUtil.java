package com.bryanrady.versionadapter.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class ContextUtil {

//      dirPath ==      /data/user/0/com.bryanrady.versionadapter/app_adapter
//      cacheDir ==     /data/user/0/com.bryanrady.versionadapter/cache
//      filesDir ==     /data/user/0/com.bryanrady.versionadapter/files
//      dataDir ==      /data/user/0/com.bryanrady.versionadapter
//      codeCacheDir == /data/user/0/com.bryanrady.versionadapter/code_cache

//      obbDir ==               /storage/emulated/0/Android/obb/com.bryanrady.versionadapter
//      externalCacheDir ==     /storage/emulated/0/Android/data/com.bryanrady.versionadapter/cache
//      externalFilesDir ==     /storage/emulated/0/Android/data/com.bryanrady.versionadapter/files
//      externalFilesDir2 ==    /storage/emulated/0/Android/data/com.bryanrady.versionadapter/files/Pictures

    public static void getDir(Context context){
        //2.通过Context获取的

        //  /data/user/0/com.bryanrady.versionadapter/app_adapter
        String dirPath = context.getDir("adapter", Context.MODE_PRIVATE).getAbsolutePath();
        Log.d("wangqingbin","dirPath == " + dirPath);

        //获取应用程序自己的缓存目录     /data/user/0/com.bryanrady.versionadapter/cache
        String cacheDir = context.getCacheDir().getAbsolutePath();
        Log.d("wangqingbin","cacheDir == " + cacheDir);

        //  /data/user/0/com.bryanrady.versionadapter/files
        String filesDir = context.getFilesDir().getAbsolutePath();
        Log.d("wangqingbin","filesDir == " + filesDir);

        //7.0以上 getDataDir
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String dataDir = context.getDataDir().getAbsolutePath();
            Log.d("wangqingbin","dataDir == " + dataDir);
            //  /data/user/0/com.bryanrady.versionadapter
        }

        //5.0以上 getCodeCacheDir
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String codeCacheDir = context.getCodeCacheDir().getAbsolutePath();
            Log.d("wangqingbin","codeCacheDir == " + codeCacheDir);
            //  /data/user/0/com.bryanrady.versionadapter/code_cache
        }

//************************************************************************************

        //通过 context 获取应用程序外部存储的东西

        String obbDir = context.getObbDir().getAbsolutePath();
        Log.d("wangqingbin","obbDir == " + obbDir);
        //  /storage/emulated/0/Android/obb/com.bryanrady.versionadapter

        //获取应用程序在外部存储的存储目录
        String externalCacheDir = context.getExternalCacheDir().getAbsolutePath();
        Log.d("wangqingbin","externalCacheDir == " + externalCacheDir);
        //  /storage/emulated/0/Android/data/com.bryanrady.versionadapter/cache

        File externalFilesDir = context.getExternalFilesDir(null);
        Log.d("wangqingbin","externalFilesDir == " + externalFilesDir);
        //  /storage/emulated/0/Android/data/com.bryanrady.versionadapter/files

        File externalFilesDir2 = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.d("wangqingbin","externalFilesDir2 == " + externalFilesDir2);
        //  /storage/emulated/0/Android/data/com.bryanrady.versionadapter/files/Pictures

        //10.0模拟器
//        2020-11-12 11:04:38.054 12718-12718/com.bryanrady.versionadapter D/wangqingbin: dirPath == /data/user/0/com.bryanrady.versionadapter/app_adapter
//        2020-11-12 11:04:38.054 12718-12718/com.bryanrady.versionadapter D/wangqingbin: cacheDir == /data/user/0/com.bryanrady.versionadapter/cache
//        2020-11-12 11:04:38.054 12718-12718/com.bryanrady.versionadapter D/wangqingbin: filesDir == /data/user/0/com.bryanrady.versionadapter/files
//        2020-11-12 11:04:38.054 12718-12718/com.bryanrady.versionadapter D/wangqingbin: dataDir == /data/user/0/com.bryanrady.versionadapter
//        2020-11-12 11:04:38.054 12718-12718/com.bryanrady.versionadapter D/wangqingbin: codeCacheDir == /data/user/0/com.bryanrady.versionadapter/code_cache

//        2020-11-12 11:04:38.057 12718-12718/com.bryanrady.versionadapter D/wangqingbin: obbDir == /storage/emulated/0/Android/obb/com.bryanrady.versionadapter
//        2020-11-12 11:04:38.062 12718-12718/com.bryanrady.versionadapter D/wangqingbin: externalCacheDir == /storage/emulated/0/Android/data/com.bryanrady.versionadapter/cache
//        2020-11-12 11:04:38.063 12718-12718/com.bryanrady.versionadapter D/wangqingbin: externalFilesDir == /storage/emulated/0/Android/data/com.bryanrady.versionadapter/files
//        2020-11-12 11:04:38.065 12718-12718/com.bryanrady.versionadapter D/wangqingbin: externalFilesDir2 == /storage/emulated/0/Android/data/com.bryanrady.versionadapter/files/Pictures

        //11.0模拟器
//        2020-11-12 11:08:19.416 25801-25801/com.bryanrady.versionadapter D/wangqingbin: dirPath == /data/user/0/com.bryanrady.versionadapter/app_adapter
//        2020-11-12 11:08:19.417 25801-25801/com.bryanrady.versionadapter D/wangqingbin: cacheDir == /data/user/0/com.bryanrady.versionadapter/cache
//        2020-11-12 11:08:19.417 25801-25801/com.bryanrady.versionadapter D/wangqingbin: filesDir == /data/user/0/com.bryanrady.versionadapter/files
//        2020-11-12 11:08:19.417 25801-25801/com.bryanrady.versionadapter D/wangqingbin: dataDir == /data/user/0/com.bryanrady.versionadapter
//        2020-11-12 11:08:19.417 25801-25801/com.bryanrady.versionadapter D/wangqingbin: codeCacheDir == /data/user/0/com.bryanrady.versionadapter/code_cache

//        2020-11-12 11:08:19.419 25801-25801/com.bryanrady.versionadapter D/wangqingbin: obbDir == /storage/emulated/0/Android/obb/com.bryanrady.versionadapter
//        2020-11-12 11:08:19.425 25801-25801/com.bryanrady.versionadapter W/ContextImpl: Failed to ensure /storage/emulated/0/Android/data/com.bryanrady.versionadapter/cache: android.os.ServiceSpecificException:  (code -1)

        //11.0 的模拟器 getExternalCache()返回 null，通过这种方式不能创建出cache目录，真机没问题

    }

}
