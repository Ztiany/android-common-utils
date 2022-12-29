package com.blankj.utilcode.util;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.lifecycle.Lifecycle;

import com.android.base.utils.BaseUtils;

/**
 * @see <a href='https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/src/main/java/com/blankj/utilcode/util/Utils.java'>AndroidUtilCode's Utils</a>
 */
public class Utils {

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public static void init(Application application) {
        UtilsBridge.init(application);
    }

    public static Application getApp() {
        return (Application) BaseUtils.getAppContext();
    }

    public static class ActivityLifecycleCallbacks {

        public void onActivityCreated(@NonNull Activity activity) {/**/}

        public void onActivityStarted(@NonNull Activity activity) {/**/}

        public void onActivityResumed(@NonNull Activity activity) {/**/}

        public void onActivityPaused(@NonNull Activity activity) {/**/}

        public void onActivityStopped(@NonNull Activity activity) {/**/}

        public void onActivityDestroyed(@NonNull Activity activity) {/**/}

        public void onLifecycleChanged(@NonNull Activity activity, Lifecycle.Event event) {/**/}
    }

    public interface OnAppStatusChangedListener {
        void onForeground(Activity activity);

        void onBackground(Activity activity);
    }

}
