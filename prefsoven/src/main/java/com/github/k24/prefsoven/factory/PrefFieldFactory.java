package com.github.k24.prefsoven.factory;

import android.support.annotation.NonNull;

import com.github.k24.prefsoven.field.AbstractPref;

/**
 * Created by k24 on 2016/01/14.
 */
public interface PrefFieldFactory {
    @NonNull
    <T> AbstractPref<T> createPref(String keyStirng, T defaultValue, Class<T> typeClass);
}
