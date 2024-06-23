@file:JvmName("ViewInteractionEx")

package com.android.base.utils.android.views

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import com.android.base.utils.R

internal var viewClickThrottledTime = 200L
internal var viewClickDebouncedTime = 200L

fun View.onThrottledClickClick(milliseconds: Long = viewClickThrottledTime, onClick: (View) -> Unit) {
    setOnClickListener {
        val currentTimeStamp = System.currentTimeMillis()
        val lastClickTimeStamp = getTag(R.id.base_last_click_timestamp) as? Long ?: 0
        if (currentTimeStamp - lastClickTimeStamp >= milliseconds) {
            setTag(R.id.base_last_click_timestamp, currentTimeStamp)
            onClick(this)
        }
    }
}

fun View.onDebouncedClickClick(wait: Long = viewClickDebouncedTime, onClick: (View) -> Unit) {
    setOnClickListener {
        var action = (getTag(R.id.base_click_debounce_action) as? DebounceAction)
        if (action == null) {
            action = DebounceAction(this, onClick)
            setTag(R.id.base_click_debounce_action, action)
        } else {
            action.block = onClick
        }
        removeCallbacks(action)
        postDelayed(action, wait)
    }
}

private class DebounceAction(val view: View, var block: ((View) -> Unit)) : Runnable {
    override fun run() {
        if (view.isAttachedToWindow) {
            block(view)
        }
    }
}

@SuppressLint("ClickableViewAccessibility")
fun View.setClickFeedback(pressAlpha: Float = 0.5F) {
    this.setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.alpha = pressAlpha
            }

            MotionEvent.ACTION_UP -> {
                v.alpha = 1F
            }

            MotionEvent.ACTION_CANCEL -> {
                v.alpha = 1F
            }
        }
        false
    }
}