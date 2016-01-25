package com.github.k24.prefsoven.store;

import android.content.SharedPreferences;

import com.github.k24.prefsoven.field.AbstractOvenPrefField;

/**
 * Created by k24 on 2016/01/14.
 */
public interface PrefFieldFactory {
    <T> AbstractOvenPrefField<T> createField(String keyStirng, T defaultValue, Class<T> typeClass);

    SharedPreferences getPrefs();
}
