package com.idisfkj.loadingcirclerview;

import android.app.Application;
import android.content.Context;

/**
 * Created by idisfkj on 17/3/12.
 * Email : idisfkj@gmail.com.
 */

public class App extends Application {

    public static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
    }
}
