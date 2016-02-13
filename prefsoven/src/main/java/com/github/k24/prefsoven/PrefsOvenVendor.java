package com.github.k24.prefsoven;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.github.k24.prefsoven.factory.PrefFieldFactory;
import com.github.k24.prefsoven.field.AbstractOvenPrefField;
import com.github.k24.prefsoven.store.Model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by k24 on 2016/01/14.
 */
final class PrefsOvenVendor {
    private static PrefsOvenVendor instance;

    public static synchronized PrefsOvenVendor vendor(@NonNull Context context) {
        if (instance == null) {
            instance = new PrefsOvenVendor(context.getApplicationContext());
        }
        return instance;
    }

    private final Context context;
    private final Map<Method, AbstractOvenPrefField<?>> pefFieldMap = new HashMap<>();
    private final Map<Class<?>, Model> modelMap = new HashMap<>();
    private PrefFieldFactory prefFieldFactory;

    PrefsOvenVendor(Context context) {
        this.context = context;
    }

    public void setPrefFieldFactory(PrefFieldFactory prefFieldFactory) {
        this.prefFieldFactory = prefFieldFactory;
    }

    @SuppressWarnings("unchecked")
    public <T extends PrefsOven> T create(Class<T> clazz) throws InvocationTargetException, IllegalAccessException {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz},
                newPrefsHandler(clazz));
    }

    @SuppressWarnings("unchecked")
    public <T extends PrefsStoreOven> T createStore(Class<T> clazz) throws InvocationTargetException, IllegalAccessException {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz},
                new PrefsStoreInvocationHandler<>(context, clazz, modelMap));
    }

    private <T> PrefsInvocationHandler<T> newPrefsHandler(Class<T> clazz) throws InvocationTargetException, IllegalAccessException {
        PrefsInvocationHandler<T> handler = new PrefsInvocationHandler<>(context, clazz, pefFieldMap);
        handler.getPrefsHelper().setFactory(prefFieldFactory);
        return handler;
    }

    public SharedPreferences prefs(Class<?> clazz) {
        try {
            return PrefsInvocationHandler.createSharedPreferences(context, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public SharedPreferences getDefaultPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
