package com.android.base.utils.android.views

import android.view.View
import android.widget.CompoundButton
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.otherwise
import timber.log.Timber

/** TODO: configurable behaviors like making crash when [View.getTag] returning a null value. */
@Suppress("UNCHECKED_CAST")
fun <T> newOnItemClickListener(listener: (view: View, item: T) -> Unit): View.OnClickListener {
    return View.OnClickListener {
        (it.tag as? T).ifNonNull {
            listener(it, this)
        } otherwise {
            Timber.e("the view $it has not tag set.")
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> newCheckedChangeListener(listener: (buttonView: CompoundButton, isChecked: Boolean, item: T) -> Unit): CompoundButton.OnCheckedChangeListener {
    return CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        (buttonView.tag as? T).ifNonNull {
            listener(buttonView, isChecked, this)
        } otherwise {
            Timber.e("the view $buttonView has not tag set.")
        }
    }
}