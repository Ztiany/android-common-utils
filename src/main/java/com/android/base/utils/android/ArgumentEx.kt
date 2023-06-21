package com.android.base.utils.android

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/* modified from https://github.com/pengxurui/DemoHall */

/** 请确保 T 是 Fragment 所支持的类型。 */
fun <T> Fragment.requireArgument(name: String, defaultValue: T? = null): T {
    @Suppress("UNCHECKED_CAST")
    return arguments?.get(name) as? T ?: defaultValue ?: throw IllegalStateException("Property $name not exists.")
}

/** 请确保 T 是 Fragment 所支持的类型。 */
fun <T> Fragment.getArgument(name: String): T? {
    val arguments = arguments ?: return null
    @Suppress("UNCHECKED_CAST")
    return arguments.get(name) as? T
}

/** 请确保 T 是 Fragment 所支持的类型。 */
fun <T> Fragment.argumentNullable(name: String? = null) = FragmentArgumentPropertyNullable<T>(name)

/** 请确保 T 是 Fragment 所支持的类型。 */
fun <T> Fragment.argument(
    name: String? = null,
    defaultValue: T? = null
) = FragmentArgumentProperty(name, defaultValue)

// --------------------------------------------------------------------------------------
// Fragment
// --------------------------------------------------------------------------------------

class FragmentArgumentProperty<T>(
    /**value name*/
    private val name: String? = null,
    /**default cannot be null*/
    private val defaultValue: T? = null
) : ReadOnlyProperty<Fragment, T> {

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return thisRef.arguments?.getValue(confirmPropertyName(name, property)) as? T
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

class FragmentArgumentPropertyNullable<T>(
    private val name: String? = null
) : ReadOnlyProperty<Fragment, T?> {

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T? {
        return thisRef.arguments?.getValue(confirmPropertyName(name, property))
    }

}

// --------------------------------------------------------------------------------------
// Activity
// --------------------------------------------------------------------------------------

fun <T> Activity.argumentNullable(name: String? = null) = ActivityArgumentDelegateNullable<T>()

fun <T> Activity.argument(
    name: String? = null,
    defaultValue: T? = null
) = ActivityArgumentProperty(name, defaultValue)

class ActivityArgumentProperty<T>(
    private val name: String? = null,
    private val defaultValue: T? = null
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

class ActivityArgumentDelegateNullable<T>(
    private val name: String? = null
) : ReadOnlyProperty<Activity, T?> {

    override fun getValue(thisRef: Activity, property: KProperty<*>): T? {
        return thisRef.intent?.extras?.getValue(confirmPropertyName(name, property))
    }

}

// --------------------------------------------------------------------------------------
// Utils
// --------------------------------------------------------------------------------------

private fun confirmPropertyName(name: String?, property: KProperty<*>): String {
    return name ?: property.name
}

fun <T> Bundle.getValue(key: String): T? {
    @Suppress("UNCHECKED_CAST")
    return get(key) as T?
}