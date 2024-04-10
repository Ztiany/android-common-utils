@file:JvmName("ViewInteractionEx")

package com.android.base.utils.android.views

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

fun View.onDebouncedClick(onClick: (View) -> Unit) {
    setOnClickListener {
        if (!AntiShakeUtil.isInvalidClick(this)) {
            onClick(this)
        }
    }
}

fun View.onDebouncedClick(milliseconds: Long, onClick: (View) -> Unit) {
    setOnClickListener {
        if (!AntiShakeUtil.isInvalidClick(this, milliseconds)) {
            onClick(this)
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