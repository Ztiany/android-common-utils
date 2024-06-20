@file:JvmName("UnsafeResourceEx")

package com.android.base.utils.android.views

import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes

///////////////////////////////////////////////////////////////////////////
// text
///////////////////////////////////////////////////////////////////////////

@UnSafeContext
fun getText(@StringRes id: Int): CharSequence {
    return getSafeContext().resources.getText(id)
}

@UnSafeContext
fun getString(@StringRes id: Int): String {
    return getSafeContext().resources.getString(id)
}

@UnSafeContext
fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
    return getSafeContext().resources.getString(id, *formatArgs)
}

@UnSafeContext
fun getStringArray(@ArrayRes id: Int): Array<String> {
    return getSafeContext().resources.getStringArray(id)
}

@UnSafeContext
fun getDimensionPixelSize(dimenId: Int): Int {
    return getSafeContext().resources.getDimensionPixelSize(dimenId)
}

@UnSafeContext
fun getIntArray(@ArrayRes id: Int): IntArray {
    return getSafeContext().resources.getIntArray(id)
}

///////////////////////////////////////////////////////////////////////////
// styled resource
///////////////////////////////////////////////////////////////////////////

@UnSafeContext
fun getStyledColor(@AttrRes attr: Int, name: String): Int {
    return getSafeContext().getStyledColor(attr, name)
}

@UnSafeContext
fun getStyledColor(@AttrRes attr: Int, @ColorInt defaultColor: Int): Int {
    return getSafeContext().getStyledColor(attr, defaultColor)
}

@UnSafeContext
fun getStyledDrawable(@AttrRes attr: Int): Drawable? {
    return getSafeContext().getStyledDrawable(attr)
}