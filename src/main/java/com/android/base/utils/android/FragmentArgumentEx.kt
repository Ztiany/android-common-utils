package com.android.base.utils.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

///////////////////////////////////////////////////////////////////////////
// get directly
///////////////////////////////////////////////////////////////////////////

/** 请确保 T 是 Bundle 所支持的类型。 */
fun <T> Fragment.requireArgument(name: String): T {
    @Suppress("UNCHECKED_CAST")
    return arguments?.get(name) as? T ?: throw IllegalStateException("Property $name not exists.")
}

/** 请确保 T 是 Bundle 所支持的类型。 */
fun <T> Fragment.requireArgument(name: String, defaultValue: T): T {
    @Suppress("UNCHECKED_CAST")
    return arguments?.get(name) as? T ?: defaultValue
}

/** 请确保 T 是 Bundle 所支持的类型。 */
fun <T> Fragment.getArgument(name: String): T? {
    val arguments = arguments ?: return null
    @Suppress("UNCHECKED_CAST")
    return arguments.get(name) as? T
}

///////////////////////////////////////////////////////////////////////////
// by property delegate
///////////////////////////////////////////////////////////////////////////

/** 请确保 T 是 Bundle 所支持的类型。 */
fun <T> Fragment.argumentNullable(name: String? = null): ReadOnlyProperty<Fragment, T?> = FragmentArgumentPropertyNullable<T>(name)

/** 请确保 T 是 Bundle 所支持的类型。 */
fun <T> Fragment.argument(
    name: String? = null,
    defaultValue: T? = null,
): ReadOnlyProperty<Fragment, T> = FragmentArgumentProperty(name, defaultValue)

// --------------------------------------------------------------------------------------
// Fragment
// --------------------------------------------------------------------------------------

private class FragmentArgumentProperty<T>(
    /**value name*/
    private val name: String? = null,
    /**default cannot be null*/
    private val defaultValue: T? = null,
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

private class FragmentArgumentPropertyNullable<T>(
    private val name: String? = null,
) : ReadOnlyProperty<Fragment, T?> {

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T? {
        return thisRef.arguments?.getValue(confirmPropertyName(name, property))
    }

}

///////////////////////////////////////////////////////////////////////////
// utils
///////////////////////////////////////////////////////////////////////////

private fun confirmPropertyName(name: String?, property: KProperty<*>): String {
    return name ?: property.name
}

private fun <T> Bundle.getValue(key: String): T? {
    @Suppress("UNCHECKED_CAST")
    return get(key) as T?
}