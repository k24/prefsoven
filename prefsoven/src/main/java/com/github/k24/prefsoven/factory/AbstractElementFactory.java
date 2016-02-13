package com.github.k24.prefsoven.factory;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.github.k24.prefsoven.field.AbstractOvenPrefField;
import com.github.k24.prefsoven.store.Element;

/**
 * Created by k24 on 2016/02/13.
 */
public abstract class AbstractElementFactory {
    @NonNull
    public abstract Element<?> createElement(Class<?> elementClass, String key, Resources res, int defaultId);

    @NonNull
    public abstract AbstractOvenPrefField<?> createPref(SharedPreferences prefs, String key, Object defaultValue, Class<?> typeClass);
}
