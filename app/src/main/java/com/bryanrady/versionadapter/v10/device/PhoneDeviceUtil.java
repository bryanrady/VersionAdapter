package com.bryanrady.versionadapter.v10.device;

import android.content.Context;
import android.os.Build;

public class PhoneDeviceUtil {

    public static String getDeviceId(Context context){
        String serial = Build.SERIAL;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String serial1 = Build.getSerial();
        }
        return null;
    }

}
