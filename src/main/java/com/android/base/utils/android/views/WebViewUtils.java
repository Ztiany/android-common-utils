package com.android.base.utils.android.views;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * 关于内存泄漏，最好的方式还是开启独立的进程。
 */
public class WebViewUtils {

    public static void destroy(WebView webView, boolean pauseTimers) {
        try {
            if (webView == null) {
                return;
            }
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.onPause();
            if (webView.getParent() != null) {
                ((ViewGroup) webView.getParent()).removeAllViews();
            }
            webView.setVisibility(View.GONE);//解决崩溃问题 Receiver not Register
            webView.removeAllViews();
            if (webView.getHandler() != null) {
                webView.getHandler().removeCallbacks(null);
            }
            if (pauseTimers) {
                // Pauses all layout, parsing, and JavaScript timers for all WebViews.
                // This is a global requests, not restricted to just this WebView. This can be useful if the application has been paused.
                webView.pauseTimers();
            }
            webView.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void destroy(WebView webView) {
        destroy(webView, false);
    }

}