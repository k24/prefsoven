package com.github.k24.prefsoven.field;

import org.androidannotations.api.sharedpreferences.FloatPrefField;

/**
 * Created by k24 on 2015/12/27.
 */
public class FloatPref extends AbstractPref<Float> {
    public FloatPref(FloatPrefField field) {
        super(field);
    }

    public float add(float adding) {
        float value = get();
        if (Float.isInfinite(adding) || Float.isNaN(adding)) return value;
        if (adding == 0) return value;
        if (adding > 0 && Float.MAX_VALUE - adding < value) return Float.MAX_VALUE;
        value += adding;
        put(value);
        return value;
    }
}
