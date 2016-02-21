package com.github.k24.prefsoven.field;

import android.os.Build;

import com.github.k24.prefsoven.Prefs;
import com.github.k24.prefsoven.sample.TestPrefs;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Created by k24 on 2016/02/21.
 */
@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
@RunWith(RobolectricTestRunner.class)
public class IntPrefTest {

    @Before
    public void setUp() throws Exception {
        Prefs.install(RuntimeEnvironment.application);
        Prefs.prefs(TestPrefs.class).edit().clear().commit();
    }

    @Test
    public void increment() throws Exception {
        TestPrefs prefs = Prefs.oven(TestPrefs.class);

        assertThat(prefs.int1().increment())
                .isEqualTo(1);
        assertThat(prefs.int1().increment())
                .isEqualTo(2);
        prefs.int1().put(Integer.MAX_VALUE);
        assertThat(prefs.int1().increment())
                .isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void decrement() throws Exception {
        TestPrefs prefs = Prefs.oven(TestPrefs.class);

        assertThat(prefs.int1().decrement())
                .isEqualTo(-1);
        assertThat(prefs.int1().decrement())
                .isEqualTo(-2);
        prefs.int1().put(Integer.MIN_VALUE);
        assertThat(prefs.int1().decrement())
                .isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    public void add() throws Exception {
        TestPrefs prefs = Prefs.oven(TestPrefs.class);

        assertThat(prefs.int1().add(1))
                .isEqualTo(1);
        assertThat(prefs.int1().add(2))
                .isEqualTo(3);
        assertThat(prefs.int1().add(-7))
                .isEqualTo(-4);

        prefs.int1().put(1);
        assertThat(prefs.int1().add(Integer.MAX_VALUE))
                .isEqualTo(Integer.MAX_VALUE);
        prefs.int1().put(-1);
        assertThat(prefs.int1().add(Integer.MAX_VALUE))
                .isEqualTo(Integer.MAX_VALUE - 1);

        prefs.int1().put(-1);
        assertThat(prefs.int1().add(Integer.MIN_VALUE))
                .isEqualTo(Integer.MIN_VALUE);
        prefs.int1().put(1);
        assertThat(prefs.int1().add(Integer.MIN_VALUE))
                .isEqualTo(Integer.MIN_VALUE + 1);
    }
}