package com.github.k24.prefsoven.field;

import org.androidannotations.api.sharedpreferences.BooleanPrefField;

/**
 * Created by k24 on 2015/12/27.
 */
public class BooleanPref extends AbstractPref<Boolean> {
    public BooleanPref(BooleanPrefField field) {
        super(field);
    }
}
