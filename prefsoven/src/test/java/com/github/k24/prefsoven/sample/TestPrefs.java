package com.github.k24.prefsoven.sample;

import com.github.k24.prefsoven.PrefsOven;
import com.github.k24.prefsoven.field.BooleanPref;
import com.github.k24.prefsoven.field.FloatPref;
import com.github.k24.prefsoven.field.IntPref;
import com.github.k24.prefsoven.field.LongPref;
import com.github.k24.prefsoven.field.StringPref;
import com.github.k24.prefsoven.field.StringSetPref;

/**
 * Created by k24 on 2016/01/15.
 */
public interface TestPrefs extends PrefsOven {
    IntPref int1();

    FloatPref float1();

    LongPref long1();

    BooleanPref boolean1();

    StringPref string1();

    StringSetPref stringSet1();
}
