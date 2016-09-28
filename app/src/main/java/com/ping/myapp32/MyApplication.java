package com.ping.myapp32;

import android.app.Application;
import android.content.Context;

/**
 * Created by 2-1Ping on 2016/9/5.
 */
public class MyApplication extends Application {
    public  static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }
}
