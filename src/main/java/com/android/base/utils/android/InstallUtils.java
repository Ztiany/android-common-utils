package com.android.base.utils.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.android.base.utils.common.Files;
import com.android.base.utils.common.Strings;

import java.io.File;
import java.util.List;

import timber.log.Timber;

public class InstallUtils {

    /**
     * 安装 App，支持 Android 6.0 FileProvider。
     *
     * @param authority FileProvider authorities, default is {@code PackageName + ".fileProvider"}
     */
    public static boolean installApp(Context context, File file, @Nullable String authority) {
        if (file == null || !file.exists()) {
            return false;
        }
        try {
            if (Build.VERSION.SDK_INT <= 23) {
                context.startActivity(getInstallAppIntent23(file));
            } else {
                Intent intent = getInstallAppIntent24(context, file, Strings.isEmpty(authority) ? (context.getPackageName() + ".fileProvider") : authority);
                context.startActivity(intent);
            }
            Timber.d("installApp open  activity successfully");
            return true;
        } catch (Exception e) {
            Timber.e(e, "installApp");
        }
        return false;
    }

    /**
     * 获取安装 App 的意图（支持 7.0）。
     *
     * @param file 文件
     * @return intent
     */
    @NonNull
    @RequiresApi(24)
    private static Intent getInstallAppIntent24(Context context, File file, String authority) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri contentUri = FileProvider.getUriForFile(context, authority, file);
        intent.setDataAndType(contentUri, MimeTypeMap.getSingleton().getMimeTypeFromExtension(Files.getFileExtension(file)));

        // 然后全部授权
        List<ResolveInfo> resolveLists = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveLists) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        return intent;
    }

    /**
     * 获取安装 App 的意图（支持 6.0）。
     *
     * @param file 文件
     * @return intent
     */
    private static Intent getInstallAppIntent23(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type;
        if (Build.VERSION.SDK_INT < 23) {
            type = "application/vnd.android.package-archive";
        } else {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(Files.getFileExtension(file));
        }
        intent.setDataAndType(Uri.fromFile(file), type);
        return intent;
    }

}