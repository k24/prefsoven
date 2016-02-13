package com.github.k24.prefsoven.field;

import org.androidannotations.api.sharedpreferences.IntPrefField;

/**
 * Created by k24 on 2015/12/26.
 */
public class IntPref extends AbstractPref<Integer> {
    public IntPref(IntPrefField field) {
        super(field);
    }
}
