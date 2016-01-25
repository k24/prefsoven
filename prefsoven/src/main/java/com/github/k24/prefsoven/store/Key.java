package com.github.k24.prefsoven.store;

import android.support.annotation.NonNull;

import com.github.k24.prefsoven.field.AbstractOvenPrefField;

/**
 * Created by k24 on 2015/12/30.
 */
public class Key<T> {
    @NonNull
    private final Model model;
    @NonNull
    private final String keyStirng;
    @NonNull
    private final Pid pid;
    @NonNull
    private final Element<T> element;

    private AbstractOvenPrefField<T> value;

    private volatile boolean removed;

    public Key(@NonNull Model model, @NonNull String keyStirng, @NonNull Pid pid, @NonNull Element<T> element) {
        this.model = model;
        this.keyStirng = keyStirng;
        this.pid = pid;
        this.element = element;
    }

    public Pid pid() {
        return pid;
    }

    public Element<T> element() {
        return element;
    }

    public synchronized AbstractOvenPrefField<T> value() {
        if (value == null) {
            value = model.getPrefFieldFactory().createField(keyStirng, element.defaultValue(), element.getTypeClass());
        }
        return value;
    }

    void clear() {
        element.clearKeys();
        removed = true;
    }
}
