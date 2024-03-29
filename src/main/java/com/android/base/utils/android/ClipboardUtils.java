package com.android.base.utils.android;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.android.base.utils.BaseUtils;

public class ClipboardUtils {

    /**
     * 复制文本到剪贴板。
     */
    public static void copyText(CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) BaseUtils.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("text", text));
    }

    /**
     * 获取剪贴板的文本。
     *
     * @return 剪贴板的文本
     */
    public static CharSequence getText() {
        ClipboardManager clipboard = (ClipboardManager) BaseUtils.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(BaseUtils.getAppContext());
        }
        return null;
    }

    /**
     * 复制 Uri 到剪贴板。
     */
    public static void copyUri(Uri uri) {
        ClipboardManager clipboard = (ClipboardManager) BaseUtils.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newUri(BaseUtils.getAppContext().getContentResolver(), "uri", uri));
    }

    /**
     * 获取剪贴板的 Uri。
     *
     * @return 剪贴板的 uri
     */
    public static Uri getUri() {
        ClipboardManager clipboard = (ClipboardManager) BaseUtils.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getUri();
        }
        return null;
    }

    /**
     * 复制意图到剪贴板。
     */
    public static void copyIntent(Intent intent) {
        ClipboardManager clipboard = (ClipboardManager) BaseUtils.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newIntent("intent", intent));
    }

    public static Intent getClipboardIntent() {
        ClipboardManager clipboard = (ClipboardManager) BaseUtils.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getIntent();
        }
        return null;
    }

}