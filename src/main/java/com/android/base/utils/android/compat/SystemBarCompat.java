package com.android.base.utils.android.compat;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.android.base.utils.R;


/**
 * A tool for adjusting system bars.
 *
 * <p>
 * There are some useful utils you may need to know:
 * <ol>
 * <li>{@link ViewCompat}</li>
 * <li>{@link WindowCompat}</li>
 * <li>{@link WindowInsetsCompat}</li>
 * <li>{@link androidx.core.view.WindowInsetsControllerCompat}</li>
 * <li>{@link androidx.activity.EdgeToEdge}</li>
 * </ol>
 * </p>
 *
 * @author Ztiany
 */
public class SystemBarCompat {

    private SystemBarCompat() {
        throw new UnsupportedOperationException();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Full Screen
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @deprecated {@link  WindowManager.LayoutParams#FLAG_FULLSCREEN} is not recommended to use.
     */
    @Deprecated
    public static void setFullScreen(@NonNull Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    ///////////////////////////////////////////////////////////////////////////
    // setDecorFitsSystemWindows
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Let the layout extend to the status bar and navigation area, and set the color of the status bar and navigation bar to transparent.
     */
    public static void setLayoutExtendsToSystemBars(@NonNull Activity activity, boolean extend) {
        extendToSystemBarsInternally(activity, extend, extend, extend, extend, extend);
    }

    public static void setLayoutExtendsToSystemBars(@NonNull Activity activity, boolean status, boolean navigation) {
        extendToSystemBarsInternally(activity, status, status, navigation, navigation, status && navigation);
    }

    public static void setLayoutExtendsToSystemBars(
            @NonNull Activity activity,
            boolean status,
            boolean transparentStatusBar,
            boolean navigation,
            boolean transparentNavigationBar,
            boolean displayInCutout) {
        extendToSystemBarsInternally(activity, status, transparentStatusBar, navigation, transparentNavigationBar, displayInCutout);
    }

    @Deprecated
    public static void setLayoutExtendsToSystemBarsOnlyFor19(@NonNull Activity activity, boolean status, boolean navigation) {
        if (AndroidVersion.at(19)) {
            setTranslucentSystemBar(activity.getWindow(), status, navigation);
        }
    }

    private static void extendToSystemBarsInternally(
            @NonNull Activity activity,
            boolean status,
            boolean transparentStatusBar,
            boolean navigation,
            boolean transparentNavigationBar,
            boolean displayInCutout
    ) {
        Window window = activity.getWindow();
        if (AndroidVersion.atLeast(30) && (status == navigation)) {

            WindowCompat.setDecorFitsSystemWindows(window, !status);
            if (transparentStatusBar) {
                setStatusBarColorAfter19(activity, Color.TRANSPARENT);
            }
            if (transparentNavigationBar) {
                setNavigationBarColorAfter19(activity, Color.TRANSPARENT);
            }
        } else if (AndroidVersion.atLeast(21)) {

            if (navigation && status) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                if (transparentStatusBar) {
                    setStatusBarColorAfter19(activity, Color.TRANSPARENT);
                }
                if (transparentNavigationBar) {
                    setNavigationBarColorAfter19(activity, Color.TRANSPARENT);
                }
            } else if (status) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                if (transparentStatusBar) {
                    setStatusBarColorAfter19(activity, Color.TRANSPARENT);
                }
            } else if (navigation) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                if (transparentNavigationBar) {
                    setNavigationBarColorAfter19(activity, Color.TRANSPARENT);
                }
            }
        } else if (AndroidVersion.at(19)) {
            setTranslucentSystemBar(window, status, navigation);
        }

        if (displayInCutout) {
            setLayoutDisplayInCutout(activity);
        }
    }

    private static void setTranslucentSystemBar(Window win, boolean status, boolean navigation) {
        WindowManager.LayoutParams winParams = win.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (status) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (navigation) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    ///////////////////////////////////////////////////////////////////////////
    // SystemBar Color
    ///////////////////////////////////////////////////////////////////////////

    public static void setStatusBarColor(@NonNull Activity activity, @ColorInt int color) {
        setStatusBarColorOn19(activity, color);
        setStatusBarColorAfter19(activity, color);
    }

    public static void setNavigationBarColor(@NonNull Activity activity, @ColorInt int color) {
        setNavigationBarColorAfter19(activity, color);
    }

    private static void setStatusBarColorAfter19(@NonNull Activity activity, @ColorInt int color) {
        if (!AndroidVersion.above(20)) {
            return;
        }
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().setStatusBarColor(color);
    }

    private static void setNavigationBarColorAfter19(@NonNull Activity activity, @ColorInt int color) {
        if (!AndroidVersion.above(20)) {
            return;
        }
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(color);
    }

    @Deprecated
    private static View setStatusBarColorOn19(@NonNull Activity activity, @ColorInt int color) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        return setupStatusBarViewOn19(activity, decorView, color);
    }

    /**
     * This method is only available for Android 4.4. What this method does is to add a View with the same height as the StatusBar
     * to the rootView for coloring the StatusBar.
     *
     * @param rootView The root view used to add the coloring View.
     * @param color    The color of the StatusBar.
     * @return The View that colors the StatusBar.
     */
    private static View setupStatusBarViewOn19(@NonNull Activity activity, ViewGroup rootView, @ColorInt int color) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) {
            return null;
        }
        View statusBarTintView = rootView.findViewById(R.id.base_status_view_id);
        if (statusBarTintView == null) {
            statusBarTintView = new View(activity);
            statusBarTintView.setId(R.id.base_status_view_id);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
            layoutParams.gravity = Gravity.TOP;
            statusBarTintView.setLayoutParams(layoutParams);
            rootView.addView(statusBarTintView, 0);
        }
        statusBarTintView.setBackgroundColor(color);
        return statusBarTintView;
    }

    ///////////////////////////////////////////////////////////////////////////
    // SystemBar height
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get the height of the StatusBar, if the StatusBar is not displayed, return 0. Visibility of window decorations means whether these
     * elements are currently visible or hidden. For example:
     *
     * <ul>
     *     <li>Status Bar: This is the bar at the top of the screen that typically shows notifications and system information.</li>
     *     <li>Navigation Bar: This is the bar at the bottom of the screen with back, home, and recent apps buttons.</li>
     * </ul>
     */
    public static int getStatusBarHeight(@NonNull Activity activity) {
        int statusBarHeight = 0;
        /*
            1. This method returns the original insets dispatched to the view tree.
            2. Insets are only available when the view is attached.
            3. API 20 and below always return false
         */
        WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(activity.getWindow().getDecorView());
        if (windowInsets != null) {
            statusBarHeight = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
        }
        return statusBarHeight;
    }

    public static int getStatusBarHeightIgnoreVisibility(@NonNull Activity activity) {
        return getStatusBarHeightIgnoreVisibility(activity, activity.getWindow());
    }

    public static int getStatusBarHeightIgnoreVisibility(@NonNull Context context, @NonNull Window window) {
        int statusBarHeight = 0;
        @SuppressLint("InternalInsetResource")
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        if (statusBarHeight <= 0) {
            WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(window.getDecorView());
            if (windowInsets != null) {
                statusBarHeight = windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.statusBars()).top;
            }
        }

        return statusBarHeight;
    }

    /**
     * Get the height of the NavigationBar, if the NavigationBar is not displayed, return 0. Visibility of window decorations means whether these
     * elements are currently visible or hidden. For example:
     *
     * <ul>
     *     <li>Status Bar: This is the bar at the top of the screen that typically shows notifications and system information.</li>
     *     <li>Navigation Bar: This is the bar at the bottom of the screen with back, home, and recent apps buttons.</li>
     * </ul>
     */
    public static int getNavigationBarHeight(@NonNull Activity activity) {
        int navigationBarHeight = 0;
        WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(activity.getWindow().getDecorView());
        if (windowInsets != null) {
            navigationBarHeight = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
        }
        return navigationBarHeight;
    }

    public static int getNavigationBarHeightIgnoreVisibility(@NonNull Activity activity) {
        return getNavigationBarHeightIgnoreVisibility(activity, activity.getWindow());
    }

    public static int getNavigationBarHeightIgnoreVisibility(@NonNull Context context, @NonNull Window window) {
        int navigationBarHeight = 0;
        Resources resources = context.getResources();
        @SuppressLint("InternalInsetResource")
        int id = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(id);
        }

        if (navigationBarHeight <= 0) {
            WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(window.getDecorView());
            if (windowInsets != null) {
                navigationBarHeight = windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.navigationBars()).bottom;
            }
        }

        return navigationBarHeight;
    }

    /**
     * @see androidx.core.view.WindowInsetsCompat
     * @see <a href='https://juejin.cn/post/7038422081528135687'>Android Detail：Window 篇—— WindowInsets 与 fitsSystemWindow</a>
     */
    public static boolean hasNavigationBar(@NonNull Activity activity) {
        return getNavigationBarHeight(activity) > 0;
    }

    public static int getActionBarHeight(@NonNull Activity activity) {
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        return 0;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Cutout
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @see <a href='https://developer.android.com/develop/ui/views/layout/display-cutout'>Support display cutouts</a>
     * @see <a href='https://juejin.im/post/5cf635846fb9a07f0c466ea7'>Android 刘海屏、水滴屏全面屏适配方案</a>
     */
    public static void setLayoutDisplayInCutout(@NonNull Window window) {
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (AndroidVersion.atLeast(30)) {
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS;
        } else if (AndroidVersion.atLeast(28)) {
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        window.setAttributes(attributes);
    }

    /**
     * @see #setLayoutDisplayInCutout(Window)
     */
    public static void setLayoutDisplayInCutout(@NonNull Activity activity) {
        setLayoutDisplayInCutout(activity.getWindow());
    }

    ///////////////////////////////////////////////////////////////////////////
    // Light or Dark StatusBar/NavigationBar
    ///////////////////////////////////////////////////////////////////////////

    public static void setLightStatusBar(Window window, boolean lightStatusBar) {
        WindowInsetsControllerCompat windowInsetController = WindowCompat.getInsetsController(window, window.getDecorView());
        windowInsetController.setAppearanceLightStatusBars(lightStatusBar);
    }

    public static void setLightStatusBar(Activity activity, boolean lightStatusBar) {
        setLightStatusBar(activity.getWindow(), lightStatusBar);
    }

    public static void setLightNavigationBar(Window window, boolean lightNavigationBar) {
        WindowInsetsControllerCompat windowInsetController = WindowCompat.getInsetsController(window, window.getDecorView());
        windowInsetController.setAppearanceLightNavigationBars(lightNavigationBar);
    }

    public static void setLightNavigationBar(Activity activity, boolean lightNavigationBar) {
        setLightNavigationBar(activity.getWindow(), lightNavigationBar);
    }

}