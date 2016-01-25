package com.github.k24.prefsovensample;

import android.app.Application;

import com.github.k24.prefsoven.Prefs;
import com.github.k24.prefsovensample.prefs.LastUpdatedOven;
import com.github.k24.prefsovensample.prefs.MemoStore;

/**
 * Created by k24 on 2016/01/17.
 */
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Prefs.install(this);
    }

    public static MemoStore memoStore() {
        return Prefs.store(MemoStore.class);
    }

    public static LastUpdatedOven lastUpdatedOven() {
        return Prefs.oven(LastUpdatedOven.class);
    }
}
