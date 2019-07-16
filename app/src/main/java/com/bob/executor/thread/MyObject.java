package com.bob.executor.thread;

//默认集成Object
public class MyObject extends Object {

    //shadow$_monitor_和shadow$_klass_ 是Android sdk21之后Object增加的两个字段

    //用来说明类型的
    private transient Class<?> shadow$_klass_;
    //用来表明地址的
    private transient int shadow$_monitor_;

    public final Class<?> getClass_() {
        return shadow$_klass_;
    }

    /**
     * 用来判断两个对象是否相等的函数，默认的是两个对象的地址比较，
     * 在使用Compare计算的时候，往往需要重写这个函数
     */
    public boolean equals(Object obj) {
        return (this == obj);
    }

    /**
     * 返回的是这个对象的hash值
     */
    //public int hashCode() { return identityHashCode(this); --> private static native int identityHashCodeNative(Object obj); }

    /**
     * clone();调用该函数需要实现 Cloneable，
     * 否则会抛出  CloneNotSupportedException的异常
     */
    protected Object clone() throws CloneNotSupportedException {
        if (!(this instanceof Cloneable)) {
            throw new CloneNotSupportedException("Class " + getClass().getName() +
                    " doesn't implement Cloneable");
        }

        return internalClone();
    }

    private native Object internalClone();

    /**
     * 唤醒被这个对象的monitor标记等待的线程，
     * 如果线程的数量大于1，那么被唤醒的线程是由VM自行决定的。
     * 注意被唤醒的线程不一定立即执行，至少要等待当前调用notify的线程释放这个对象的monitor，
     * 或者等待其他正好同步锁定该对象的线程释放了该对象。
     * 当然了，也不是任何地方都可以调用notify的，调用的地方必须持有对象的monitor，
     * 可以有以下几种情况：
     * 1.在一个同步（ synchronized）方法中；
     * 2.在一段该对象的同步代码块中；
     * 3.如果这个变量是类变量（static），同步的静态方法也持有
     */
    //public final native void notify();

    //public final native void notifyAll();

    /**
     * 通知当前线程挂起，当对象的notify或者notifyAll被调用的时候才会被重新唤醒，wait了的thread是可以被中断（interrupt）的。
     * 当线程wait的时候，这个线程其实丢失了对象的monitor，当被notify的时候，会在程序执行前重新请求到对象的monitor。
     * 还有两个wait函数是带参数的，参数指明了线程的wait时长，如果在这个时长内，线程没有被唤醒，那么当时间到达的时候，这个线程也会被重新唤醒。
     * 其他的与上面的wait一致，对了时间为0，说明没有超时时间，时间不可以小于零，否则抛出 IllegalArgumentException
     */
    //public final native void wait() throws InterruptedException;

    //public final native void wait(long millis, int nanos) throws InterruptedException;

}