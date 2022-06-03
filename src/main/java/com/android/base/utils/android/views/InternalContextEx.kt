package com.android.base.utils.android.views

import android.content.Context
import com.android.base.utils.BaseUtils
import com.android.base.utils.android.AppUtils

internal fun getActivityContext(): Context {
    return AppUtils.getTopActivity() ?: BaseUtils.getAppContext()
}