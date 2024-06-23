package com.android.base.utils;

import static com.android.base.utils.android.AppExKt.listenAppLifecycle;
import static com.android.base.utils.android.AppExKt.observableAppState;
import static com.android.base.utils.android.network.NetworkStateKt.initNetworkState;
import static com.android.base.utils.android.network.NetworkStateKt.observableNetworkState;

import android.app.Application;
import android.content.Context;

import com.android.base.utils.android.network.NetworkState;
import com.android.base.utils.android.views.ViewInteractionEx;
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

    /**
     * Get the state of the network.
     */
    public static Flow<NetworkState> networkStateFlow() {
        return observableNetworkState();
    }

    /**
     * Get the application's state of being in the foreground or background.
     */
    public static Flow<Boolean> appStateFlow() {
        return observableAppState();
    }

    /**
     * Set the interval time of the a click event.
     *
     * @param clickInterval The interval time of the a click event.
     */
    public static void setClickInterval(long clickInterval) {
        ViewInteractionEx.setViewClickThrottledTime(clickInterval);
        ViewInteractionEx.setViewClickDebouncedTime(clickInterval);
    }

}