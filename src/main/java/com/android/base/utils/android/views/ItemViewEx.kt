package com.android.base.utils.android.views

import android.view.View
import android.widget.CompoundButton
import com.android.base.utils.R
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.otherwise
import timber.log.Timber

@Suppress("UNCHECKED_CAST")
fun <T> newOnItemClickListener(listener: (view: View, item: T) -> Unit): View.OnClickListener {
    return View.OnClickListener {
        (it.tag as? T).ifNonNull {
            listener(it, this)
        } otherwise {
            Timber.e("the view $it has not tag set.")
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> newThrottledOnItemClickListener(
    milliseconds: Long = viewClickThrottledTime,
    listener: (view: View, item: T) -> Unit
): View.OnClickListener {
    return View.OnClickListener {
        val currentTimeStamp = System.currentTimeMillis()
        val lastClickTimeStamp = it.getTag(R.id.base_last_click_timestamp) as? Long ?: 0
        if (currentTimeStamp - lastClickTimeStamp >= milliseconds) {
            it.setTag(R.id.base_last_click_timestamp, currentTimeStamp)
            (it.tag as? T).ifNonNull {
                listener(it, this)
            } otherwise {
                Timber.e("the view $it has not tag set.")
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> newDebouncedOnItemClickListener(
    wait: Long = viewClickDebouncedTime,
    listener: (view: View, item: T) -> Unit
): View.OnClickListener {
    return View.OnClickListener {
        var action = (it.getTag(R.id.base_click_debounce_action) as? ItemDebounceAction<T>)
        if (action == null) {
            action = ItemDebounceAction(it, listener)
            it.setTag(R.id.base_click_debounce_action, action)
        } else {
            action.block = listener
        }
        it.removeCallbacks(action)
        it.postDelayed(action, wait)
    }
}

@Suppress("UNCHECKED_CAST")
private class ItemDebounceAction<T>(val view: View, var block: ((View, T) -> Unit)) : Runnable {
    override fun run() {
        if (view.isAttachedToWindow) {
            (view.tag as? T) ifNonNull {
                block(view, this)
            } otherwise {
                Timber.e("the view $view has not tag set.")
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> newOnItemLongClickListener(listener: (view: View, item: T) -> Unit): View.OnLongClickListener {
    return View.OnLongClickListener {
        (it.tag as? T).ifNonNull {
            listener(it, this)
        } otherwise {
            Timber.e("the view $it has not tag set.")
        }
        true
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> newCheckedChangeListener(listener: (buttonView: CompoundButton, isChecked: Boolean, item: T) -> Unit): CompoundButton.OnCheckedChangeListener {
    return CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        (buttonView.tag as? T).ifNonNull {
            listener(buttonView, isChecked, this)
        } otherwise {
            Timber.e("the view $buttonView has not tag set.")
        }
    }
}