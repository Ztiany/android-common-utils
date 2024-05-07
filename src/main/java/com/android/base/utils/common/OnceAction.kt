package com.android.base.utils.common

import kotlin.reflect.KProperty

private object UNDEFINED

/**
 * An once object makes sure code blocks are executed only once
 *
 * usage:
 *
 * ```
 * class Gretting {
 *
 *   val once = OnceAction<Unit>()
 *
 *   fun hello(name: String): Unit = once {
 *     println("Hello, ${name}")
 *   }
 *
 * }
 * ```
 *
 * check [johnsonlee/once](https://github.com/johnsonlee/once) for details.
 */
class OnceAction<T : Any> {

    @Volatile
    private var _value: Any? = UNDEFINED

    @Suppress("UNCHECKED_CAST")
    val value: T?
        get() = _value as? T

    @Suppress("UNCHECKED_CAST")
    operator fun invoke(action: () -> T): T {
        val v1 = _value
        if (v1 !== UNDEFINED) {
            return v1 as T
        }

        return synchronized(this) {
            val v2 = _value
            if (v2 !== UNDEFINED) {
                v2
            } else {
                val v3 = action()
                _value = v3
                v3
            }
        } as T
    }

}

operator fun <T : Any> OnceAction<T>.getValue(thisRef: Any?, property: KProperty<*>): T? = value