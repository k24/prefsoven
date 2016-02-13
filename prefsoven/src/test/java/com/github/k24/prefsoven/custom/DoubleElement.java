package com.github.k24.prefsoven.custom;

import android.support.annotation.Nullable;

import com.github.k24.prefsoven.store.Element;

/**
 * Created by k24 on 2016/02/13.
 */
public class DoubleElement extends Element<Double> {
    public DoubleElement(String name, @Nullable Double defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected Class<Double> getTypeClass() {
        return Double.class;
    }
}
