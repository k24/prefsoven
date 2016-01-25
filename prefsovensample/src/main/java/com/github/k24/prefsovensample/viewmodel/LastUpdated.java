package com.github.k24.prefsovensample.viewmodel;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;

/**
 * Created by k24 on 2016/01/18.
 */
public class LastUpdated {
    public long updatedAt;
    public String summary;

    @NonNull
    public static LastUpdated newInstance(@NonNull Memo memo) {
        LastUpdated lastUpdated = new LastUpdated();
        lastUpdated.summary = memo.subject + " " + memo.body;
        lastUpdated.updatedAt = System.currentTimeMillis();
        return lastUpdated;
    }

    public static String formatDatetime(long datetimeMillis) {
        return SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.DEFAULT, SimpleDateFormat.DEFAULT)
                .format(datetimeMillis);
    }
}
