@file:JvmName("SecurityEx")

package com.android.base.utils.security

import androidx.annotation.WorkerThread

@WorkerThread
fun md5(content: String): String {
    return MD5Utils.md5(content) ?: ""
}