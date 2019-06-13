package com.bob.executor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface Future<V> {

    boolean cancel(boolean mayInterruptIfRunning);

    boolean isCancelled();

    boolean isDone();

    /**
     * @throws InterruptedException
     * @throws ExecutionException
     */
    V get() throws InterruptedException, ExecutionException;

    /**
     * @param timeout
     * @param unit
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    V get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException;

}
