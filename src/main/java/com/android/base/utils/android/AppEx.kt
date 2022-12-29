package com.android.base.utils.android

import android.app.Activity
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

/** 获取可观察的 app 生命周期，发射 true 表示 app 切换到前台，发射 false 表示 app 切换到后台。  */
internal fun observableAppState(): Flow<Boolean> {
    return internalAppStatus
}

/** 获取可观察的 app 生命周期。  */
private val internalAppStatus = MutableStateFlow(true)

internal fun listenAppLifecycle() {
    AppUtils.registerAppStatusChangedListener(object : Utils.OnAppStatusChangedListener {
        override fun onBackground(activity: Activity?) {
            Timber.d("app进入后台。")
            internalAppStatus.value = false
        }

        override fun onForeground(activity: Activity?) {
            Timber.d("app进入前台。")
            internalAppStatus.value = true
        }
    })
}