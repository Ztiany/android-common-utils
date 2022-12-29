@file:JvmName("WebViewEx")

package com.android.base.utils.android.views

import android.graphics.Bitmap
import android.graphics.Canvas
import android.webkit.WebView

fun WebView.captureBitmapFromWebView(): Bitmap {
    val snapShot = this.capturePicture()
    val bmp = Bitmap.createBitmap(snapShot.width, snapShot.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    snapShot.draw(canvas)
    return bmp
}