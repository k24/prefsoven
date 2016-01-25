package com.github.k24.prefsoven.store;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.k24.prefsoven.field.AbstractOvenPrefField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by k24 on 2015/12/30.
 */
public abstract class Element<T> {
    private final String name;
    private final T defaultValue;
    private final List<Key<T>> keys = new ArrayList<>();

    public Element(String name, @Nullable T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String name() {
        return name;
    }

    public List<Key<T>> keys() {
        return Collections.unmodifiableList(keys);
    }

    @Nullable
    public Key<T> find(@NonNull T value) {
        // TODO More efficient
        for (Key<T> key : keys) {
            if (value.equals(key.value().get())) return key;
        }
        return null;
    }

    public T defaultValue() {
        return defaultValue;
    }

    protected abstract Class<T> getTypeClass();

    public List<AbstractOvenPrefField<T>> values() {
        ArrayList<AbstractOvenPrefField<T>> values = new ArrayList<>();
        for (Key<T> key : keys) {
            values.add(key.value());
        }
        return values;
    }

    @SuppressWarnings("unchecked")
    void addKey(Key<?> key) {
        keys.add((Key<T>) key);
    }

    void clearKeys() {
        keys.clear();
    }
}
