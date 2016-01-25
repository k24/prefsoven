package com.github.k24.prefsoven;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by k24 on 2016/01/17.
 */
public class Prefs {
    public static final Class<?> DEFAULT = Prefs.class;

    private static PrefsOvenVendor vendor;

    public synchronized static void install(Context context) {
        vendor = PrefsOvenVendor.vendor(context);
    }

    public synchronized static <T extends PrefsOven> T oven(@NonNull Class<T> clazz) {
        try {
            return vendor.create(clazz);
        } catch (NullPointerException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static <T extends PrefsStoreOven> T store(@NonNull Class<T> clazz) {
        try {
            return vendor.createStore(clazz);
        } catch (NullPointerException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static SharedPreferences prefs(@NonNull Class<?> clazz) {
        if (clazz == DEFAULT) return vendor.getDefaultPrefs();
        return vendor.prefs(clazz);
    }
}
