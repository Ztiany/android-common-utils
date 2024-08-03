package com.android.base.utils.android.compat;

import android.os.Build;

import androidx.annotation.ChecksSdkIntAtLeast;

/**
 * Android SDK 版本判断。
 */
public class AndroidVersion {

    private AndroidVersion() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param sdkVersion 要求的版本。
     * @return true when the caller API version is > level
     */
    @ChecksSdkIntAtLeast(parameter = 0)
    public static boolean above(int sdkVersion) {
        return Build.VERSION.SDK_INT > sdkVersion;
    }

    /**
     * @param sdkVersion 要求的版本。
     * @return true when the caller API version >= level
     */
    @ChecksSdkIntAtLeast(parameter = 0)
    public static boolean atLeast(int sdkVersion) {
        return Build.VERSION.SDK_INT >= sdkVersion;
    }

    /**
     * 当前系统版本  == level。
     */
    public static boolean at(int sdkVersion) {
        return Build.VERSION.SDK_INT == sdkVersion;
    }

}
