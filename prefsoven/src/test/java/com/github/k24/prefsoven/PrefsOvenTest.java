package com.github.k24.prefsoven;

import android.os.Build;

import com.github.k24.prefsoven.custom.CustomBread;
import com.github.k24.prefsoven.custom.CustomPrefFieldFactory;
import com.github.k24.prefsoven.custom.CustomPrefs;
import com.github.k24.prefsoven.sample.TestBread;
import com.github.k24.prefsoven.sample.TestPrefs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by k24 on 2016/01/15.
 */
@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
@RunWith(RobolectricTestRunner.class)
public class PrefsOvenTest {
    private PrefsOvenVendor ovenVendor;

    @Before
    public void setUp() throws Exception {
        ovenVendor = PrefsOvenVendor.vendor(RuntimeEnvironment.application);
        ovenVendor.create(TestPrefs.class).getControlPanel().clear();
    }

    @Test
    public void bake() throws Exception {
        TestPrefs prefs = ovenVendor.create(TestPrefs.class);
        prefs.int1().put(1);
        prefs.float1().put(2.3f);
        prefs.long1().put(4L);
        prefs.boolean1().put(true);
        prefs.string1().put("str");
        prefs.stringSet1().put(new LinkedHashSet<>(Arrays.asList(new String[]{"5", "6", "7"})));

        TestBread testBread = new TestBread();
        prefs.bake(testBread);

        assertThat(testBread.int1).isEqualTo(1);
        assertThat(testBread.float1).isEqualTo(2.3f);
        assertThat(testBread.long1).isEqualTo(4L);
        assertThat(testBread.boolean1).isTrue();
        assertThat(testBread.string1).isEqualTo("str");
        assertThat(testBread.stringSet1).contains("5", "6", "7");
    }

    @Test
    public void cook() throws Exception {
        TestPrefs prefs = ovenVendor.create(TestPrefs.class);
        prefs.int1().put(1);
        prefs.float1().put(2.3f);
        prefs.long1().put(4L);
        prefs.boolean1().put(true);
        prefs.string1().put("str");
        prefs.stringSet1().put(new LinkedHashSet<>(Arrays.asList(new String[]{"5", "6", "7"})));

        TestBread testBread = prefs.cook(TestBread.class);

        assertThat(testBread.int1).isEqualTo(1);
        assertThat(testBread.float1).isEqualTo(2.3f);
        assertThat(testBread.long1).isEqualTo(4L);
        assertThat(testBread.boolean1).isTrue();
        assertThat(testBread.string1).isEqualTo("str");
        assertThat(testBread.stringSet1).contains("5", "6", "7");
    }

    @Test
    public void controlPanel_clear() throws Exception {
        TestPrefs prefs = ovenVendor.create(TestPrefs.class);
        // Get a default value and put then get

        // Int
        assertThat(prefs.int1().get()).isEqualTo(0);
        prefs.int1().put(100);
        assertThat(prefs.int1().get()).isEqualTo(100);
        // Float
        assertThat(prefs.float1().get()).isEqualTo(0f);
        prefs.float1().put(12.3f);
        assertThat(prefs.float1().get()).isEqualTo(12.3f);
        // Long
        assertThat(prefs.long1().get()).isEqualTo(0L);
        prefs.long1().put(123L);
        assertThat(prefs.long1().get()).isEqualTo(123L);
        // Boolean
        assertThat(prefs.boolean1().get()).isFalse();
        prefs.boolean1().put(true);
        assertThat(prefs.boolean1().get()).isTrue();
        // String
        assertThat(prefs.string1().get()).isNull();
        prefs.string1().put("str");
        assertThat(prefs.string1().get()).isEqualTo("str");
        // StringSet
        LinkedHashSet<String> strings = new LinkedHashSet<>();
        strings.add("1");
        strings.add("2");
        strings.add("3");
        assertThat(prefs.stringSet1().get()).isNull();
        prefs.stringSet1().put(strings);
        assertThat(prefs.stringSet1().get()).contains("1", "2", "3");
    }

    @Test
    public void controlPanel_preheat() throws Exception {
        TestPrefs prefs = ovenVendor.create(TestPrefs.class);

        TestBread testBread = new TestBread();
        testBread.int1 = 100;
        testBread.float1 = 20f;
        testBread.long1 = 34L;
        testBread.boolean1 = true;
        testBread.string1 = "str5";
        testBread.stringSet1 = new LinkedHashSet<>(Arrays.asList(new String[]{"6", "7", "8"}));

        prefs.getControlPanel().preheat(testBread);

        assertThat(prefs.int1().get()).isEqualTo(100);
        assertThat(prefs.float1().get()).isEqualTo(20f);
        assertThat(prefs.long1().get()).isEqualTo(34L);
        assertThat(prefs.boolean1().get()).isTrue();
        assertThat(prefs.string1().get()).isEqualTo("str5");
        assertThat(prefs.stringSet1().get()).contains("6", "7", "8");
    }

    @Test
    public void customField() throws Exception {
        ovenVendor.setPrefFieldFactory(new CustomPrefFieldFactory());
        CustomPrefs prefs = ovenVendor.create(CustomPrefs.class);

        CustomBread bread = new CustomBread();

        // Bake default
        prefs.bake(bread);

        assertThat(bread.double1)
                .isEqualTo(0);

        // Preheat
        bread.double1 = 123.4;

        prefs.getControlPanel().preheat(bread);

        assertThat(prefs.double1().get())
                .isEqualTo(123.4);

        // Cook value
        bread = prefs.cook(CustomBread.class);

        assertThat(bread.double1)
                .isEqualTo(123.4);
    }
}