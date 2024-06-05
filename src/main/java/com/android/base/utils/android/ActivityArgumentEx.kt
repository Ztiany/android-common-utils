package com.android.base.utils.android

import android.app.Activity
import android.os.Bundle
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/** 请确保 T 是 Bundle 所支持的类型。 */
fun <T> activityArgument(
    name: String? = null,
    defaultValue: T? = null,
): ReadOnlyProperty<Activity, T> = ActivityArgumentProperty(name, defaultValue)

/** 请确保 T 是 Bundle 所支持的类型。 */
fun <T> activityArgumentNullable(name: String? = null): ReadOnlyProperty<Activity, T?> = ActivityArgumentDelegateNullable<T>(name)

private class ActivityArgumentProperty<T>(
    private val name: String? = null,
    private val defaultValue: T? = null,
) : ReadOnlyProperty<Activity, T> {

    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return thisRef.intent?.extras?.getValue(confirmPropertyName(name, property)) as? T
            ?: defaultValue
            ?: throw IllegalStateException(
                "Property ${
                    confirmPropertyName(
                        name,
                        property
                    )
                } could not be read"
            )
    }
}

private class ActivityArgumentDelegateNullable<T>(private val name: String? = null) : ReadOnlyProperty<Activity, T?> {

    override fun getValue(thisRef: Activity, property: KProperty<*>): T? {
        return thisRef.intent?.extras?.getValue(confirmPropertyName(name, property))
    }

}

private fun confirmPropertyName(name: String?, property: KProperty<*>): String {
    return name ?: property.name
}

private fun <T> Bundle.getValue(key: String): T? {
    @Suppress("UNCHECKED_CAST")
    return get(key) as T?
}