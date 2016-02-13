package com.github.k24.prefsoven.factory;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import com.github.k24.prefsoven.field.AbstractOvenPrefField;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by k24 on 2016/02/13.
 */
public abstract class PrefFieldFactory {
    @NonNull
    public abstract AbstractOvenPrefField<?> createField(Context context, SharedPreferences prefs, Class<?> type, String key, int defaultResId);

    public static int getIntDefaultValue(Resources res, int defaultResId) {
        return defaultResId == 0 ? 0 : res.getInteger(defaultResId);
    }

    public static float getFloatDefaultValue(Resources res, int defaultResId) {
        if (defaultResId == 0) return 0;
        TypedValue typedValue = new TypedValue();
        res.getValue(defaultResId, typedValue, true);
        return typedValue.getFloat();
    }

    public static long getLongDefaultValue(Resources res, int defaultResId) {
        if (defaultResId == 0) return 0;
        TypedValue typedValue = new TypedValue();
        res.getValue(defaultResId, typedValue, true);
        switch (typedValue.type) {
            case TypedValue.TYPE_STRING:
                return Long.parseLong(typedValue.string.toString());
            default:
                return typedValue.data;
        }
    }

    public static boolean getBooleanDefaultValue(Resources res, int defaultResId) {
        if (defaultResId == 0) return false;
        TypedValue typedValue = new TypedValue();
        res.getValue(defaultResId, typedValue, true);
        switch (typedValue.type) {
            case TypedValue.TYPE_STRING:
                return Boolean.parseBoolean(typedValue.string.toString());
            default:
                return typedValue.data != 0;
        }
    }

    public static String getStringDefaultValue(Resources res, int defaultResId) {
        if (defaultResId == 0) return null;
        return res.getString(defaultResId);
    }

    public static Set<String> getStringSetDefaultValue(Resources res, int defaultResId) {
        if (defaultResId == 0) return null;
        return new LinkedHashSet<>(Arrays.asList(res.getStringArray(defaultResId)));
    }
}
