package com.android.base.utils;

import static com.android.base.utils.android.AppExKt.listenAppLifecycle;
import static com.android.base.utils.android.AppExKt.observableAppState;
import static com.android.base.utils.android.network.NetworkStateKt.initNetworkState;
import static com.android.base.utils.android.network.NetworkStateKt.observableNetworkState;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.android.base.utils.android.network.NetworkState;
import com.android.base.utils.android.views.AntiShakeUtil;
import com.blankj.utilcode.util.Utils;

import java.util.concurrent.atomic.AtomicBoolean;

import kotlinx.coroutines.flow.Flow;

/**
 * 工具类初始化入口。
 */
public class BaseUtils {

    private static final AtomicBoolean isInitialized = new AtomicBoolean(false);

    private static Application sApplication;

    public static void init(Application application) {
        if (isInitialized.compareAndSet(false, true)) {
            sApplication = application;
            Utils.init(application);
            listenAppLifecycle();
            initNetworkState(application);
        }
    }

    public static Context getAppContext() {
        return sApplication;
    }

    public static Resources getResources() {
        return sApplication.getResources();
    }

    public static AssetManager getAssets() {
        return sApplication.getAssets();
    }

    public static DisplayMetrics getDisplayMetrics() {
        return sApplication.getResources().getDisplayMetrics();
    }

    /**
     * 获取可以监听网络状态。
     */
    public static Flow<NetworkState> networkStateFlow() {
        return observableNetworkState();
    }

    /**
     * 获取可以监听网络状态。
     */
    public static Flow<Boolean> appStateFlow() {
        return observableAppState();
    }

    public static void setClickInterval(long clickInterval) {
        AntiShakeUtil.setClickInterval(clickInterval);
    }

}