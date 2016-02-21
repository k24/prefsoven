package com.github.k24.prefsoven.field;

import android.text.TextUtils;

import org.androidannotations.api.sharedpreferences.StringPrefField;

/**
 * Created by k24 on 2015/12/27.
 */
public class StringPref extends AbstractPref<String> {
    public StringPref(StringPrefField field) {
        super(field);
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(get());
    }

    public String add(String adding) {
        String value = get();
        if (adding == null) return value;
        value = value == null ? adding : value + adding;
        put(value);
        return value;
    }
}
