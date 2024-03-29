@file:JvmName("SizeEx")

package com.android.base.utils.android.views

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt

fun dip(dp: Float): Float {
    return dp * getContextResource().displayMetrics.density
}

fun dip(dp: Int): Int {
    return (dp * getContextResource().displayMetrics.density).roundToInt()
}

fun sp(sp: Float): Float {
    return sp * getContextResource().displayMetrics.scaledDensity
}

fun sp(sp: Int): Int {
    return (sp * getContextResource().displayMetrics.scaledDensity).roundToInt()
}

fun pxToDp(px: Float): Float {
    return px / getContextResource().displayMetrics.density
}

fun pxToDp(px: Int): Int {
    return (px / getContextResource().displayMetrics.density).roundToInt()
}

fun pxToSp(px: Float): Float {
    return px / getContextResource().displayMetrics.scaledDensity
}

fun pxToSp(px: Int): Int {
    return (px / getContextResource().displayMetrics.scaledDensity).roundToInt()
}

/**
 * 各种单位转换，该方法存在于[TypedValue] 中。
 *
 * @param unit  单位
 * @param value 值
 * @return 转换结果
 */
fun applyDimension(unit: Int, value: Float): Float {
    val metrics = getContextResource().displayMetrics
    when (unit) {
        TypedValue.COMPLEX_UNIT_PX -> return value
        TypedValue.COMPLEX_UNIT_DIP -> return value * metrics.density
        TypedValue.COMPLEX_UNIT_SP -> return value * metrics.scaledDensity
        TypedValue.COMPLEX_UNIT_PT -> return value * metrics.xdpi * (1.0F / 72)
        TypedValue.COMPLEX_UNIT_IN -> return value * metrics.xdpi
        TypedValue.COMPLEX_UNIT_MM -> return value * metrics.xdpi * (1.0F / 25.4F)
    }
    return 0f
}

private fun getContextResource(): Resources {
    return getActivityContext().resources
}