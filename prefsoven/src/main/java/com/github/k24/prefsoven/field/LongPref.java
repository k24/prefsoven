package com.github.k24.prefsoven.field;

import org.androidannotations.api.sharedpreferences.LongPrefField;

/**
 * Created by k24 on 2015/12/27.
 */
public class LongPref extends AbstractPref<Long> {
    public LongPref(LongPrefField field) {
        super(field);
    }

    public long increment() {
        long value = get();
        if (value == Long.MAX_VALUE) return Long.MAX_VALUE;
        put(++value);
        return value;
    }

    public long decrement() {
        long value = get();
        if (value == Long.MIN_VALUE) return Long.MIN_VALUE;
        put(--value);
        return value;
    }

    public long add(long adding) {
        long value = get();
        if (adding == 0) return value;
        if (adding > 0 && Long.MAX_VALUE - adding < value) return Long.MAX_VALUE;
        if (adding < 0 && Long.MIN_VALUE - adding > value) return Long.MIN_VALUE;
        value += adding;
        put(value);
        return value;
    }
}
