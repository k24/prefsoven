package com.github.k24.prefsoven;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.github.k24.prefsoven.factory.AbstractElementFactory;
import com.github.k24.prefsoven.factory.AbstractFieldFactory;
import com.github.k24.prefsoven.factory.PrefFieldFactory;
import com.github.k24.prefsoven.field.AbstractOvenPrefField;
import com.github.k24.prefsoven.field.BooleanPref;
import com.github.k24.prefsoven.field.FloatPref;
import com.github.k24.prefsoven.field.IntPref;
import com.github.k24.prefsoven.field.LongPref;
import com.github.k24.prefsoven.field.StringPref;
import com.github.k24.prefsoven.field.StringSetPref;
import com.github.k24.prefsoven.store.Element;
import com.github.k24.prefsoven.store.element.BooleanElement;
import com.github.k24.prefsoven.store.element.FloatElement;
import com.github.k24.prefsoven.store.element.IntElement;
import com.github.k24.prefsoven.store.element.LongElement;
import com.github.k24.prefsoven.store.element.StringElement;
import com.github.k24.prefsoven.store.element.StringSetElement;

import org.androidannotations.api.sharedpreferences.SharedPreferencesHelper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by k24 on 2015/12/31.
 */
final class PrefFieldHelper extends SharedPreferencesHelper implements PrefFieldFactory {

    private final Map<Method, Element<?>> methodElementMap = new HashMap<>();
    private final Context context;
    private AbstractElementFactory elementFactory;

    public PrefFieldHelper(Context context, SharedPreferences sharedPreferences) {
        super(sharedPreferences);
        this.context = context;
    }

    public void setElementFactory(AbstractElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> AbstractOvenPrefField<T> createField(String keyStirng, T defaultValue, Class<T> typeClass) {
        FieldGetter fieldGetter = TypeMap.FIELD_GETTER_MAP.get(typeClass);
        if (fieldGetter != null) {
            return (AbstractOvenPrefField<T>) fieldGetter.get(this, keyStirng, defaultValue);
        } else if (elementFactory != null) {
            AbstractOvenPrefField<T> prefField = (AbstractOvenPrefField<T>) elementFactory.createPref(getSharedPreferences(), keyStirng, defaultValue, typeClass);
            if (prefField == null)
                throw new UnsupportedOperationException("Your AbstractElementFactory should implement Pref for: " + typeClass);
            return prefField;
        } else {
            throw new UnsupportedOperationException("You should set AbstractElementFactory for: " + typeClass);
        }
    }

    public <T> Map<String, Element<?>> createElementMap(Class<T> clazz) {
        Resources res = context.getResources();
        LinkedHashMap<String, Element<?>> map = new LinkedHashMap<>();
        for (Method method : clazz.getMethods()) {
            Class<?> returnType = method.getReturnType();
            if (returnType.getSuperclass() == Element.class) {
                String key = PrefsHelper.getKey(res, method);
                int defaultId = PrefsHelper.getDefaultId(method);
                Element<?> element = createElementByElementClass(returnType, key, res, defaultId);
                map.put(key, element);
                addMethodToElement(method, element);
            }
        }
        return map;
    }

    private Element<?> createElementByElementClass(Class<?> elementClass, String key, Resources res, int defaultId) {
        ElementGetter elementGetter = TypeMap.ELEMENT_GETTER_MAP.get(elementClass);

        if (elementGetter != null) {
            return (Element<?>) elementGetter.get(res, key, defaultId);
        } else if (elementFactory != null) {
            Element<?> element = elementFactory.createElement(elementClass, key, res, defaultId);
            if (element == null)
                throw new UnsupportedOperationException("Your AbstractElementFactory should implement Pref for: " + elementClass);
            return element;
        } else {
            throw new UnsupportedOperationException("You should set AbstractElementFactory for: " + elementClass);
        }
    }

    private static class TypeMap {
        public static final Map<Class<?>, FieldGetter> FIELD_GETTER_MAP;
        public static final Map<Class<?>, ElementGetter> ELEMENT_GETTER_MAP;

        static {
            HashMap<Class<?>, FieldGetter> fieldGetterMap = new HashMap<>();
            fieldGetterMap.put(Integer.class, new FieldGetter() {

                @Override
                public Object get(PrefFieldHelper prefsHelper, String keyString, Object defaultValue) {
                    return new IntPref(prefsHelper.intField(keyString, (Integer) defaultValue));
                }
            });
            fieldGetterMap.put(Float.class, new FieldGetter() {
                @Override
                public Object get(PrefFieldHelper prefsHelper, String keyString, Object defaultValue) {
                    return new FloatPref(prefsHelper.floatField(keyString, (Float) defaultValue));
                }
            });
            fieldGetterMap.put(Long.class, new FieldGetter() {
                @Override
                public Object get(PrefFieldHelper prefsHelper, String keyString, Object defaultValue) {
                    return new LongPref(prefsHelper.longField(keyString, (Long) defaultValue));
                }
            });
            fieldGetterMap.put(Boolean.class, new FieldGetter() {
                @Override
                public Object get(PrefFieldHelper prefsHelper, String keyString, Object defaultValue) {
                    return new BooleanPref(prefsHelper.booleanField(keyString, (Boolean) defaultValue));
                }
            });
            fieldGetterMap.put(String.class, new FieldGetter() {
                @Override
                public Object get(PrefFieldHelper prefsHelper, String keyString, Object defaultValue) {
                    return new StringPref(prefsHelper.stringField(keyString, (String) defaultValue));
                }
            });
            fieldGetterMap.put(Set.class, new FieldGetter() {
                @SuppressWarnings("unchecked")
                @Override
                public Object get(PrefFieldHelper prefsHelper, String keyString, Object defaultValue) {
                    return new StringSetPref(prefsHelper.stringSetField(keyString, (Set<String>) defaultValue));
                }
            });

            FIELD_GETTER_MAP = fieldGetterMap;

            HashMap<Class<?>, ElementGetter> elementGetterMap = new HashMap<>();

            elementGetterMap.put(BooleanElement.class, new ElementGetter() {
                @Override
                public Object get(Resources res, String key, int defaultResId) {
                    return new BooleanElement(key, AbstractFieldFactory.getBooleanDefaultValue(res, defaultResId));
                }
            });
            elementGetterMap.put(FloatElement.class, new ElementGetter() {
                @Override
                public Object get(Resources res, String key, int defaultResId) {
                    return new FloatElement(key, AbstractFieldFactory.getFloatDefaultValue(res, defaultResId));
                }
            });
            elementGetterMap.put(IntElement.class, new ElementGetter() {
                @Override
                public Object get(Resources res, String key, int defaultResId) {
                    return new IntElement(key, AbstractFieldFactory.getIntDefaultValue(res, defaultResId));
                }
            });
            elementGetterMap.put(LongElement.class, new ElementGetter() {
                @Override
                public Object get(Resources res, String key, int defaultResId) {
                    return new LongElement(key, AbstractFieldFactory.getLongDefaultValue(res, defaultResId));
                }
            });
            elementGetterMap.put(StringElement.class, new ElementGetter() {
                @Override
                public Object get(Resources res, String key, int defaultResId) {
                    return new StringElement(key, AbstractFieldFactory.getStringDefaultValue(res, defaultResId));
                }
            });
            elementGetterMap.put(StringSetElement.class, new ElementGetter() {
                @Override
                public Object get(Resources res, String key, int defaultResId) {
                    return new StringSetElement(key, AbstractFieldFactory.getStringSetDefaultValue(res, defaultResId));
                }
            });

            ELEMENT_GETTER_MAP = elementGetterMap;
        }
    }

    private interface ElementGetter {
        Object get(Resources res, String key, int defaultResId);
    }

    private interface FieldGetter {
        Object get(PrefFieldHelper prefsHelper, String keyString, Object defaultValue);
    }

    void addMethodToElement(Method method, Element<?> element) {
        methodElementMap.put(method, element);
    }

    Element<?> getElement(Method method) {
        return methodElementMap.get(method);
    }
}
