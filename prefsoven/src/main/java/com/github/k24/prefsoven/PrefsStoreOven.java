package com.github.k24.prefsoven;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.k24.prefsoven.store.Element;
import com.github.k24.prefsoven.store.Key;
import com.github.k24.prefsoven.store.Pid;

import java.util.List;

/**
 * Created by k24 on 2016/01/14.
 */
public interface PrefsStoreOven {
    void bake(@NonNull Pid pid, @NonNull Object object);

    @NonNull
    <T> T cook(@NonNull Pid pid, @NonNull Class<T> clazz);

    @NonNull
    ControlPanel getControlPanel();

    interface ControlPanel {
        void clear();

        void preheat(@NonNull Pid pid, @NonNull Object object);

        @NonNull
        Pid preheat(@Nullable Object object); // To add

        @NonNull
        Pid preheat(); // To add by default

        @NonNull
        List<Key<?>> keys();

        @NonNull
        List<Pid> pids();

        @NonNull
        List<String> elementNames();

        @NonNull
        List<Element<?>> elements();

        @Nullable
        Pid pid(@NonNull Pid.Index index);
    }
}
