package com.github.k24.prefsoven;

import android.os.Build;

import com.github.k24.prefsoven.sample.TestPrefs;
import com.github.k24.prefsoven.sample.TestPrefsStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by k24 on 2016/01/15.
 */
@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
@RunWith(RobolectricTestRunner.class)
public class PrefsOvenVendorTest {

    private PrefsOvenVendor ovenVendor;

    @Before
    public void setUp() throws Exception {
        ovenVendor = PrefsOvenVendor.vendor(RuntimeEnvironment.application);
        ovenVendor.create(TestPrefs.class).getControlPanel().clear();
        ovenVendor.createStore(TestPrefsStore.class).getControlPanel().clear();
    }

    @Test
    public void createOven_noAnnotation() throws Exception {

    }

    @Test
    public void createStoreOven_noAnnotation() throws Exception {
        TestPrefsStore prefsStore = PrefsOvenVendor.vendor(RuntimeEnvironment.application).createStore(TestPrefsStore.class);
    }

}