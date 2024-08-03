package com.android.base.utils.coroutine

import java.lang.ref.WeakReference
import java.util.concurrent.CancellationException
import kotlin.coroutines.intrinsics.intercepted
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

class Ref<out T : Any> internal constructor(obj: T) {

    private val weakRef = WeakReference(obj)

    suspend operator fun invoke(): T {
        return suspendCoroutineUninterceptedOrReturn {
            it.intercepted()
            weakRef.get() ?: throw CancellationException()
        }
    }
    
}

fun <T : Any> T.asReference() = Ref(this)
