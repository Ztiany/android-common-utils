package com.android.base.utils.android.compat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.android.base.utils.common.ignoreCrash
import com.android.base.utils.common.invoke

private const val REQUEST_CODE = 20099

/**
 * References:
 *
 * - [android-toast-doesnt-show-up](https://stackoverflow.com/questions/53703013/android-toast-doesnt-show-up)
 * - [disabling-notifications-also-disables-toast-on-oreo](https://stackoverflow.com/questions/50742586/disabling-notifications-also-disables-toast-on-oreo)
 * - [issuetracker.google.com/issues/36951147](https://issuetracker.google.com/issues/36951147)
 */
class NotificationCompat {

    fun isNotificationEnabled(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun openNotificationSettings(context: Activity) {
        //判断API，跳转到应用通知管理页面
        val localIntent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0及以上
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            localIntent.data = Uri.fromParts("package", context.packageName, null)
        } else (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//8.0以下
            localIntent.action = "android.settings.APP_NOTIFICATION_SETTINGS";
            localIntent.putExtra("app_package", context.packageName);
            localIntent.putExtra("app_uid", context.applicationInfo.uid);
        }
        ignoreCrash {
            context.startActivityForResult(localIntent, REQUEST_CODE)
        }
    }

    fun onNotificationResult(context: Activity, requestCode: Int, onResult: (Boolean) -> Unit) {
        if (requestCode == REQUEST_CODE) {
            onResult(isNotificationEnabled(context))
        }
    }

}