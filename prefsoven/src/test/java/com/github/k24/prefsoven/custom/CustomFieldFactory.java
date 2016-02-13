package com.github.k24.prefsoven.custom;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import com.github.k24.prefsoven.factory.AbstractFieldFactory;
import com.github.k24.prefsoven.field.AbstractPref;

import org.androidannotations.api.sharedpreferences.AbstractPrefField;
import org.androidannotations.api.sharedpreferences.SharedPreferencesCompat;

/**
 * Created by k24 on 2016/02/13.
 */
public class CustomFieldFactory extends AbstractFieldFactory {

    @NonNull
    @Override
    public AbstractPref<?> createPref(Context context, final SharedPreferences prefs, Class<?> type, String key, int defaultResId) {
        return doublePref(prefs, key, getDoubleDefaultValue(context.getResources(), defaultResId));
    }

    static DoublePref doublePref(SharedPreferences prefs, String key, Double defaultValue) {
        return new DoublePref(new AbstractPrefField<Double>(prefs, key, defaultValue) {
            @Override
            public Double getOr(Double aDouble) {
                return Double.valueOf(sharedPreferences.getString(key, String.valueOf(aDouble)));
            }

            @Override
            protected void putInternal(Double aDouble) {
                SharedPreferencesCompat.apply(sharedPreferences.edit().putString(key, String.valueOf(aDouble)));
            }
        });
    }

    static double getDoubleDefaultValue(Resources res, int defaultResId) {
        if (defaultResId == 0) return 0;
        TypedValue typedValue = new TypedValue();
        res.getValue(defaultResId, typedValue, true);
        switch (typedValue.type) {
            case TypedValue.TYPE_STRING:
                return Double.parseDouble(typedValue.string.toString());
            case TypedValue.TYPE_FLOAT:
                return typedValue.getFloat();
            case TypedValue.TYPE_INT_DEC:
            case TypedValue.TYPE_INT_HEX:
            default:
                return typedValue.data;
        }
    }
}
