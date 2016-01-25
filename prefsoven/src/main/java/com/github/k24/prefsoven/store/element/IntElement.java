package com.github.k24.prefsoven.store.element;

import android.support.annotation.Nullable;

import com.github.k24.prefsoven.store.Element;

/**
 * Created by k24 on 2015/12/31.
 */
public class IntElement extends Element<Integer> {
    public static final Integer PROTOTYPE_VALUE = 0;
    public IntElement(String name, @Nullable Integer defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected Class<Integer> getTypeClass() {
        return Integer.class;
    }
}
