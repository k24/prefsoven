package com.github.k24.prefsoven.custom;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.github.k24.prefsoven.factory.AbstractElementFactory;
import com.github.k24.prefsoven.field.AbstractPref;
import com.github.k24.prefsoven.store.Element;

/**
 * Created by k24 on 2016/02/13.
 */
public class CustomElementFactory extends AbstractElementFactory {

    @NonNull
    @Override
    public Element<?> createElement(Class<?> elementClass, String key, Resources res, int defaultId) {
        return new DoubleElement(key, CustomFieldFactory.getDoubleDefaultValue(res, defaultId));
    }

    @NonNull
    @Override
    public AbstractPref<?> createPref(SharedPreferences prefs, String key, Object defaultValue, Class<?> typeClass) {
        return CustomFieldFactory.doublePref(prefs, key, (Double) defaultValue);
    }
}
