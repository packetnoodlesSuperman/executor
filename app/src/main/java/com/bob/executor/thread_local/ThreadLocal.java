package com.bob.executor.thread_local;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadLocal用于保存某个线程共享变量
 *
 * ThreadLocal的实例代表了一个线程局部的变量，每条线程都只能看到自己的值，
 * 并不会意识到其它的线程中也存在该变量。
 *
 * 它采用采用空间来换取时间的方式，解决多线程中相同变量的访问冲突问题
 * https://www.liaoxuefeng.com/wiki/1016959663602400/1017630786314240
 * https://www.jianshu.com/p/411c40b09a81
 */
public class ThreadLocal<T> {

    private final int threadLocalHashCode = nextHashCode();

    private static AtomicInteger nextHashCode = new AtomicInteger();
    private static final int HASH_INCREMENT = 0x61c88647;
    private int nextHashCode() {
        // 原子方式将当前值与输入值相加并返回结果
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    public ThreadLocal() { }

    public T get() {
        Thread t = Thread.currentThread();
        getMap(t);
        return null;
    }

    ThreadLocalMap getMap(Thread t) {
//        return t.threadLocals;
        return null;
    }

    static class ThreadLocalMap {

    }
}