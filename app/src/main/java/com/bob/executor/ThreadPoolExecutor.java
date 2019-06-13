package com.bob.executor;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutor extends AbstractExecutorService {

    private volatile int corePoolSize;

    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new NullPointerException();
        }

        int c = ctl.get();
        if (workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
    }

    private boolean addWorker(Runnable firstTask, boolean core) {
        retry:
        for (;;) {
            int c = ctl.get();
            int rs = runStateOf(c);
        }
    }

    /**
     * 内部类
     */
    private final class Worker extends AbstractQueuedSynchronizer implements Runnable {

        private static final long serialVersionUID = 6138294804551838833L;

        @Override
        public void run() {

        }
    }


    // 29位的偏移量  11111111 11111111 11111111 11111100
    private static final int COUNT_BITS = Integer.SIZE - 3;
    // 线程运行状态，总共有5个状态，需要3位来表示（所以偏移量的29 = 32 - 3）
    /**
     * RUNNING     :     接受新任务并且处理已经进入阻塞队列的任务
     * SHUTDOWN    ：    不接受新任务，但是处理已经进入阻塞队列的任务
     * STOP        :     不接受新任务，不处理已经进入阻塞队列的任务并且中断正在运行的任务
     * TIDYING     :     所有的任务都已经终止，workerCount为0， 线程转化为TIDYING状态并且调用terminated钩子函数
     * TERMINATED  :    terminated钩子函数已经运行完成
     **/
    //运行中 111 00000000000000000000000000000
    //关闭 000 00000000000000000000000000000
    //停止 001 00000000000000000000000000000
    //整理 010 00000000000000000000000000000
    //终止 011 00000000000000000000000000000
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // 线程池的控制状态（用来表示线程池的运行状态（整形的高3位）和运行的worker数量（低29位）
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int ctlOf(int rs, int wc) { return rs | wc; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }
}
