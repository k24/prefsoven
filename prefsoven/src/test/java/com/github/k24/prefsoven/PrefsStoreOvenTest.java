package com.github.k24.prefsoven;

import android.os.Build;

import com.github.k24.prefsoven.field.AbstractOvenPrefField;
import com.github.k24.prefsoven.sample.TestBread;
import com.github.k24.prefsoven.sample.TestPrefsStore;
import com.github.k24.prefsoven.store.Key;
import com.github.k24.prefsoven.store.Pid;

import org.assertj.core.api.Condition;
import org.assertj.core.data.Index;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by k24 on 2016/01/15.
 */
@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
@RunWith(RobolectricTestRunner.class)
public class PrefsStoreOvenTest {

    private PrefsOvenVendor ovenVendor;

    @Before
    public void setUp() throws Exception {
        ovenVendor = PrefsOvenVendor.vendor(RuntimeEnvironment.application);
        ovenVendor.createStore(TestPrefsStore.class).getControlPanel().clear();
    }

    @Test
    public void clear() throws Exception {
        TestPrefsStore prefsStore = ovenVendor.createStore(TestPrefsStore.class);
        assertThat(prefsStore.int1().values()).isEmpty();
        assertThat(prefsStore.float1().values()).isEmpty();
        assertThat(prefsStore.long1().values()).isEmpty();
        assertThat(prefsStore.boolean1().values()).isEmpty();
        assertThat(prefsStore.string1().values()).isEmpty();
        assertThat(prefsStore.stringSet1().values()).isEmpty();
    }

    @Test
    public void keys() throws Exception {
        TestPrefsStore prefsStore = ovenVendor.createStore(TestPrefsStore.class);
        assertThat(prefsStore.getControlPanel().keys()).isEmpty();

        TestBread bread = new TestBread();
        bread.int1 = 100;
        bread.float1 = 2.3f;
        bread.long1 = 4L;
        bread.boolean1 = true;
        bread.string1 = "5";
        bread.stringSet1 = new HashSet<>(Arrays.asList("6", "7", "8"));

        final Pid pid = prefsStore.getControlPanel().preheat(bread);

        assertThat(prefsStore.getControlPanel().keys())
                .hasSize(6)
                .has(new Condition<List<? extends Key>>() {
                    @Override
                    public boolean matches(List<? extends Key> keys) {
                        for (Key key : keys) {
                            if (!pid.equals(key.pid())) return false;
                        }
                        return true;
                    }
                });
    }

    @Test
    public void bake() throws Exception {
        TestPrefsStore prefsStore = ovenVendor.createStore(TestPrefsStore.class);

        TestBread bread = new TestBread();
        bread.int1 = 100;
        bread.float1 = 2.3f;
        bread.long1 = 4L;
        bread.boolean1 = true;
        bread.string1 = "5";
        bread.stringSet1 = new HashSet<>(Arrays.asList("6", "7", "8"));

        Pid pid = prefsStore.getControlPanel().preheat(bread);

        assertThat(pid.value(prefsStore.int1()).get()).isEqualTo(100);
        assertThat(pid.value(prefsStore.float1()).get()).isEqualTo(2.3f);
        assertThat(pid.value(prefsStore.long1()).get()).isEqualTo(4L);
        assertThat(pid.value(prefsStore.boolean1()).get()).isTrue();
        assertThat(pid.value(prefsStore.string1()).get()).isEqualTo("5");
        assertThat(pid.value(prefsStore.stringSet1()).get()).contains("6", "7", "8");

        bread = new TestBread();

        prefsStore.bake(pid, bread);

        assertThat(bread.int1).isEqualTo(100);
        assertThat(bread.float1).isEqualTo(2.3f);
        assertThat(bread.long1).isEqualTo(4L);
        assertThat(bread.boolean1).isTrue();
        assertThat(bread.string1).isEqualTo("5");
        assertThat(bread.stringSet1).contains("6", "7", "8");

        assertThat(prefsStore.int1().values())
                .hasSize(1)
                .has(new Condition<AbstractOvenPrefField<Integer>>() {
                    @Override
                    public boolean matches(AbstractOvenPrefField<Integer> abstractOvenPrefField) {
                        return abstractOvenPrefField.get() == 100;
                    }
                }, Index.atIndex(0));
    }

    @Test
    public void cook() throws Exception {
        TestPrefsStore prefsStore = ovenVendor.createStore(TestPrefsStore.class);

        TestBread bread = new TestBread();
        bread.int1 = 100;
        bread.float1 = 2.3f;
        bread.long1 = 4L;
        bread.boolean1 = true;
        bread.string1 = "5";
        bread.stringSet1 = new HashSet<>(Arrays.asList("6", "7", "8"));

        Pid pid = prefsStore.getControlPanel().preheat(bread);

        assertThat(pid.value(prefsStore.int1()).get()).isEqualTo(100);
        assertThat(pid.value(prefsStore.float1()).get()).isEqualTo(2.3f);
        assertThat(pid.value(prefsStore.long1()).get()).isEqualTo(4L);
        assertThat(pid.value(prefsStore.boolean1()).get()).isTrue();
        assertThat(pid.value(prefsStore.string1()).get()).isEqualTo("5");
        assertThat(pid.value(prefsStore.stringSet1()).get()).contains("6", "7", "8");

        bread = prefsStore.cook(pid, TestBread.class);

        assertThat(bread.int1).isEqualTo(100);
        assertThat(bread.float1).isEqualTo(2.3f);
        assertThat(bread.long1).isEqualTo(4L);
        assertThat(bread.boolean1).isTrue();
        assertThat(bread.string1).isEqualTo("5");
        assertThat(bread.stringSet1).contains("6", "7", "8");

        assertThat(prefsStore.int1().values())
                .hasSize(1)
                .has(new Condition<AbstractOvenPrefField<Integer>>() {
                    @Override
                    public boolean matches(AbstractOvenPrefField<Integer> abstractOvenPrefField) {
                        return abstractOvenPrefField.get() == 100;
                    }
                }, Index.atIndex(0));
    }

    @Test
    public void preheat() throws Exception {
        TestPrefsStore prefsStore = ovenVendor.createStore(TestPrefsStore.class);
        Pid pid = prefsStore.getControlPanel().preheat();
        TestBread bread = prefsStore.cook(pid, TestBread.class);

        // Verify default values
        assertThat(bread.int1).isEqualTo(0);
        assertThat(bread.float1).isEqualTo(0f);
        assertThat(bread.long1).isEqualTo(0L);
        assertThat(bread.boolean1).isFalse();
        assertThat(bread.string1).isNull();
        assertThat(bread.stringSet1).isNull();

        bread.int1 = 100;
        bread.float1 = 2.3f;
        bread.long1 = 4L;
        bread.boolean1 = true;
        bread.string1 = "5";
        bread.stringSet1 = new HashSet<>(Arrays.asList("6", "7", "8"));

        prefsStore.getControlPanel().preheat(pid, bread);

        // Verify preheated
        assertThat(pid.value(prefsStore.int1()).get()).isEqualTo(100);
        assertThat(pid.value(prefsStore.float1()).get()).isEqualTo(2.3f);
        assertThat(pid.value(prefsStore.long1()).get()).isEqualTo(4L);
        assertThat(pid.value(prefsStore.boolean1()).get()).isTrue();
        assertThat(pid.value(prefsStore.string1()).get()).isEqualTo("5");
        assertThat(pid.value(prefsStore.stringSet1()).get()).contains("6", "7", "8");
    }
}