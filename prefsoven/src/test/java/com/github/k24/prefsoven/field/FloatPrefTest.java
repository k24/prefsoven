package com.github.k24.prefsoven.field;

import android.os.Build;

import com.github.k24.prefsoven.Prefs;
import com.github.k24.prefsoven.sample.TestPrefs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by k24 on 2016/02/21.
 */
@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
@RunWith(RobolectricTestRunner.class)
public class FloatPrefTest {

    @Before
    public void setUp() throws Exception {
        Prefs.install(RuntimeEnvironment.application);
        Prefs.prefs(TestPrefs.class).edit().clear().commit();
    }

    @Test
    public void add() throws Exception {
        TestPrefs prefs = Prefs.oven(TestPrefs.class);

        assertThat(prefs.float1().add(1f))
                .isEqualTo(1f);
        assertThat(prefs.float1().add(1f))
                .isEqualTo(2f);
        assertThat(prefs.float1().add(1.9f))
                .isEqualTo(2f + 1.9f);
        assertThat(prefs.float1().add(-1.9f))
                .isEqualTo(2f);

        // Max
        prefs.float1().put(1f);
        assertThat(prefs.float1().add(Float.MAX_VALUE))
                .isEqualTo(Float.MAX_VALUE);
        prefs.float1().put(-1f);
        assertThat(prefs.float1().add(Float.MAX_VALUE))
                .isEqualTo(Float.MAX_VALUE - 1f);
    }
}