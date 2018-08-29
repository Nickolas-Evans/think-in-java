package com.book.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimplePriorities implements Runnable {

    private final int priority;

    public SimplePriorities(int priority) {
        this.priority = priority;
    }

    private int countDown = 5;

    private volatile double d; // No optimization

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        Thread.currentThread().setPriority(priority);
//        while (true) {
//
//            // An expensive, interruptable operation:
//            for (int i = 1; i < 100000; i++) {
//                d += (Math.PI + Math.E) / (double) i;
//                if (i % 1000 == 0) {
                    Thread.yield();
//                }
//            }
//
            System.out.println(this);
//
//            if (--countDown == 0) {
//                return;
//            }
//        }
    }

    @Override
    public String toString() {
        return Thread.currentThread().toString() + ": " + countDown;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.execute(new SimplePriorities(Thread.MIN_PRIORITY));
        }
        executorService.execute(new SimplePriorities(Thread.MAX_PRIORITY));
        executorService.shutdown();
    }
}
