package com.github.k24.prefsoven.store.element;

import android.support.annotation.Nullable;

import com.github.k24.prefsoven.store.Element;

/**
 * Created by k24 on 2016/01/01.
 */
public class BooleanElement extends Element<Boolean> {
    public static final Boolean PROTOTYPE_VALUE = false;
    public BooleanElement(String name, @Nullable Boolean defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected Class<Boolean> getTypeClass() {
        return Boolean.class;
    }

}
