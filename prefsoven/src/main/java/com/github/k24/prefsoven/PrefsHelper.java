package com.github.k24.prefsoven;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.TypedValue;

import com.github.k24.prefsoven.annotation.DefaultRes;
import com.github.k24.prefsoven.annotation.KeyRes;
import com.github.k24.prefsoven.factory.AbstractFieldFactory;
import com.github.k24.prefsoven.field.AbstractPref;
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
    private AbstractFieldFactory factory;
    private HashMap<Method, Pair<String, Integer>> keyDefaultResMap = new HashMap<>();

    public PrefsHelper(Context context, SharedPreferences sharedPreferences) {
        super(sharedPreferences);
        this.context = context;
    }

    public void setFactory(AbstractFieldFactory factory) {
        this.factory = factory;
    }

    @NonNull
    public AbstractPref<?> createPrefField(Method method) {
        FieldGetter fieldGetter = TypeMap.FIELD_GETTER_MAP.get(method.getReturnType());
        Pair<String, Integer> keyAndDefaultRes = getKeyAndDefaultRes(method);
        if (fieldGetter != null) {
            return (AbstractPref<?>) fieldGetter.get(this, keyAndDefaultRes.first, keyAndDefaultRes.second);
        } else {
            if (factory == null)
                throw new UnsupportedOperationException("You should set AbstractFieldFactory for: " + method.getReturnType());
            AbstractPref<?> field = factory.createPref(context, getSharedPreferences(), method.getReturnType(), keyAndDefaultRes.first, keyAndDefaultRes.second);
            if (field == null)
                throw new UnsupportedOperationException("Your AbstractFieldFactory should implement Pref for: " + method.getReturnType());
            return field;
        }
    }

    @NonNull
    private Pair<String, Integer> getKeyAndDefaultRes(Method method) {
        Pair<String, Integer> pair = keyDefaultResMap.get(method);
        if (pair == null) {
            pair = Pair.create(getKey(context.getResources(), method), getDefaultId(method));
            keyDefaultResMap.put(method, pair);
        }
        return pair;
    }

    private static class TypeMap {
        public static final Map<Class<?>, FieldGetter> FIELD_GETTER_MAP;

        static {
            HashMap<Class<?>, FieldGetter> map = new HashMap<>();
            map.put(IntPref.class, new FieldGetter() {
                @Override
                public Object get(PrefsHelper prefsHelper, String key, int defaultResId) {
                    return new IntPref(prefsHelper.intField(key, AbstractFieldFactory.getIntDefaultValue(prefsHelper.context.getResources(), defaultResId)));
                }
            });
            map.put(FloatPref.class, new FieldGetter() {
                @Override
                public Object get(PrefsHelper prefsHelper, String key, int defaultResId) {
                    return new FloatPref(prefsHelper.floatField(key, AbstractFieldFactory.getFloatDefaultValue(prefsHelper.context.getResources(), defaultResId)));
                }
            });
            map.put(LongPref.class, new FieldGetter() {
                @Override
                public Object get(PrefsHelper prefsHelper, String key, int defaultResId) {
                    return new LongPref(prefsHelper.longField(key, AbstractFieldFactory.getLongDefaultValue(prefsHelper.context.getResources(), defaultResId)));
                }
            });
            map.put(BooleanPref.class, new FieldGetter() {
                @Override
                public Object get(PrefsHelper prefsHelper, String key, int defaultResId) {
                    return new BooleanPref(prefsHelper.booleanField(key, AbstractFieldFactory.getBooleanDefaultValue(prefsHelper.context.getResources(), defaultResId)));
                }
            });
            map.put(StringPref.class, new FieldGetter() {
                @Override
                public Object get(PrefsHelper prefsHelper, String key, int defaultResId) {
                    return new StringPref(prefsHelper.stringField(key, AbstractFieldFactory.getStringDefaultValue(prefsHelper.context.getResources(), defaultResId)));
                }
            });
            map.put(StringSetPref.class, new FieldGetter() {
                @Override
                public Object get(PrefsHelper prefsHelper, String key, int defaultResId) {
                    return new StringSetPref(prefsHelper.stringSetField(key, AbstractFieldFactory.getStringSetDefaultValue(prefsHelper.context.getResources(), defaultResId)));
                }
            });

            FIELD_GETTER_MAP = map;
        }
    }

    interface FieldGetter {
        Object get(PrefsHelper prefsHelper, String key, int defaultResId);
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

    static <T extends AccessibleObject & Member> int getDefaultId(T member) {
        DefaultRes annotation = member.getAnnotation(DefaultRes.class);
        return annotation == null ? 0 : annotation.value();
    }
}
