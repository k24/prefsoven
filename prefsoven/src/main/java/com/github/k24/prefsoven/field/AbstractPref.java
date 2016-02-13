package com.github.k24.prefsoven.field;

import org.androidannotations.api.sharedpreferences.AbstractPrefField;

/**
 * Created by k24 on 2015/12/26.
 */
public abstract class AbstractPref<T> {
    private final AbstractPrefField<T> field;

    protected AbstractPref(AbstractPrefField<T> field) {
        this.field = field;
    }

    public final T getOr(T defaultValue) {
        return field.getOr(defaultValue);
    }

    public final boolean exists() {
        return field.exists();
    }

    public String key() {
        return field.key();
    }

    public final T get() {
        return field.get();
    }

    public final void put(T value) {
        field.put(value);
    }

    public final void remove() {
        field.remove();
    }
}
