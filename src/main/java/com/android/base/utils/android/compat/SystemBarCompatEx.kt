package com.android.base.utils.android.compat

import android.app.Activity
import androidx.annotation.ColorInt


fun Activity.setStatusBarLightMode() {
    SystemBarCompat.setLightStatusBar(this, true)
}

fun Activity.setStatusBarDarkMode() {
    SystemBarCompat.setLightStatusBar(this, false)
}

fun Activity.setNavigationBarLightMode() {
    SystemBarCompat.setLightNavigationBar(this, true)
}

fun Activity.setNavigationBarDarkMode() {
    SystemBarCompat.setLightNavigationBar(this, false)
}

fun Activity.setExtendsToSystemBars() {
    SystemBarCompat.setExtendsToSystemBars(this, true)
}

fun Activity.setStatusBarColor(@ColorInt color: Int) {
    SystemBarCompat.setStatusBarColor(this, color)
}

fun Activity.setNavigationBarColor(@ColorInt color: Int) {
    SystemBarCompat.setNavigationBarColor(this, color)
}