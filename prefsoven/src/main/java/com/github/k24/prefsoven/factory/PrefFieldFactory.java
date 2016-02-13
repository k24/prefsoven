package com.github.k24.prefsoven.factory;

import android.support.annotation.NonNull;

import com.github.k24.prefsoven.field.AbstractOvenPrefField;

/**
 * Created by k24 on 2016/01/14.
 */
public interface PrefFieldFactory {
    @NonNull
    <T> AbstractOvenPrefField<T> createField(String keyStirng, T defaultValue, Class<T> typeClass);
}
