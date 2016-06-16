package com.nb.leakcanary_handlerdemo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @项目名 Leakcanary_handlerDemo
 * @作者 侯紫睿
 * @时间 2016/6/16 0016  下午 4:38
 * @描述 ${TODD}
 * @SVN版本号 $$Rev$$
 * @修改人 $$Author$$
 * @修改内容 $$Date$$
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
