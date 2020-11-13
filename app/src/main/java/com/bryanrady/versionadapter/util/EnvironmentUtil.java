package com.bryanrady.versionadapter.util;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class EnvironmentUtil {

    public static void getDir(){
        //1.获取外部存储目录    /storage/emulated/0
        String externalStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d("wangqingbin","externalStorageDirectory == " + externalStorageDirectory);

        //获取外部存储目录的挂载状态    mounted   存储媒体已经挂载，并且挂载可读/可写
        String externalStorageState = Environment.getExternalStorageState();
        Log.d("wangqingbin","externalStorageState == " + externalStorageState);

        //获取外部存储指定目录下的挂载状态
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //传递参数的 5.0以上才支持
            String externalStorageState1 = Environment.getExternalStorageState(new File(externalStorageDirectory + "/Music"));
            Log.d("wangqingbin","externalStorageState1 == " + externalStorageState1);
        }

        //这两个待验证
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            //android 11以上支持
//            Environment.getStorageDirectory();
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Environment.getStorageState(new File(""));
        }

        //外部存储状态
//        Environment.MEDIA_REMOVED	存储媒体被移除
//        Environment.MEDIA_UNMOUNTED	存储媒体没有挂载
//        Environment.MEDIA_CHECKING	正在检查存储媒体
//        Environment.MEDIA_NOFS	存储媒体是空白或是不支持的文件系统
//        Environment.MEDIA_MOUNTED	存储媒体已经挂载，并且挂载点可读/写
//        Environment.MEDIA_MOUNTED_READ_ONLY	存储媒体已经挂载，挂载点只读
//        Environment.MEDIA_SHARED	存储媒体正在通过USB共享
//        Environment.MEDIA_BAD_REMOVAL	在没有挂载前存储媒体已经被移除
//        Environment.MEDIA_UNMOUNTABLE	存储媒体无法挂载

        //获取data的目录 /data
        String dataDirectory = Environment.getDataDirectory().getAbsolutePath();
        Log.d("wangqingbin","dataDirectory == " + dataDirectory);

        //获取下载缓存目录  /data/cache
        String downloadCacheDirectory = Environment.getDownloadCacheDirectory().getAbsolutePath();
        Log.d("wangqingbin","downloadCacheDirectory == " + downloadCacheDirectory);

        //获得系统主目录   /system
        String rootDirectory = Environment.getRootDirectory().getAbsolutePath();
        Log.d("wangqingbin","rootDirectory == " + rootDirectory);

        //获取外部存储共享目录    getExternalStoragePublicDirectory
        String externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        Log.d("wangqingbin","externalStoragePublicDirectory == " + externalStoragePublicDirectory);
        //  /storage/emulated/0/Music


        //type类型
//        Environment.DIRECTORY_ALARMS	        系统提醒铃声
//        Environment.DIRECTORY_AUDIOBOOKS      有声读物
//        Environment.DIRECTORY_DCIM	        相机拍摄照片和视频
//        Environment.DIRECTORY_DOCUMENTS       文档存放
//        Environment.DIRECTORY_DOWNLOADS	    下载存放
//        Environment.DIRECTORY_MOVIES	        电影存放
//        Environment.DIRECTORY_MUSIC	        音乐存放
//        Environment.DIRECTORY_NOTIFICATIONS	系统通知铃声
//        Environment.DIRECTORY_PICTURES	    图片存放
//        Environment.DIRECTORY_PODCASTS        系统广播
//        Environment.DIRECTORY_SCREENSHOTS     截图存放
//        Environment.DIRECTORY_RINGTONES	    系统铃声
    }

}
