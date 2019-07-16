package com.bob.executor.thread_local;

import java.lang.ref.WeakReference;
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

    public static void main(String[] args) {
        final java.lang.ThreadLocal local = new java.lang.ThreadLocal(){
            @Override
            protected Object initialValue() {
                return new String("test");
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                local.set("001");
                System.out.println(local.get());

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                local.set("002");
                System.out.println(local.get());

            }
        }).start();
    }


//
//    private final int threadLocalHashCode = nextHashCode();
//
//    private static AtomicInteger nextHashCode = new AtomicInteger();
//    private static final int HASH_INCREMENT = 0x61c88647;
//    private int nextHashCode() {
//        // 原子方式将当前值与输入值相加并返回结果
//        return nextHashCode.getAndAdd(HASH_INCREMENT);
//    }
//
//    public ThreadLocal() { }
//
//    public void set(T value) {
//        Thread t = Thread.currentThread();
//        ThreadLocalMap map = getMap(t);
//        if (map != null) {
//            map.set(this, value);
//        } else {
//            createMap(t, value);
//        }
//    }
//
//    private void createMap(Thread t, T firstValue) {
//        t.threadLocals = new ThreadLocalMap(this, firstValue);
//    }
//
//    /**
//     * @return 查询
//     */
//    public T get() {
//        Thread t = Thread.currentThread();
//        ThreadLocalMap map = getMap(t);
//        if (map != null) {
//            ThreadLocalMap.Entry e = map.getEntry(this);
//            if (e != null) {
//                T result = (T) e.value;
//                return result;
//            }
//        }
//        return setInitialValue();
//    }
//
//    private T setInitialValue() {
//        T value = initialValue();
//        Thread t = Thread.currentThread();
//        ThreadLocalMap map = getMap(t);
//        if (map != null) {
//            map.set(this, value);
//        } else {
//            createMap(t, value);
//        }
//        return value;
//    }
//
//    private T initialValue() {
//        return null;
//    }
//
//
//    ThreadLocalMap getMap(Thread t) {
//        return t.threadLocals;
//    }
//
//
//    /**
//     * 内部类
//     */
//    static class ThreadLocalMap {
//
//        static class Entry extends WeakReference<java.lang.ThreadLocal<?>> {
//
//            Object value;
//
//
//            public Entry(ThreadLocal<?> k, Object v) {
//                super(k);
//                value = v;
//            }
//        }
//
//        private static final int INITIAL_CAPACITY = 16;
//        private Entry[] table;
//
//        ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
//            table = new Entry[INITIAL_CAPACITY];
//            int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
//            table[i] = new Entry(firstKey, firstValue);
//            size = 1;
//            setThreshold(INITIAL_CAPACITY);
//        }
//
//        public void set(ThreadLocal<?> tThreadLocal, Object value) {
//
//        }
//
//        private Entry getEntry(ThreadLocal<?> key) {
//            return null;
//        }
//    }
}