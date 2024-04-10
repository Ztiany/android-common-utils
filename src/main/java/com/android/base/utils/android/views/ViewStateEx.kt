@file:JvmName("ViewStateEx")

package com.android.base.utils.android.views

import android.view.View

fun View.beVisibleOrGone(visible: Boolean) {
    if (visible) {
        beVisible()
    } else {
        beGone()
    }
}

fun View.beVisibleOrInvisible(visible: Boolean) {
    if (visible) {
        beVisible()
    } else {
        beInvisible()
    }
}

fun View.beVisible() {
    this.visibility = View.VISIBLE
}

fun View.beInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.beGone() {
    this.visibility = View.GONE
}

fun View.isVisible() = this.visibility == View.VISIBLE

fun View.isInvisible() = this.visibility == View.INVISIBLE

fun View.isGone() = this.visibility == View.GONE

private const val ACTION_VISIBLE = 0x01
private const val ACTION_GONE = 0x02
private const val ACTION_INVISIBLE = 0x03
private const val ACTION_DISABLE = 0x04
private const val ACTION_ENABLE = 0x05

fun disableViews(view1: View, view2: View) {
    view1.isEnabled = false
    view2.isEnabled = false
}

fun disableViews(view1: View, view2: View, view3: View) {
    view1.isEnabled = false
    view2.isEnabled = false
    view3.isEnabled = false
}

fun disableViews(view1: View, view2: View, view3: View, vararg views: View) {
    view1.isEnabled = false
    view2.isEnabled = false
    view3.isEnabled = false
    setViewsState(ACTION_DISABLE, *views)
}

fun enableViews(view1: View, view2: View) {
    view1.isEnabled = true
    view2.isEnabled = true
}

fun enableViews(view1: View, view2: View, view3: View) {
    view1.isEnabled = true
    view2.isEnabled = true
    view3.isEnabled = true
}

fun enableViews(view1: View, view2: View, view3: View, vararg views: View) {
    view1.isEnabled = true
    view2.isEnabled = true
    view3.isEnabled = true
    setViewsState(ACTION_ENABLE, *views)
}

fun setViewsGone(view1: View, view2: View) {
    view1.visibility = View.GONE
    view2.visibility = View.GONE
}

fun setViewsGone(view1: View, view2: View, view3: View) {
    view1.visibility = View.GONE
    view2.visibility = View.GONE
    view3.visibility = View.GONE
}

fun setViewsGone(view1: View, view2: View, view3: View, vararg views: View) {
    view1.visibility = View.GONE
    view2.visibility = View.GONE
    view3.visibility = View.GONE
    setViewsState(ACTION_GONE, *views)
}

fun setViewsVisible(view1: View, view2: View) {
    view1.visibility = View.VISIBLE
    view2.visibility = View.VISIBLE
}

fun setViewsVisible(view1: View, view2: View, view3: View) {
    view1.visibility = View.VISIBLE
    view2.visibility = View.VISIBLE
    view3.visibility = View.VISIBLE
}

fun setViewsVisible(view1: View, view2: View, view3: View, vararg views: View) {
    view1.visibility = View.VISIBLE
    view2.visibility = View.VISIBLE
    view3.visibility = View.VISIBLE
    setViewsState(ACTION_VISIBLE, *views)
}

fun setViewsInvisible(view1: View, view2: View) {
    view1.visibility = View.INVISIBLE
    view2.visibility = View.INVISIBLE
}

fun setViewsInvisible(view1: View, view2: View, view3: View) {
    view1.visibility = View.INVISIBLE
    view2.visibility = View.INVISIBLE
    view3.visibility = View.INVISIBLE
}

fun setViewsInvisible(view1: View, view2: View, view3: View, vararg views: View) {
    view1.visibility = View.INVISIBLE
    view2.visibility = View.INVISIBLE
    view3.visibility = View.INVISIBLE
    setViewsState(ACTION_INVISIBLE, *views)
}

private fun setViewsState(action: Int, vararg views: View) {
    for (view in views) {
        when (action) {
            ACTION_GONE -> view.visibility = View.GONE
            ACTION_INVISIBLE -> view.visibility = View.INVISIBLE
            ACTION_VISIBLE -> view.visibility = View.VISIBLE
            ACTION_ENABLE -> view.isEnabled = true
            ACTION_DISABLE -> view.isEnabled = false
        }
    }
}