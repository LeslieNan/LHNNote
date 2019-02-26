package com.lhn.yourstory.lhnnote;

import android.app.Application;
import android.content.Context;

/**
 * Created by lenovo on 2018/10/8.
 * 全局application类
 */

public class MyApplication extends Application {

    private static Context context;

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

    /**
     * 单例模式中获取唯一的MyApplication实例
     *
     * @return
     */
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;

    }
}
