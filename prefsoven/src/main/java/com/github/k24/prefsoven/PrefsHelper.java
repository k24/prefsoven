package com.github.k24.prefsoven;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.TypedValue;

import com.github.k24.prefsoven.annotation.DefaultRes;
import com.github.k24.prefsoven.annotation.KeyRes;
import com.github.k24.prefsoven.field.AbstractOvenPrefField;
import com.github.k24.prefsoven.field.BooleanPref;
import com.github.k24.prefsoven.field.FloatPref;
import com.github.k24.prefsoven.field.IntPref;
import com.github.k24.prefsoven.field.LongPref;
import com.github.k24.prefsoven.field.StringPref;
import com.github.k24.prefsoven.field.StringSetPref;

import org.androidannotations.api.sharedpreferences.SharedPreferencesHelper;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by k24 on 2016/01/14.
 */
final class PrefsHelper extends SharedPreferencesHelper {
    final Context context;

    public PrefsHelper(Context context, SharedPreferences sharedPreferences) {
        super(sharedPreferences);
        this.context = context;
    }

    public AbstractOvenPrefField<?> createPrefField(Method method) {
        return (AbstractOvenPrefField<?>) TypeMap.FIELD_GETTER_MAP.get(method.getReturnType()).get(this, method);
    }

    private static class TypeMap {
        public static final Map<Class<?>, FieldGetter> FIELD_GETTER_MAP;

        static {
            HashMap<Class<?>, FieldGetter> map = new HashMap<>();
            map.put(IntPref.class, new FieldGetter() {

                @Override
                public Object get(PrefsHelper prefsHelper, Method method) {
                    Resources res = prefsHelper.context.getResources();
                    String key = getKey(res, method);
                    int defaultValue = getIntDefaultValue(res, method);
                    return new IntPref(prefsHelper.intField(key, defaultValue));
                }
            });
            map.put(FloatPref.class, new FieldGetter() {
                @Override
                public Object get(PrefsHelper prefsHelper, Method method) {
                    Resources res = prefsHelper.context.getResources();
                    String key = getKey(res, method);
                    float defaultValue = getFloatDefaultValue(res, method);
                    return new FloatPref(prefsHelper.floatField(key, defaultValue));
                }
            });
            map.put(LongPref.class, new FieldGetter() {
                @Override
                public Object get(PrefsHelper prefsHelper, Method method) {
                    Resources res = prefsHelper.context.getResources();
                    String key = getKey(res, method);
                    long defaultValue = getLongDefaultValue(res, method);
                    return new LongPref(prefsHelper.longField(key, defaultValue));
                }
            });
            map.put(BooleanPref.class, new FieldGetter() {
                @Override
                public Object get(PrefsHelper prefsHelper, Method method) {
                    Resources res = prefsHelper.context.getResources();
                    String key = getKey(res, method);
                    boolean defaultValue = getBooleanDefaultValue(res, method);
                    return new BooleanPref(prefsHelper.booleanField(key, defaultValue));
                }
            });
            map.put(StringPref.class, new FieldGetter() {
                @Override
                public Object get(PrefsHelper prefsHelper, Method method) {
                    Resources res = prefsHelper.context.getResources();
                    String key = getKey(res, method);
                    String defaultValue = getStringDefaultValue(res, method);
                    return new StringPref(prefsHelper.stringField(key, defaultValue));
                }
            });
            map.put(StringSetPref.class, new FieldGetter() {
                @Override
                public Object get(PrefsHelper prefsHelper, Method method) {
                    Resources res = prefsHelper.context.getResources();
                    String key = getKey(res, method);
                    Set<String> defaultValue = getStringSetDefaultValue(res, method);
                    return new StringSetPref(prefsHelper.stringSetField(key, defaultValue));
                }
            });

            FIELD_GETTER_MAP = map;
        }
    }

    private interface FieldGetter {
        Object get(PrefsHelper prefsHelper, Method method);
    }

    static <T extends AccessibleObject & Member> String getKey(Resources res, T member) {
        KeyRes annotation = member.getAnnotation(KeyRes.class);
        return annotation == null ? member.getName() : res.getString(annotation.value());
    }

    static <T extends AccessibleObject & Member> int getIntDefaultValue(Resources res, T member) {
        DefaultRes annotation = member.getAnnotation(DefaultRes.class);
        return annotation == null ? 0 : res.getInteger(annotation.value());
    }

    static <T extends AccessibleObject & Member> float getFloatDefaultValue(Resources res, T member) {
        DefaultRes annotation = member.getAnnotation(DefaultRes.class);
        if (annotation == null) return 0;
        TypedValue typedValue = new TypedValue();
        res.getValue(annotation.value(), typedValue, true);
        return typedValue.getFloat();
    }

    static <T extends AccessibleObject & Member> long getLongDefaultValue(Resources res, T member) {
        DefaultRes annotation = member.getAnnotation(DefaultRes.class);
        return annotation == null ? 0 : res.getInteger(annotation.value());
    }

    static <T extends AccessibleObject & Member> boolean getBooleanDefaultValue(Resources res, T member) {
        DefaultRes annotation = member.getAnnotation(DefaultRes.class);
        return annotation != null && res.getBoolean(annotation.value());
    }

    static <T extends AccessibleObject & Member> String getStringDefaultValue(Resources res, T member) {
        DefaultRes annotation = member.getAnnotation(DefaultRes.class);
        return annotation == null ? null : res.getString(annotation.value());
    }

    static <T extends AccessibleObject & Member> Set<String> getStringSetDefaultValue(Resources res, T member) {
        DefaultRes annotation = member.getAnnotation(DefaultRes.class);
        if (annotation == null) return null;
        return new LinkedHashSet<>(Arrays.asList(res.getStringArray(annotation.value())));
    }
}
