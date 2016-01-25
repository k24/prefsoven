package com.github.k24.prefsoven;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.github.k24.prefsoven.field.AbstractOvenPrefField;
import com.github.k24.prefsoven.field.BooleanPref;
import com.github.k24.prefsoven.field.FloatPref;
import com.github.k24.prefsoven.field.IntPref;
import com.github.k24.prefsoven.field.LongPref;
import com.github.k24.prefsoven.field.StringPref;
import com.github.k24.prefsoven.field.StringSetPref;
import com.github.k24.prefsoven.store.Element;
import com.github.k24.prefsoven.store.PrefFieldFactory;

import org.androidannotations.api.sharedpreferences.SharedPreferencesHelper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by k24 on 2015/12/31.
 */
final class PrefFieldHelper extends SharedPreferencesHelper implements PrefFieldFactory {

    private final Map<Method, Element<?>> methodElementMap = new HashMap<>();

    public PrefFieldHelper(SharedPreferences sharedPreferences) {
        super(sharedPreferences);
    }

    @SuppressWarnings("unchecked")
    public <T> AbstractOvenPrefField<T> createField(String keyStirng, T defaultValue, Class<T> typeClass) {
        return (AbstractOvenPrefField<T>) TypeMap.FIELD_GETTER_MAP.get(typeClass).get(this, keyStirng, defaultValue);
    }

    @Override
    public SharedPreferences getPrefs() {
        return getSharedPreferences();
    }

    @SuppressWarnings("unchecked")
    static <T> T getDefaultValue(Class<T> typeClass, Resources res, Method method) {
        return (T) TypeMap.FIELD_GETTER_MAP.get(typeClass).getDefaultValue(res, method);
    }

    private static class TypeMap {
        public static final Map<Class<?>, FieldGetter> FIELD_GETTER_MAP;

        static {
            HashMap<Class<?>, FieldGetter> fieldGetterMap = new HashMap<>();
            fieldGetterMap.put(Integer.class, new FieldGetter() {

                @Override
                public Object get(PrefFieldHelper prefsHelper, String keyString, Object defaultValue) {
                    return new IntPref(prefsHelper.intField(keyString, (Integer) defaultValue));
                }

                @Override
                public Object getDefaultValue(Resources res, Method method) {
                    return PrefsHelper.getIntDefaultValue(res, method);
                }
            });
            fieldGetterMap.put(Float.class, new FieldGetter() {
                @Override
                public Object get(PrefFieldHelper prefsHelper, String keyString, Object defaultValue) {
                    return new FloatPref(prefsHelper.floatField(keyString, (Float) defaultValue));
                }

                @Override
                public Object getDefaultValue(Resources res, Method method) {
                    return PrefsHelper.getFloatDefaultValue(res, method);
                }
            });
            fieldGetterMap.put(Long.class, new FieldGetter() {
                @Override
                public Object get(PrefFieldHelper prefsHelper, String keyString, Object defaultValue) {
                    return new LongPref(prefsHelper.longField(keyString, (Long) defaultValue));
                }

                @Override
                public Object getDefaultValue(Resources res, Method method) {
                    return PrefsHelper.getLongDefaultValue(res, method);
                }
            });
            fieldGetterMap.put(Boolean.class, new FieldGetter() {
                @Override
                public Object get(PrefFieldHelper prefsHelper, String keyString, Object defaultValue) {
                    return new BooleanPref(prefsHelper.booleanField(keyString, (Boolean) defaultValue));
                }

                @Override
                public Object getDefaultValue(Resources res, Method method) {
                    return PrefsHelper.getBooleanDefaultValue(res, method);
                }
            });
            fieldGetterMap.put(String.class, new FieldGetter() {
                @Override
                public Object get(PrefFieldHelper prefsHelper, String keyString, Object defaultValue) {
                    return new StringPref(prefsHelper.stringField(keyString, (String) defaultValue));
                }

                @Override
                public Object getDefaultValue(Resources res, Method method) {
                    return PrefsHelper.getStringDefaultValue(res, method);
                }
            });
            fieldGetterMap.put(Set.class, new FieldGetter() {
                @SuppressWarnings("unchecked")
                @Override
                public Object get(PrefFieldHelper prefsHelper, String keyString, Object defaultValue) {
                    return new StringSetPref(prefsHelper.stringSetField(keyString, (Set<String>) defaultValue));
                }

                @Override
                public Object getDefaultValue(Resources res, Method method) {
                    return PrefsHelper.getStringSetDefaultValue(res, method);
                }
            });

            FIELD_GETTER_MAP = fieldGetterMap;
        }
    }

    private interface FieldGetter extends DefaultValueGetter {
        Object get(PrefFieldHelper prefsHelper, String keyString, Object defaultValue);
    }

    private interface DefaultValueGetter {
        Object getDefaultValue(Resources res, Method method);
    }

    void addMethodToElement(Method method, Element<?> element) {
        methodElementMap.put(method, element);
    }

    Element<?> getElement(Method method) {
        return methodElementMap.get(method);
    }
}
