package com.github.k24.prefsoven;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.k24.prefsoven.factory.AbstractElementFactory;
import com.github.k24.prefsoven.field.AbstractPref;
import com.github.k24.prefsoven.store.Element;
import com.github.k24.prefsoven.store.Key;
import com.github.k24.prefsoven.store.Model;
import com.github.k24.prefsoven.store.Pid;

import org.androidannotations.api.sharedpreferences.SharedPreferencesCompat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by k24 on 2016/01/14.
 */
final class PrefsStoreInvocationHandler<T> implements InvocationHandler {
    private static final String NAME = "name";
    private static final String MODE = "mode";
    private static final String UNIQUE = "unique";
    private final Class<T> clazz;
    private final PrefsStoreHelper prefsHelper;
    private final Model model;
    private PrefsStoreOven.ControlPanel controlPanel;

    public PrefsStoreInvocationHandler(Context context, Class<T> clazz, Map<Class<?>, Model> modelMap, AbstractElementFactory elementFactory) throws InvocationTargetException, IllegalAccessException {
        this.clazz = clazz;
        SharedPreferences prefs = PrefsInvocationHandler.createSharedPreferences(context, clazz);
        if (modelMap.containsKey(clazz)) {
            model = modelMap.get(clazz);
            prefsHelper = (PrefsStoreHelper) model.getPrefFieldFactory();
        } else {
            prefsHelper = new PrefsStoreHelper(context, prefs);
            prefsHelper.setElementFactory(elementFactory);
            model = Model.create(prefs, prefsHelper, prefsHelper.createElementMap(clazz));
            modelMap.put(clazz, model);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        String methodName = method.getName();
        switch (methodName) {
            case "bake":
                if (args.length == 2 && args[0] instanceof Pid) {
                    bake((Pid) args[0], args[1]);
                    return null;
                }
                throw new IllegalArgumentException("Don't overload " + methodName);
            case "cook":
                if (args.length == 2 && args[0] instanceof Pid && args[1] instanceof Class) {
                    Class<?> targetClass = (Class<?>) args[1];
                    Object target = targetClass.newInstance();
                    bake((Pid) args[0], target);
                    return target;
                }
            case "getControlPanel":
                if (args == null) {
                    return getControlPanel();
                }
                throw new IllegalArgumentException("Don't overload " + methodName);
            default:
                if (method.getReturnType().getSuperclass() == Element.class) {
                    Element<?> element = prefsHelper.getElement(method);
                    if (element != null) {
                        return element;
                    }
                }
        }
        throw new NoSuchMethodException(method.toString());
    }

    //region ControlPanel
    private PrefsStoreOven.ControlPanel getControlPanel() {
        if (controlPanel == null) {
            controlPanel = new PrefsStoreOven.ControlPanel() {
                @Override
                public void clear() {
                    SharedPreferencesCompat.apply(prefsHelper.getSharedPreferences().edit().clear());
                    model.clear();
                }

                @Override
                public void preheat(@NonNull Pid pid, @NonNull Object object) {
                    PrefsStoreInvocationHandler.this.preheat(pid, object);
                }

                @NonNull
                @Override
                public Pid preheat(@Nullable Object object) {
                    return PrefsStoreInvocationHandler.this.preheat(object);
                }

                @NonNull
                @Override
                public Pid preheat() {
                    return preheat(null);
                }

                @NonNull
                @Override
                public List<Key<?>> keys() {
                    return model.keys();
                }

                @NonNull
                @Override
                public List<Pid> pids() {
                    return model.pids();
                }

                @NonNull
                @Override
                public List<Element<?>> elements() {
                    return model.elements();
                }

                @NonNull
                @Override
                public List<String> elementNames() {
                    return model.elementNames();
                }

                @Nullable
                @Override
                public Pid pid(@NonNull Pid.Index index) {
                    return model.pid(index);
                }
            };
        }
        return controlPanel;
    }

    Pid preheat(@Nullable Object source) {
        HashMap<Element<?>, Field> map = new HashMap<>();
        Class<?> sourceClass = source == null ? null : source.getClass();
        for (Method method : clazz.getMethods()) {
            if (method.getReturnType().getSuperclass() != Element.class)
                continue; // Not pref method
            Element<?> element = prefsHelper.getElement(method);
            if (element == null) continue; // Something wrong
            Field field = sourceClass == null ? null : getFieldNoThrow(sourceClass, method.getName());
            map.put(element, field);
        }

        Pid pid = model.addElements(map.keySet());
        for (Map.Entry<Element<?>, Field> entry : map.entrySet()) {
            Field field = entry.getValue();
            if (field == null) continue;
            getValueFromFieldNoThrow(field, source, pid.value(entry.getKey()));
        }
        return pid;
    }

    void preheat(Pid pid, @NonNull Object source) {
        if (!pid.isBoundWithKeys()) throw new IllegalArgumentException();

        Class<?> sourceClass = source.getClass();
        for (Method method : clazz.getMethods()) {
            if (method.getReturnType().getSuperclass() != Element.class)
                continue; // Not pref method
            Field field = getFieldNoThrow(sourceClass, method.getName());
            if (field == null) continue; // Missing in target
            Element<?> element = prefsHelper.getElement(method);
            if (element == null) continue; // Something wrong
            getValueFromFieldNoThrow(field, source, pid.value(element));
        }
    }

    @SuppressWarnings("unchecked")
    private static void getValueFromFieldNoThrow(Field field, Object source, AbstractPref pref) {
        try {
            pref.put(field.get(source));
        } catch (IllegalAccessException e) {
            Log.w("PrefsOven", "bake fails", e);
        }
    }
    //endregion

    //region Bake
    private void bake(Pid pid, Object target) {
        if (!pid.isBoundWithKeys()) throw new IllegalArgumentException();

        Class<?> targetClass = target.getClass();
        for (Method method : clazz.getMethods()) {
            if (method.getReturnType().getSuperclass() != Element.class)
                continue; // Not pref method
            Field field = getFieldNoThrow(targetClass, method.getName());
            if (field == null) continue; // Missing in target
            Element<?> element = prefsHelper.getElement(method);
            if (element == null) continue; // Something wrong
            setValueToFieldNoThrow(field, target, pid.value(element).get());
        }
    }

    private static Field getFieldNoThrow(@NonNull Class<?> clazz, @NonNull String name) {
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
    //endregion
}
