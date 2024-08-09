package com.android.base.utils.android.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Set;

public class SpCache {

    private final SharedPreferences mSharedPreferences;

    private final boolean mCommitImmediately;

    public SpCache(Context context, String prefFileName) {
        this(context, prefFileName, false);
    }

    public SpCache(Context context, String prefFileName, boolean commitImmediately) {
        if (TextUtils.isEmpty(prefFileName)) {
            throw new NullPointerException("SpCache get fileName = null");
        }
        mSharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        mCommitImmediately = commitImmediately;
    }

    ///////////////////////////////////////////////////////////////////////////
    // put
    ///////////////////////////////////////////////////////////////////////////

    public void putInt(String key, int val) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putInt(key, val);
        apply(edit);
    }

    public void putLong(String key, long val) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putLong(key, val);
        apply(edit);
    }

    public void putString(String key, String val) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(key, val);
        apply(edit);
    }

    public void putBoolean(String key, boolean val) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putBoolean(key, val);
        apply(edit);
    }

    public void putFloat(String key, float val) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putFloat(key, val);
        apply(edit);
    }

    public void putStringSet(String key, Set<String> val) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putStringSet(key, val);
        apply(edit);
    }

    ///////////////////////////////////////////////////////////////////////////
    // get
    ///////////////////////////////////////////////////////////////////////////

    public int getInt(String key, int defaultVal) {
        return mSharedPreferences.getInt(key, defaultVal);
    }

    public long getLong(String key, long defaultVal) {
        return mSharedPreferences.getLong(key, defaultVal);
    }

    public String getString(String key, String defaultVal) {
        return mSharedPreferences.getString(key, defaultVal);
    }

    public boolean getBoolean(String key, boolean defaultVal) {
        return mSharedPreferences.getBoolean(key, defaultVal);
    }

    public float getFloat(String key, float defaultVal) {
        return mSharedPreferences.getFloat(key, defaultVal);
    }

    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return mSharedPreferences.getStringSet(key, defaultValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // other
    ///////////////////////////////////////////////////////////////////////////

    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        apply(editor);
    }

    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        apply(editor);
    }

    private void apply(SharedPreferences.Editor editor) {
        if (mCommitImmediately) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public SharedPreferences.Editor editor() {
        return mSharedPreferences.edit();
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

}