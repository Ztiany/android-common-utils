package com.android.base.utils.android.views

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

fun ConstraintLayout.newConstraintSet(block: ConstraintSet.() -> Unit) {
    ConstraintSet().apply {
        clone(this@newConstraintSet)
        block()
    }.applyTo(this)
}