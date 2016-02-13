package com.github.k24.prefsoven.custom;

import android.net.Uri;

import com.github.k24.prefsoven.field.AbstractOvenPrefField;

import org.androidannotations.api.sharedpreferences.AbstractPrefField;

/**
 * Created by k24 on 2016/02/13.
 */
public class DoublePref extends AbstractOvenPrefField<Double> {
    protected DoublePref(AbstractPrefField<Double> field) {
        super(field);
    }

}
