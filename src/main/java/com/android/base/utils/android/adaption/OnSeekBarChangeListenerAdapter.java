package com.android.base.utils.android.adaption;

import android.widget.SeekBar;

/**
 * @author Ztiany
 */
public interface OnSeekBarChangeListenerAdapter extends SeekBar.OnSeekBarChangeListener {

    @Override
    default void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    default void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    default void onStopTrackingTouch(SeekBar seekBar) {
    }

}
