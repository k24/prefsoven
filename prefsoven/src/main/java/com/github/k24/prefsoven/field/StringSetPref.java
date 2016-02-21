package com.github.k24.prefsoven.field;

import org.androidannotations.api.sharedpreferences.StringSetPrefField;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by k24 on 2015/12/27.
 */
public class StringSetPref extends AbstractPref<Set<String>> {
    public StringSetPref(StringSetPrefField field) {
        super(field);
    }

    public boolean isEmpty() {
        Set<String> strings = get();
        return strings == null || strings.isEmpty();
    }

    public Set<String> add(String adding) {
        Set<String> strings = get();
        if (strings == null) {
            strings = Collections.singleton(adding);
        } else {
            strings = new LinkedHashSet<>(strings);
            strings.add(adding);
        }
        put(strings);
        return strings;
    }

    public Set<String> remove(String removing) {
        Set<String> strings = get();
        if (strings == null) {
            return null;
        } else {
            if (!strings.contains(removing)) {
                return strings;
            }
            strings = new LinkedHashSet<>(strings);
            strings.remove(removing);
        }
        put(strings);
        return strings;
    }
}
