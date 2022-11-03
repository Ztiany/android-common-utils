package com.android.base.utils.android

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