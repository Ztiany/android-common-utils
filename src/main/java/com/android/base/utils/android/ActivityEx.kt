package com.android.base.utils.android

import android.app.Activity
import androidx.annotation.AnimRes
import androidx.appcompat.app.AppCompatActivity


fun AppCompatActivity.finishWithAnimation(@AnimRes enterAnim: Int, @AnimRes exitAnim: Int) {
    supportFinishAfterTransition()
    overridePendingTransition(enterAnim, exitAnim)
}

fun AppCompatActivity.finishWithoutAnimation() {
    supportFinishAfterTransition()
    overridePendingTransition(0, 0)
}

/** 获取当前 resume 的 Activity */
fun currentActivity(): Activity? {
    return AppUtils.getTopActivity()
}

/** 获取当前 resume 的 Activity */
fun onTopActivity(action: (activity: Activity) -> Unit) {
    currentActivity()?.let(action)
}