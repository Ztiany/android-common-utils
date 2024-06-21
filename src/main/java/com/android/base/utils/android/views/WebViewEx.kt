@file:JvmName("WebViewEx")

package com.android.base.utils.android.views

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient

/** Destroy the WebView. But it's better to run a WebView in a separate process. */
fun WebView.destroyAll() {
    try {
        webChromeClient = null
        setWebViewClient(object : WebViewClient() {
            @Deprecated("Deprecated in Java", ReplaceWith("true"))
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return true
            }
        })
        webChromeClient = null
        onPause()
        if (parent != null) {
            (parent as ViewGroup).removeAllViews()
        }
        visibility = View.GONE
        removeAllViews()
        if (handler != null) {
            handler.removeCallbacksAndMessages(null)
        }
        destroy()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun WebView.captureBitmapFromWebView(): Bitmap {
    val snapShot = this.capturePicture()
    val bmp = Bitmap.createBitmap(snapShot.width, snapShot.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    snapShot.draw(canvas)
    return bmp
}