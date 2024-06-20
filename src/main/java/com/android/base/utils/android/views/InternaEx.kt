package com.android.base.utils.android.views

import android.app.Activity
import android.app.Application
import android.content.Context
import com.android.base.utils.BaseUtils
import com.blankj.utilcode.util.ActivityUtils

internal fun getTopActivityContext(): Context? {
    return ActivityUtils.getTopActivity()
}

internal fun getSafeContext(): Context {
    return getTopActivityContext() ?: BaseUtils.getAppContext()
}

/**
 * Caution: The needed [Context] ([Activity] level) is obtained by [Application.registerComponentCallbacks].
 * You'd better n't call methods annotated by UnSafeContext while your app is running in the background.
 * In the App's background, the [Application] context is used in these methods, then it may cause
 * the un-styled resources.
 */
@Retention(AnnotationRetention.SOURCE)
internal annotation class UnSafeContext