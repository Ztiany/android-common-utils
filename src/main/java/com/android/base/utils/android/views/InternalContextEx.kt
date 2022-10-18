package com.android.base.utils.android.views

import android.content.Context
import com.android.base.utils.BaseUtils
import com.blankj.utilcode.util.AppUtils

internal fun getActivityContext(): Context {
    return AppUtils.getTopActivity() ?: BaseUtils.getAppContext()
}