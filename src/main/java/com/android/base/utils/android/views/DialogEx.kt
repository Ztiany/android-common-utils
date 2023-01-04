package com.android.base.utils.android.views

import android.app.Dialog
import androidx.fragment.app.FragmentActivity

fun Dialog.notCancelable(): Dialog {
    this.setCancelable(false)
    return this
}

fun Dialog.onDismiss(action: () -> Unit): Dialog {
    setOnDismissListener {
        action()
    }
    return this
}

val Dialog.realContext: FragmentActivity?
    get() {
        var context = context
        while (context is android.content.ContextWrapper) {
            if (context is FragmentActivity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }