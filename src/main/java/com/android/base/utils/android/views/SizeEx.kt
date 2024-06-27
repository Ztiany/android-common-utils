@file:JvmName("SizeEx")

package com.android.base.utils.android.views

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt

context(Context)
val Float.dp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)

context(Context)
val Int.dp: Int
    get() = toFloat().dp.roundToInt()

context(Fragment)
val Float.dp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)

context(Fragment)
val Int.dp: Int
    get() = toFloat().dp.roundToInt()

context(View)
val Float.dp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)

context(View)
val Int.dp: Int
    get() = toFloat().dp.roundToInt()

context(Context)
val Float.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, resources.displayMetrics)

context(Context)
val Int.sp: Int
    get() = toFloat().sp.roundToInt()

context(Fragment)
val Float.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, resources.displayMetrics)

context(Fragment)
val Int.sp: Int
    get() = toFloat().sp.roundToInt()

context(View)
val Float.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, resources.displayMetrics)

context(View)
val Int.sp: Int
    get() = toFloat().sp.roundToInt()