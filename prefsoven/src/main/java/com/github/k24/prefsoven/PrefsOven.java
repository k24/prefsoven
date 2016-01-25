package com.github.k24.prefsoven;

/**
 * Created by k24 on 2016/01/08.
 */
public interface PrefsOven {
    void bake(Object object);

    <T> T cook(Class<T> clazz);

    ControlPanel getControlPanel();

    interface ControlPanel {
        void clear();

        void preheat(Object prototype);
    }
}
