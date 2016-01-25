package com.github.k24.prefsoven.store.element;

import android.support.annotation.Nullable;

import com.github.k24.prefsoven.store.Element;

import java.util.Collections;
import java.util.Set;

/**
 * Created by k24 on 2016/01/01.
 */
public class StringSetElement extends Element<Set> {
    public static final Set<String> PROTOTYPE_VALUE = Collections.emptySet();
    public StringSetElement(String name, @Nullable Set<String> defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected Class<Set> getTypeClass() {
        return Set.class;
    }
}
