package com.bob.executor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = Thread.currentThread();
        Log.e("Executor_custom", thread.getName()); //main

    }

    public static void main(String[] args) throws InterruptedException {


        final ThreadLocal<Boolean> mBooleanThreadLocal=new ThreadLocal<Boolean>(){

            @Override
            protected Boolean initialValue() {

                //初始值是false
                return false;
            }
        };
        mBooleanThreadLocal.set(true);

        System.out.println("[Thread#main]mBooleanThreadLocal=" +mBooleanThreadLocal.hashCode()+"  "+ mBooleanThreadLocal.get());

        new Thread("Thread#1") {
            @Override
            public void run() {
                mBooleanThreadLocal.set(false);
                System.out.println( "[Thread#1]mBooleanThreadLocal="+mBooleanThreadLocal.hashCode() +"  "+ mBooleanThreadLocal.get());
            };
        }.start();

        new Thread("Thread#2") {
            @Override
            public void run() {
                System.out.println( "[Thread#2]mBooleanThreadLocal="+mBooleanThreadLocal.hashCode() + "  "+mBooleanThreadLocal.get());
            };
        }.start();
    }
}
