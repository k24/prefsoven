package com.github.k24.prefsovensample.prefs;

import com.github.k24.prefsoven.PrefsOven;
import com.github.k24.prefsoven.field.LongPref;
import com.github.k24.prefsoven.field.StringPref;

/**
 * Created by k24 on 2016/01/17.
 */
public interface LastUpdatedOven extends PrefsOven {
    LongPref updatedAt();

    StringPref summary();
}
