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
public class StringPrefTest {

    @Before
    public void setUp() throws Exception {
        Prefs.install(RuntimeEnvironment.application);
        Prefs.prefs(TestPrefs.class).edit().clear().commit();
    }

    @Test
    public void isEmpty() throws Exception {
        TestPrefs prefs = Prefs.oven(TestPrefs.class);

        assertThat(prefs.string1().isEmpty())
                .isTrue();
        prefs.string1().put("");
        assertThat(prefs.string1().isEmpty())
                .isTrue();
        prefs.string1().put("str");
        assertThat(prefs.string1().isEmpty())
                .isFalse();
    }

    @Test
    public void add() throws Exception {
        TestPrefs prefs = Prefs.oven(TestPrefs.class);

        assertThat(prefs.string1().add("str"))
                .isEqualTo("str");
        assertThat(prefs.string1().add(""))
                .isEqualTo("str");
        assertThat(prefs.string1().add("ing"))
                .isEqualTo("string");
    }
}