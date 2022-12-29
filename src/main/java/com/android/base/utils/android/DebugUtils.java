package com.android.base.utils.android;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.text.format.Formatter;

import com.android.base.utils.BaseUtils;
import com.blankj.utilcode.util.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * @author Ztiany
 */
public class DebugUtils {

    /**
     * 开启严苛模式
     */
    public static void startStrictMode() {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeathOnNetwork()
                .build());
    }

    @SuppressLint("ObsoleteSdkInt")
    public static void printSystemInfo() {
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        StringBuilder sb = new StringBuilder();

        sb.append("_______\n_______  系统信息  ").append(time).append(" ______________");
        sb.append("\nID                 : ").append(Build.ID);
        sb.append("\nBRAND              : ").append(Build.BRAND);
        sb.append("\nMODEL              : ").append(Build.MODEL);
        sb.append("\nRELEASE            : ").append(Build.VERSION.RELEASE);
        sb.append("\nSDK                : ").append(Build.VERSION.SDK);

        sb.append("\n_______ OTHER _______");
        sb.append("\nBOARD              : ").append(Build.BOARD);
        sb.append("\nPRODUCT            : ").append(Build.PRODUCT);
        sb.append("\nDEVICE             : ").append(Build.DEVICE);
        sb.append("\nFINGERPRINT        : ").append(Build.FINGERPRINT);
        sb.append("\nHOST               : ").append(Build.HOST);
        sb.append("\nTAGS               : ").append(Build.TAGS);
        sb.append("\nTYPE               : ").append(Build.TYPE);
        sb.append("\nTIME               : ").append(Build.TIME);
        sb.append("\nINCREMENTAL        : ").append(Build.VERSION.INCREMENTAL);

        sb.append("\n_______ CUPCAKE-3 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            sb.append("\nDISPLAY            : ").append(Build.DISPLAY);
        }

        sb.append("\n_______ DONUT-4 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            sb.append("\nSDK_INT            : ").append(Build.VERSION.SDK_INT);
            sb.append("\nMANUFACTURER       : ").append(Build.MANUFACTURER);
            sb.append("\nBOOTLOADER         : ").append(Build.BOOTLOADER);
            sb.append("\nCPU_ABI            : ").append(Build.CPU_ABI);
            sb.append("\nCPU_ABI2           : ").append(Build.CPU_ABI2);
            sb.append("\nHARDWARE           : ").append(Build.HARDWARE);
            sb.append("\nUNKNOWN            : ").append(Build.UNKNOWN);
            sb.append("\nCODENAME           : ").append(Build.VERSION.CODENAME);
        }

        sb.append("\n_______ MEMORY-INFO _______");
        ActivityManager activityManager = (ActivityManager) BaseUtils.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        Runtime runtime = Runtime.getRuntime();
        sb.append("\nTotalMem           : ").append(Formatter.formatFileSize(BaseUtils.getAppContext(), memoryInfo.totalMem));
        sb.append("\nAvailMem           : ").append(Formatter.formatFileSize(BaseUtils.getAppContext(), memoryInfo.availMem));
        sb.append("\nThreshold          : ").append(Formatter.formatFileSize(BaseUtils.getAppContext(), memoryInfo.threshold));
        sb.append("\nMaxMemory          : ").append(Formatter.formatFileSize(BaseUtils.getAppContext(), runtime.maxMemory()));
        sb.append("\nTotalMemory        : ").append(Formatter.formatFileSize(BaseUtils.getAppContext(), runtime.totalMemory()));
        sb.append("\nFreeMemory         : ").append(Formatter.formatFileSize(BaseUtils.getAppContext(), runtime.freeMemory()));

        sb.append("\n_______ SCREEN-INFO _______");
        sb.append("\nScreenDensity      : ").append(ScreenUtils.getScreenDensity());
        sb.append("\nScreenWidth        : ").append(ScreenUtils.getScreenWidth());
        sb.append("\nScreenHeight       : ").append(ScreenUtils.getScreenHeight());
        sb.append("\nAppScreenWidth     : ").append(ScreenUtils.getAppScreenWidth());
        sb.append("\nAppScreenHeight    : ").append(ScreenUtils.getAppScreenHeight());

        //print all the information.
        Timber.tag("DEVICES").i(sb.toString());
    }

}