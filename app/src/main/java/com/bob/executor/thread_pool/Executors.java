package com.bob.executor.thread_pool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工具类
 */
public class Executors {

    /**
     * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
     */
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(
                nThreads,
                nThreads,
                0L,
                TimeUnit.MICROSECONDS,
                new LinkedBlockingQueue<Runnable>()
        );
    }

    /**
     * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
     */
    public static ExecutorService newSingleThreadExecutor() {
        return new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MICROSECONDS,
                new LinkedBlockingQueue<Runnable>()
        );
    }

    /**
     * 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程
     */
    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    /**
     * 创建一个定长线程池，支持定时及周期性任务执行
     */
//    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
//        return new ScheduledThreadPoolExecutor(corePoolSize);
//    }


    public static ThreadFactory defaultThreadFactory() {
        return new DefaultThreadFactory();
    }

    private static class DefaultThreadFactory implements ThreadFactory {

        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final String namePrefix;

        public DefaultThreadFactory() {
            //Java安全管理器
            SecurityManager s = System.getSecurityManager();
            //获取当前线程的线程组
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        /**
         * 创建线程
         */
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(
                    group, //ThreadGroup group,
                    r,     //Runnable target,
                    namePrefix + threadNumber.getAndIncrement(), //String name,
                    0 //long stackSize
            );
            if (t.isDaemon()){
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
