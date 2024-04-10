@file:JvmName("ViewMeasureEx")

package com.android.base.utils.android.views

import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ScreenUtils

fun View.measureSelf(): Boolean {
    val layoutParams = layoutParams
    if (layoutParams == null || (layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT)) {
        return false
    }
    val size = 1 shl 30 - 1//即后 30 位
    val measureSpec = View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.AT_MOST)
    measure(measureSpec, measureSpec)
    return true
}

fun View.measureSelfWithScreenSize(): Boolean {
    val layoutParams = layoutParams
    if (layoutParams == null || (layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT)) {
        return false
    }
    measure(
        View.MeasureSpec.makeMeasureSpec(ScreenUtils.getAppScreenWidth(), View.MeasureSpec.AT_MOST),
        View.MeasureSpec.makeMeasureSpec(ScreenUtils.getAppScreenHeight(), View.MeasureSpec.AT_MOST)
    )
    return true
}

fun View.measureSelf(width: Int, height: Int): Boolean {
    val layoutParams = layoutParams
    if (layoutParams == null || (layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT)) {
        return false
    }
    measure(
        View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST),
        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST)
    )
    return true
}