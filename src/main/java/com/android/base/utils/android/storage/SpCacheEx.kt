package com.android.base.utils.android.storage

import android.content.SharedPreferences.Editor

fun SpCache.commit(block: Editor.() -> Unit) {
    editor().apply {
        block()
        commit()
    }
}

fun SpCache.apply(block: Editor.() -> Unit) {
    editor().apply {
        block()
        apply()
    }
}