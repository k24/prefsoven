package com.github.k24.prefsoven.custom;

import com.github.k24.prefsoven.field.AbstractPref;

import org.androidannotations.api.sharedpreferences.AbstractPrefField;

/**
 * Created by k24 on 2016/02/13.
 */
public class DoublePref extends AbstractPref<Double> {
    protected DoublePref(AbstractPrefField<Double> field) {
        super(field);
    }

}
