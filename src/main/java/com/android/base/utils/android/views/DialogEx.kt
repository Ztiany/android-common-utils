@file:JvmName("DialogEx")

package com.android.base.utils.android.views

import android.app.Dialog
import androidx.fragment.app.FragmentActivity
import com.android.base.utils.android.activityContext

fun Dialog.notCancelable(): Dialog {
    this.setCancelable(false)
    return this
}

fun Dialog.notCanceledOnTouchOutside(): Dialog {
    this.setCanceledOnTouchOutside(false)
    return this
}

fun Dialog.onDismiss(action: () -> Unit): Dialog {
    setOnDismissListener {
        action()
    }
    return this
}

val Dialog.activityContext: FragmentActivity?
    get() {
        return context.activityContext
    }