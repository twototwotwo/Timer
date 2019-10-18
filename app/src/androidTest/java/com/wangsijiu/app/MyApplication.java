package com.wangsijiu.app;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by Administrator on 2019/8/12.
 */
public class MyApplication extends Application {
    public MyApplication(){
        super();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
