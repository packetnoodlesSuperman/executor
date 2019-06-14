package com.bob.executor.thread_pool;

import java.io.Serializable;

/**
 * 设置 和 获取独占锁的拥有者线程。
 *
 * 自己认为就是一个bean
 * Synchronizer 同步器
 */
public abstract class AbstractOwnableSynchronizer implements Serializable {

    private static final long SerialVersionUID = 3737899427754241961L;

    protected AbstractOwnableSynchronizer () {}

    //独占模式同步的当前所有者
    private transient Thread exclusiveOwnerThread;

    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }

    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }
}
