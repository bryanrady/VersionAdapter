package com.bryanrady.versionadapter.v4_4;

import android.os.Environment;

public class CompatV4_4 {

    //  KitKat      19

    /**
     * 1.如果您的应用从外部存储空间读取
     * 您的应用在 Android 4.4 上运行时无法读取外部存储空间上的共享文件，除非您的应用具有 READ_EXTERNAL_STORAGE 权限。
     * 也就是说，没有此权限，您无法再访问 getExternalStoragePublicDirectory() 返回的目录中的文件。
     * 但是，如果您仅需要访问 getExternalFilesDir() 提供的您的应用特有目录，那么，您不需要 READ_EXTERNAL_STORAGE 权限。
     */
    public void testExternalStoragePublicDirectory(){
        Environment.getExternalStoragePublicDirectory(null);
    }

}
