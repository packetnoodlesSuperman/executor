package com.bob.executor.thread_pool;

import java.io.Serializable;

public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer implements Serializable{

    private static final long serialVersionUID = 7373984972572414691L;

    protected AbstractQueuedSynchronizer() { }

    private volatile int state;

    protected final int getState() {
        return state;
    }

    protected final void setState(int newState) {
        state = newState;
    }

    public final boolean release(int arg) {
//        if (tryRelease(arg)) {
////            Node h = head;
////            if (h != null && h.waitStatus != 0)
////                unparkSuccessor(h);
////            return true;
////        }
        return false;
    }

    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }

}
