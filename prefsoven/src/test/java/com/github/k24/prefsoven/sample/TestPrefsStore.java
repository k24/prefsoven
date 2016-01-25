package com.github.k24.prefsoven.sample;

import com.github.k24.prefsoven.PrefsStoreOven;
import com.github.k24.prefsoven.store.element.BooleanElement;
import com.github.k24.prefsoven.store.element.FloatElement;
import com.github.k24.prefsoven.store.element.IntElement;
import com.github.k24.prefsoven.store.element.LongElement;
import com.github.k24.prefsoven.store.element.StringElement;
import com.github.k24.prefsoven.store.element.StringSetElement;

/**
 * Created by k24 on 2016/01/15.
 */
public interface TestPrefsStore extends PrefsStoreOven {
    IntElement int1();

    FloatElement float1();

    LongElement long1();

    BooleanElement boolean1();

    StringElement string1();

    StringSetElement stringSet1();
}
