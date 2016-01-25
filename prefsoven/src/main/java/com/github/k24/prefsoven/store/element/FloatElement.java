package com.github.k24.prefsoven.store.element;

import android.support.annotation.Nullable;

import com.github.k24.prefsoven.store.Element;

/**
 * Created by k24 on 2016/01/01.
 */
public class FloatElement extends Element<Float> {
    public static final Float PROTOTYPE_VALUE = 0.f;
    public FloatElement(String name, @Nullable Float defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected Class<Float> getTypeClass() {
        return Float.class;
    }
}
