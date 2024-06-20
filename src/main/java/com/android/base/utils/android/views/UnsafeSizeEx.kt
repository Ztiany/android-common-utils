@file:JvmName("UnsafeSizeEx")

package com.android.base.utils.android.views

import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import kotlin.math.roundToInt

/** convert dip values to px values. */
@UnSafeContext
fun dip(dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContextResource().displayMetrics)
}

/** convert dip values to px values. */
@UnSafeContext
fun dip(dp: Int): Int {
    return dip(dp.toFloat()).roundToInt()
}

/** convert sp values to px values. */
@UnSafeContext
fun sp(sp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getContextResource().displayMetrics)
}

/** convert sp values to px values. */
@UnSafeContext
fun sp(sp: Int): Int {
    return sp(sp.toFloat()).roundToInt()
}

@UnSafeContext
fun pxToDp(px: Float): Float {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        TypedValue.deriveDimension(TypedValue.COMPLEX_UNIT_DIP, px, getContextResource().displayMetrics)
    } else {
        px / getContextResource().displayMetrics.density
    }
}

@UnSafeContext
fun pxToDp(px: Int): Int {
    return pxToDp(px.toFloat()).roundToInt()
}

@UnSafeContext
fun pxToSp(px: Float): Float {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        TypedValue.deriveDimension(TypedValue.COMPLEX_UNIT_SP, px, getContextResource().displayMetrics)
    } else {
        (px / getContextResource().displayMetrics.scaledDensity)
    }
}

@UnSafeContext
fun pxToSp(px: Int): Int {
    return pxToSp(px.toFloat()).roundToInt()
}

@UnSafeContext
private fun getContextResource(): Resources {
    return getSafeContext().resources
}