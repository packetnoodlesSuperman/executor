package com.bob.executor;


import android.util.Log;

//https://www.jianshu.com/p/c0ba76c3a5c8
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e("MyUncaughHandler", "我退出了" + System.currentTimeMillis());
    }

}
