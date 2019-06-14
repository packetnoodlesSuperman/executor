package com.bob.executor.thread_pool;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程池 执行器
 */
public class ThreadPoolExecutor extends AbstractExecutorService {

    private volatile int corePoolSize;
    private volatile int maximumPoolSize;
    private volatile long keepAliveTime;                //线程池维护线程所允许的空闲时间
    private volatile ThreadFactory threadFactory;       //线程创建工程
    private final BlockingQueue<Runnable> workQueue;    //线程池所使用的缓冲队列
    private volatile RejectedExecutionHandler handler;  //线程池对拒绝任务的处理策略

    private static final RejectedExecutionHandler defaultHandler = new AbortPolicy();


    private final ReentrantLock mainLock = new ReentrantLock();

    /******************** 构造函数 ********************/
    public ThreadPoolExecutor(
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue
    ) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                Executors.defaultThreadFactory(), defaultHandler);
    }

    public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        if (corePoolSize < 0 || maximumPoolSize <= 0 || maximumPoolSize < corePoolSize || keepAliveTime < 0) {
            throw new IllegalArgumentException();
        }
        if (workQueue == null || threadFactory == null || handler == null) {
            throw new NullPointerException();
        }
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }

    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new NullPointerException();
        }

        int c = ctl.get();
        // 获取容量（也就是worker数量） ：workerCountOf(c)
        if (workerCountOf(c) < corePoolSize) {
            //让核心线程执行任务
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
        // 线程池处于RUNNING状态，将命令（用户自定义的Runnable对象）添加进workQueue队列
        if (isRunning(c) && workQueue.offer(command)) {

        }

    }

    public boolean remove(Runnable task) {
        boolean removed = workQueue.remove(task);
        return removed;
    }

    private boolean addWorker(Runnable firstTask, boolean core) {
        retry:
        for (; ; ) {
            int c = ctl.get();
            int rs = runStateOf(c);

            if (rs >= SHUTDOWN &&
                    !(rs == SHUTDOWN && firstTask == null && !workQueue.isEmpty())) {
                return false;
            }

            for (; ; ) {
                int wc = workerCountOf(c);  // 获取工作数量
                if (wc >= CAPACITY || wc >= (core ? corePoolSize : maximumPoolSize)) {
                    return false;
                }
                if (compareAndIncrementWorkerCount(c)) {
                    break retry;
                }
            }
        }

        //添加在这里
        boolean workerStarted = false;
        boolean workerAdded = false;
        Worker w = null;
        try {
            w = new Worker(firstTask);
            Thread t = w.thread;
            if (t != null) {
                ReentrantLock mainLock = this.mainLock;
                mainLock.lock();

                int rs = runStateOf(ctl.get());

            }

        } finally {

        }

        return workerStarted;
    }

    private boolean compareAndIncrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect + 1);
    }

    final void reject(Runnable command) {
        handler.rejectedExecution(command, this);
    }

    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    final void runWorker(Worker w) {
        Thread wt = Thread.currentThread();
        Runnable task = w.firstTask;
        w.firstTask = null;
        w.unlock();
    }

    protected boolean tryRelease(int unused) {
//        setExclusiveOwnerThread(null);
//        setState(0);
        return true;
    }

    /******************** 内部类 start ********************/

    /**
     * 内部类 对资源进行复用，减少创建线程的开销
     *
     * Worker继承了AQS抽象类，其重写了AQS的一些方法，
     * 并且其也可作为一个Runnable对象，从而可以创建线程Thread
     */
    private final class Worker extends AbstractQueuedSynchronizer implements Runnable {

        // 版本号
        private static final long serialVersionUID = 6138294804551838833L;

        final Thread thread;    // worker 所对应的线程
        Runnable firstTask;     // worker所对应的第一个任务
        volatile long completedTasks;    // 已完成任务数量

        Worker(Runnable firstTask) {
            setState(-1); // inhibit interrupts until runWorker
            this.firstTask = firstTask;
            this.thread = getThreadFactory().newThread(this);
        }

        @Override
        public void run() {
            runWorker(this);
        }

        public void unlock() { release(1); }
    }

    /**
     * 拒绝策略
     * 拒绝任务：拒绝任务是指当线程池里面的线程数量达到 maximumPoolSize
     * 且 workQueue 队列已满的情况下被尝试添加进来的任务
     *
     * 对拒绝任务抛弃处理，并且抛出异常  Policy(策略)
     */
    public static class AbortPolicy implements  RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            throw new RejectedExecutionException("Task " + r.toString() +
                    " rejected from " +
                    e.toString());
        }
    }

    /******************** 内部类 end ********************/


    /************************* 位操作 状态 *************************/

    // 29位的偏移量  Integer.SIZE = 32  32 - 3 = 29
    private static final int COUNT_BITS = Integer.SIZE - 3;
    // 2^29 - 1  0010 0000 0000 0000 0000 0000 0000 0000 - 1 = 0001 1111 1111 1111 1111 1111 1111 1111
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // 线程运行状态，总共有5个状态，需要3位来表示（所以偏移量的29 = 32 - 3）
    /**
     * RUNNING     :     接受新任务并且处理已经进入阻塞队列的任务
     * SHUTDOWN    ：    不接受新任务，但是处理已经进入阻塞队列的任务
     * STOP        :     不接受新任务，不处理已经进入阻塞队列的任务并且中断正在运行的任务
     * TIDYING     :     所有的任务都已经终止，workerCount为0， 线程转化为TIDYING状态并且调用terminated钩子函数
     * TERMINATED  :    terminated钩子函数已经运行完成
     **/
    //运行中 111 00000000000000000000000000000
    //关闭  000 00000000000000000000000000000
    //停止  001 00000000000000000000000000000
    //整理  010 00000000000000000000000000000
    //终止  011 00000000000000000000000000000
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;



    // 线程池的控制状态（用来表示线程池的运行状态（整形的高3位）和运行的worker数量（低29位）
    // rs runState  wc  WorkCount  我猜测的
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    private static int ctlOf(int rs, int wc) { return rs | wc; }
    //线程池的运行状态
    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    // CAPACITY = 0001 1111 1111 1111 1111 1111 1111 1111 获取运行的worker数量
    private static int workerCountOf(int c)  { return c & CAPACITY; }

    private static boolean isRunning(int c) { return c < SHUTDOWN; }
}
