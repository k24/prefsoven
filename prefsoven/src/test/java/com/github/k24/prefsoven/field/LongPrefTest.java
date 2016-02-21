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

/**
 * Created by k24 on 2016/02/21.
 */
@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
@RunWith(RobolectricTestRunner.class)
public class LongPrefTest {

    @Before
    public void setUp() throws Exception {
        Prefs.install(RuntimeEnvironment.application);
        Prefs.prefs(TestPrefs.class).edit().clear().commit();
    }

    @Test
    public void increment() throws Exception {
        TestPrefs prefs = Prefs.oven(TestPrefs.class);

        Assertions.assertThat(prefs.long1().increment())
                .isEqualTo(1L);
        Assertions.assertThat(prefs.long1().increment())
                .isEqualTo(2L);
        prefs.long1().put(Long.MAX_VALUE);
        Assertions.assertThat(prefs.long1().increment())
                .isEqualTo(Long.MAX_VALUE);
    }

    @Test
    public void decrement() throws Exception {
        TestPrefs prefs = Prefs.oven(TestPrefs.class);

        Assertions.assertThat(prefs.long1().decrement())
                .isEqualTo(-1L);
        Assertions.assertThat(prefs.long1().decrement())
                .isEqualTo(-2L);
        prefs.long1().put(Long.MIN_VALUE);
        Assertions.assertThat(prefs.long1().decrement())
                .isEqualTo(Long.MIN_VALUE);
    }

    @Test
    public void add() throws Exception {
        TestPrefs prefs = Prefs.oven(TestPrefs.class);

        Assertions.assertThat(prefs.long1().add(1))
                .isEqualTo(1L);
        Assertions.assertThat(prefs.long1().add(2))
                .isEqualTo(3L);
        Assertions.assertThat(prefs.long1().add(-7))
                .isEqualTo(-4L);

        prefs.long1().put(1L);
        Assertions.assertThat(prefs.long1().add(Long.MAX_VALUE))
                .isEqualTo(Long.MAX_VALUE);
        prefs.long1().put(-1L);
        Assertions.assertThat(prefs.long1().add(Long.MAX_VALUE))
                .isEqualTo(Long.MAX_VALUE - 1L);
        prefs.long1().put(-1L);
        Assertions.assertThat(prefs.long1().add(Long.MIN_VALUE))
                .isEqualTo(Long.MIN_VALUE);
        prefs.long1().put(1L);
        Assertions.assertThat(prefs.long1().add(Long.MIN_VALUE))
                .isEqualTo(Long.MIN_VALUE + 1L);

    }
}