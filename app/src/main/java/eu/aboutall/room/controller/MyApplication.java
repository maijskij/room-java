package eu.aboutall.room.controller;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by denis on 30/08/2017.
 */

public class MyApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

    }
}
