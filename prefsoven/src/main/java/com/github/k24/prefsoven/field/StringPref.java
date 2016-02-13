package com.github.k24.prefsoven.field;

import org.androidannotations.api.sharedpreferences.StringPrefField;

/**
 * Created by k24 on 2015/12/27.
 */
public class StringPref extends AbstractPref<String> {
    public StringPref(StringPrefField field) {
        super(field);
    }
}
