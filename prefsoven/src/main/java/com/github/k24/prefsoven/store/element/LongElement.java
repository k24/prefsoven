package com.github.k24.prefsoven.store.element;

import android.support.annotation.Nullable;

import com.github.k24.prefsoven.store.Element;

/**
 * Created by k24 on 2016/01/01.
 */
public class LongElement extends Element<Long> {
    public static final Long PROTOTYPE_VALUE = 0L;
    public LongElement(String name, @Nullable Long defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected Class<Long> getTypeClass() {
        return Long.class;
    }
}
