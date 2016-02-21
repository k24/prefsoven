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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by k24 on 2016/02/21.
 */
@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
@RunWith(RobolectricTestRunner.class)
public class StringSetPrefTest {

    @Before
    public void setUp() throws Exception {
        Prefs.install(RuntimeEnvironment.application);
        Prefs.prefs(TestPrefs.class).edit().clear().commit();
    }

    @Test
    public void isEmpty() throws Exception {
        TestPrefs prefs = Prefs.oven(TestPrefs.class);

        assertThat(prefs.stringSet1().isEmpty())
                .isTrue();
        prefs.stringSet1().put(Collections.<String>emptySet());
        assertThat(prefs.stringSet1().isEmpty())
                .isTrue();
        prefs.stringSet1().put(Collections.singleton("str"));
        assertThat(prefs.stringSet1().isEmpty())
                .isFalse();
    }

    @Test
    public void add() throws Exception {
        TestPrefs prefs = Prefs.oven(TestPrefs.class);

        assertThat(prefs.stringSet1().add("str"))
                .hasSize(1)
                .contains("str");
        assertThat(prefs.stringSet1().add(""))
                .hasSize(2)
                .contains("str", "");
        assertThat(prefs.stringSet1().add("ing"))
                .hasSize(3)
                .contains("str", "", "ing");
    }

    @Test
    public void remove() throws Exception {
        TestPrefs prefs = Prefs.oven(TestPrefs.class);

        assertThat(prefs.stringSet1().remove("str"))
                .isNull();
        prefs.stringSet1().put(new HashSet<String>(Arrays.asList("str", "ing")));
        assertThat(prefs.stringSet1().remove(""))
                .hasSize(2)
                .contains("str", "ing");
        assertThat(prefs.stringSet1().remove("ing"))
                .hasSize(1)
                .contains("str");
    }
}