package com.android.base.utils.android.compat

import android.app.Activity
import androidx.annotation.ColorInt

/**
 * Take effect as of API 23.
 */
fun Activity.setStatusBarLightMode() {
    SystemBarCompat.setLightStatusBar(this, true)
}

/**
 * Take effect as of API 23.
 */
fun Activity.setStatusBarDarkMode() {
    SystemBarCompat.setLightStatusBar(this, false)
}

/**
 * Take effect as of API 26.
 */
fun Activity.setNavigationBarLightMode() {
    SystemBarCompat.setLightNavigationBar(this, true)
}

/**
 * Take effect as of API 26.
 */
fun Activity.setNavigationBarDarkMode() {
    SystemBarCompat.setLightNavigationBar(this, false)
}

/**
 * Take effect as of API 21.
 */
fun Activity.setLayoutExtendsToSystemBars() {
    SystemBarCompat.setLayoutExtendsToSystemBars(this, true)
}

/**
 * Take effect as of API 21.
 */
private fun Activity.setLayoutExtendsToSystemBars(
    status: Boolean = true,
    transparentStatusBar: Boolean = true,
    navigation: Boolean = true,
    transparentNavigationBar: Boolean = true,
    displayInCutout: Boolean = true,
) {
    SystemBarCompat.setLayoutExtendsToSystemBars(
        this,
        status, transparentStatusBar,
        navigation, transparentNavigationBar,
        displayInCutout
    )
}


/**
 * Take effect as of API 28.
 */
private fun Activity.setLayoutDisplayInCutout() {
    SystemBarCompat.setLayoutDisplayInCutout(this)
}

/**
 * Take effect as of API 21.
 */
fun Activity.setStatusBarColor(@ColorInt color: Int) {
    SystemBarCompat.setStatusBarColor(this, color)
}

/**
 * Take effect as of API 21.
 */
fun Activity.setNavigationBarColor(@ColorInt color: Int) {
    SystemBarCompat.setNavigationBarColor(this, color)
}