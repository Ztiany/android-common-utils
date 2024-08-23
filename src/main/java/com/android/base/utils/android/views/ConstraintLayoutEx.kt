package com.android.base.utils.android.views

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

fun ConstraintLayout.applyNewConstraintSet(block: ConstraintSet.() -> Unit) {
    ConstraintSet().apply {
        clone(this@applyNewConstraintSet)
        block()
    }.applyTo(this)
}