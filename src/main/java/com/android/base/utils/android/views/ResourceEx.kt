@file:JvmName("ResourceEx")

package com.android.base.utils.android.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.base.utils.BaseUtils
import com.google.android.material.color.MaterialColors

///////////////////////////////////////////////////////////////////////////
// color & drawable
///////////////////////////////////////////////////////////////////////////

@ColorInt
fun Fragment.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(requireContext(), colorRes)
}

fun Fragment.getDrawableCompat(@DrawableRes colorRes: Int): Drawable? {
    return ContextCompat.getDrawable(requireContext(), colorRes)
}

@ColorInt
fun Context.getColorCompat(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}

@ColorInt
fun View.getColorCompat(@ColorRes colorRes: Int): Int {
    val safeContext = context ?: return 0
    return ContextCompat.getColor(safeContext, colorRes)
}

fun View.getDrawableCompat(@DrawableRes colorRes: Int): Drawable? {
    return ContextCompat.getDrawable(context, colorRes)
}

@ColorInt
fun RecyclerView.ViewHolder.getColorCompat(@ColorRes id: Int): Int {
    return ContextCompat.getColor(itemView.context, id)
}

fun RecyclerView.ViewHolder.getDrawableCompat(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(itemView.context, id)
}

/**
 * - see [https://mp.weixin.qq.com/s?__biz=MzAwODY4OTk2Mg==&mid=2652064078&idx=2&sn=884a85bfa4f9e19ec531b8245dca6314&chksm=808ce90bb7fb601dd9e144ae796eb466a2ed805bf14dbc0906543509d95c24733b05bd7fc12c&mpshare=1&scene=1&srcid=0831g6YpKX89pYtCnFVNtFSn&sharer_sharetime=1598887915007&sharer_shareid=837da3c9c7d8315352e3f3c120932755&key=573aef4c1f9b4b5f6b748d6d2bfe57b91121aed37b7ab5befb427350215abee2d42095153128819e3458a2f7900e0b083f777100a3df99374344e4d71d46d2f4fd9f1899809b98d63f5f75f20d075c994d742b73d51bd81cbc1f9daebc6366021f69043f1ca77a85434e6261b9a1d74582dbff5ba02636166dadd2b1750bb0e7&ascene=1&uin=MTE4NTEwMDEzMA%3D%3D&devicetype=Windows+10+x64&version=62090538&lang=en&exportkey=A96nCDrEWbr1qzYfBqWXtt4%3D&pass_ticket=ss1Q7B2SYeRyfoU%2FAdoyx1xQRnQRw3XnUna%2FNKH3Q%2FnYnBXqqpEL3CF%2BMICAcEIk]
 * - see [https://stackoverflow.com/questions/43004886/resourcescompat-getdrawable-vs-appcompatresources-getdrawable]
 */
fun Context.getColorStateListCompat(@ColorRes id: Int): ColorStateList {
    return AppCompatResources.getColorStateList(this, id)
}

///////////////////////////////////////////////////////////////////////////
// styled resource
///////////////////////////////////////////////////////////////////////////

/**
 * - [name] 资源的名称，如 `ic_launcher`，或者 `com.example.android/drawable/ic_launcher`，对于这种全限定名的 name，其他两个参数应该为空。
 * - [defType] 资源的类型，如 drawable
 * - [defPackage] 包名
 *
 * 返回资源 id
 */
fun Context.getResourceId(name: String, defType: String = "", defPackage: String = ""): Int {
    return resources.getIdentifier(name, defType, defPackage)
}

/**
 * - [attr], like [android.R.attr.selectableItemBackground] or other attr id.
 */
fun Context.getResourceId(@AttrRes attr: Int): Int {
    return try {
        val outValue = TypedValue()
        theme.resolveAttribute(attr, outValue, true)
        outValue.resourceId
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

@ColorInt
fun Context.getStyledColor(@AttrRes attr: Int, name: String): Int {
    return MaterialColors.getColor(this, attr, "$name is not provided in the theme.")
}

@ColorInt
fun Context.getStyledColor(@AttrRes attr: Int, @ColorInt defaultColor: Int): Int {
    return MaterialColors.getColor(this, attr, defaultColor)
}

fun Context.getStyledDrawable(@AttrRes attr: Int): Drawable? {
    val a = obtainStyledAttributes(null, intArrayOf(attr))
    try {
        return a.getDrawable(0)
    } finally {
        a.recycle()
    }
}

///////////////////////////////////////////////////////////////////////////
// URI
///////////////////////////////////////////////////////////////////////////

fun createResourceUri(id: Int): Uri {
    return Uri.parse("android.resource://" + BaseUtils.getAppContext().packageName + "/" + id)
}

fun createAssetsUri(path: String): Uri {
    return Uri.parse("file:///android_asset/$path")
}