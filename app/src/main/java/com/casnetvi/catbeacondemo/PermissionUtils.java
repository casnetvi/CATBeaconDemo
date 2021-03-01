package com.casnetvi.catbeacondemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class PermissionUtils {
    public static final int P_CODE_BLUE_TOOTH = 993;
    public static final String[] PERMISSIONS_BLUE_TOOTH = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    /**
     * 检测是否已获得所需权限
     *
     * @param permissions
     * @return
     */
    public static boolean checkHasPermission(Context context, String[] permissions) {
        for (String item : permissions) {
            if (ContextCompat.checkSelfPermission(context, item) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 请求权限
     * @param activity
     * @param permissions
     * @param code
     */
    public static void requestPermissions(Activity activity, String[] permissions, int code) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(permissions, code);
        }
    }


    /**
     * 检测是否已获得所需权限
     *
     * @param grantResults
     * @return
     */
    public static boolean checkGrantResults(int[] grantResults) {
        for (int item : grantResults) {
            if (item == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
