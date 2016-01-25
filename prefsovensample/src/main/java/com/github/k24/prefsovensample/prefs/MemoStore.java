package com.github.k24.prefsovensample.prefs;

import com.github.k24.prefsoven.PrefsStoreOven;
import com.github.k24.prefsoven.store.element.StringElement;

/**
 * Created by k24 on 2016/01/17.
 */
public interface MemoStore extends PrefsStoreOven {
    StringElement subject();

    StringElement body();
}
