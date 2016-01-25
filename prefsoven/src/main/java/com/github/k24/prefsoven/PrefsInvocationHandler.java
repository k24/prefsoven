package com.github.k24.prefsoven;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.github.k24.prefsoven.field.AbstractOvenPrefField;

import org.androidannotations.api.sharedpreferences.SharedPreferencesCompat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by k24 on 2016/01/14.
 */
final class PrefsInvocationHandler<T> implements InvocationHandler {

    private static final String NAME = "name";
    private static final String MODE = "mode";
    private static final String UNIQUE = "unique";
    private final Class<T> clazz;
    private final PrefsHelper prefsHelper;
    private final Map<Method, AbstractOvenPrefField<?>> prefFieldMap;
    private PrefsOven.ControlPanel controlPanel;

    public PrefsInvocationHandler(Context context, Class<T> clazz, Map<Method, AbstractOvenPrefField<?>> prefFieldMap) throws InvocationTargetException, IllegalAccessException {
        this.clazz = clazz;
        this.prefFieldMap = prefFieldMap;
        SharedPreferences prefs = createSharedPreferences(context, clazz);
        prefsHelper = new PrefsHelper(context, prefs);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        String methodName = method.getName();
        switch (methodName) {
            case "bake":
                if (args.length == 1) {
                    bake(args[0]);
                    return null;
                }
            case "cook":
                if (args.length == 1 && args[0] instanceof Class) {
                    Class<?> targetClass = (Class<?>) args[0];
                    Object target = targetClass.newInstance();
                    bake(target);
                    return target;
                }
            case "getControlPanel":
                if (args == null) {
                    return getControlPanel();
                }
                throw new IllegalArgumentException("Don't overload " + methodName);
            default:
                if (method.getReturnType().getSuperclass() == AbstractOvenPrefField.class) {
                    AbstractOvenPrefField prefField = getPrefField(method);
                    if (prefField != null) {
                        return prefField;
                    }
                    throw new UnsupportedOperationException("Unknown PrefField: " + method.getReturnType().getCanonicalName());
                }
        }

        throw new NoSuchMethodException(method.toString());
    }

    //region ControlPanel
    private synchronized PrefsOven.ControlPanel getControlPanel() {
        if (controlPanel == null) {
            controlPanel = new PrefsOven.ControlPanel() {
                @Override
                public void clear() {
                    SharedPreferencesCompat.apply(prefsHelper.getSharedPreferences().edit().clear());
                }

                @Override
                public void preheat(Object prototype) {
                    PrefsInvocationHandler.this.preheat(prototype);
                }
            };
        }
        return controlPanel;
    }

    void preheat(Object source) {
        Class<?> sourceClass = source.getClass();
        for (Method method : clazz.getMethods()) {
            if (method.getReturnType().getSuperclass() != AbstractOvenPrefField.class)
                continue; // Not pref method
            Field field = getFieldNoThrow(sourceClass, method.getName());
            if (field == null) continue; // Missing in target
            AbstractOvenPrefField prefField = (AbstractOvenPrefField) getPrefField(method);
            if (prefField == null) continue; // Something wrong
            getValueFromFieldNoThrow(field, source, prefField);
        }
    }

    @SuppressWarnings("unchecked")
    private static void getValueFromFieldNoThrow(Field field, Object source, AbstractOvenPrefField pref) {
        try {
            pref.put(field.get(source));
        } catch (IllegalAccessException e) {
            Log.w("PrefsOven", "bake fails", e);
        }
    }
    //endregion

    //region Bake
    private void bake(Object target) {
        Class<?> targetClass = target.getClass();
        for (Method method : clazz.getMethods()) {
            if (method.getReturnType().getSuperclass() != AbstractOvenPrefField.class)
                continue; // Not pref method
            Field field = getFieldNoThrow(targetClass, method.getName());
            if (field == null) continue; // Missing in target
            AbstractOvenPrefField prefField = (AbstractOvenPrefField) getPrefField(method);
            if (prefField == null) continue; // Something wrong
            setValueToFieldNoThrow(field, target, prefField.get());
        }
    }

    private static Field getFieldNoThrow(Class<?> clazz, String name) {
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException e) {
            Log.w("PrefsOven", "bake fails", e);
            return null;
        }
    }

    private static void setValueToFieldNoThrow(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            Log.w("PrefsOven", "bake fails", e);
        }
    }

    private AbstractOvenPrefField getPrefField(Method method) {
        synchronized (prefFieldMap) {
            AbstractOvenPrefField<?> prefField = prefFieldMap.get(method);
            if (prefField == null) {
                prefField = prefsHelper.createPrefField(method);
                prefFieldMap.put(method, prefField);
            }
            return prefField;
        }
    }
    //endregion

    public static SharedPreferences createSharedPreferences(Context context, @NonNull Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
        String name = null;
        int mode = 0;
        boolean unique = true;
        for (Annotation annotation : clazz.getAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();
            for (Method annotMethod : type.getDeclaredMethods()) {
                switch (annotMethod.getName()) {
                    case NAME:
                        name = (String) annotMethod.invoke(annotation);
                        break;
                    case MODE:
                        mode = (int) annotMethod.invoke(annotation);
                        break;
                    case UNIQUE:
                        unique = (boolean) annotMethod.invoke(annotation);
                        break;
                }
            }
        }

        if (!unique) {
            if (!TextUtils.isEmpty(name)) {
                Log.w("PrefsOven", name + " is ignored because of using default");
            }
            return PreferenceManager.getDefaultSharedPreferences(context);
        }
        if (TextUtils.isEmpty(name)) {
            name = clazz.getName();
        }
        return context.getSharedPreferences(name, mode);
    }

}
