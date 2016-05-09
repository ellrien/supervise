package com.baosaas.supervise.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ellrien on 2015/11/16.
 */
public class AppContext extends Application {

    public static Context appContext;

    private static AppContext mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        mInstance = this;
    }

    public static AppContext getInstance() {
        return mInstance;
    }
}
