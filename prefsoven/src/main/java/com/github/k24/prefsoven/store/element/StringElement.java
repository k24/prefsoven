package com.github.k24.prefsoven.store.element;

import android.support.annotation.Nullable;

import com.github.k24.prefsoven.store.Element;

/**
 * Created by k24 on 2016/01/01.
 */
public class StringElement extends Element<String> {
    public static final String PROTOTYPE_VALUE = "";
    public StringElement(String name, @Nullable String defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected Class<String> getTypeClass() {
        return String.class;
    }
}
