package com.bob.executor;


/**
 * @FunctionalInterface
 * 它们主要用在Lambda表达式和方法引用（实际上也可认为是Lambda表达式）上
 */
@FunctionalInterface
public interface Runnable {

    public abstract void run();

}
