package com.android.base.utils.android.compat

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

/**
 * Execute [f] only if the current Android SDK version is [sdkVersion].
 * Do nothing otherwise.
 */
inline fun doAtSDK(sdkVersion: Int, f: () -> Unit) {
    if (Build.VERSION.SDK_INT == sdkVersion) f()
}

/**
 * Execute [f] only if the current Android SDK version greater than [sdkVersion].
 * Do nothing otherwise.
 */
@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
inline fun doAboveSDK(sdkVersion: Int, f: () -> Unit) {
    if (Build.VERSION.SDK_INT > sdkVersion) f()
}

/**
 * Execute [f] only if the current Android SDK version is [version] or newer.
 * Do nothing otherwise.
 */
@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
inline fun doFromSDK(version: Int, f: () -> Unit) {
    if (Build.VERSION.SDK_INT >= version) f()
}

/**
 * Execute [f] only if the current Android SDK version is [version] or older.
 * Do nothing otherwise.
 */
inline fun doBeforeSDK(version: Int, f: () -> Unit) {
    if (Build.VERSION.SDK_INT <= version) f()
}