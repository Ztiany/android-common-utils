package com.android.base.utils.common

import java.util.concurrent.atomic.AtomicBoolean
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/** Returning the [initValue] at first access, then returning the opposite of the [initValue] after. */
fun onceBoolean(initValue: Boolean = false): ReadOnlyProperty<Any, Boolean> = OnceBooleanDelegate(initValue)

internal class OnceBooleanDelegate(initValue: Boolean) : ReadOnlyProperty<Any, Boolean> {

    private var value = initValue

    private val called = AtomicBoolean(false)

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        val returnValue = value
        if (called.compareAndSet(false, true)) {
            value = !returnValue
        }
        return returnValue
    }

}