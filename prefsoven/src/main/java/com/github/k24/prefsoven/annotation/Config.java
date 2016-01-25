package com.github.k24.prefsoven.annotation;

import android.content.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by k24 on 2015/12/26.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {

    String name();

    int mode() default Context.MODE_PRIVATE;

    boolean unique() default true;
}
