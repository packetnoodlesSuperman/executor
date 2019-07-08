package com.bob.executor.lock;

public class Synchronized {

    public static void main(String[] args ) {
        final Son son = new Son();
        new Thread(new Runnable() {
            @Override
            public void run() {
                son.name(son);
            }
        }, "001").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                son.name(son);
            }
        }, "002").start();




    }

    static class Father {
        public synchronized void name(Son son) {
            if (this instanceof Son) {
                boolean b = Thread.holdsLock(this);
                System.out.println(Thread.currentThread().getName() + " --- Father---- " + b);
            }
        }
    }

    static class Son extends Father {

        @Override
        public synchronized void name(Son son) {
            boolean b = Thread.holdsLock(son);
            System.out.println(Thread.currentThread().getName()+" --- Son---- " + b);
            super.name(son);
            //虽然调用的父类方法，但是其实还是自己对象里面的方法
        }
    }

}
