package com.bob.executor.thread_pool;

public interface ThreadFactory {

    Thread newThread(Runnable r);

}
