package com.android.base.utils.android.views

import android.content.Context
import com.android.base.utils.BaseUtils
import com.blankj.utilcode.util.ActivityUtils

internal fun getActivityContext(): Context {
    return ActivityUtils.getTopActivity() ?: BaseUtils.getAppContext()
}