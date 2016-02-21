package com.github.k24.prefsoven.field;

import org.androidannotations.api.sharedpreferences.IntPrefField;

/**
 * Created by k24 on 2015/12/26.
 */
public class IntPref extends AbstractPref<Integer> {
    public IntPref(IntPrefField field) {
        super(field);
    }

    public int increment() {
        int value = get();
        if (value == Integer.MAX_VALUE) return value;
        put(++value);
        return value;
    }

    public int decrement() {
        int value = get();
        if (value == Integer.MIN_VALUE) return value;
        put(--value);
        return value;
    }

    public int add(int adding) {
        int value = get();
        if (adding == 0) return value;
        if (adding > 0 && Integer.MAX_VALUE - adding < value) return Integer.MAX_VALUE;
        if (adding < 0 && Integer.MIN_VALUE - adding > value) return Integer.MIN_VALUE;
        value += adding;
        put(value);
        return value;
    }
}
