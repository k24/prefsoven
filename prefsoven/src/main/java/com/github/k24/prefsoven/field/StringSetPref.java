package com.github.k24.prefsoven.field;

import org.androidannotations.api.sharedpreferences.StringSetPrefField;

import java.util.Set;

/**
 * Created by k24 on 2015/12/27.
 */
public class StringSetPref extends AbstractOvenPrefField<Set<String>> {
    public StringSetPref(StringSetPrefField field) {
        super(field);
    }
}
