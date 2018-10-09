package com.myself;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestExecutorServiceShutdown {




    /*
        看到 Using explicit Lock and Condition objects

    This method does not wait for previously submitted tasks to complete execution.

    It means that the shutdown method returns immediately.
    It does not wait for scheduled and already running tasks to
    complete before returning to its caller. This means that the
    ExecutorService can still take some time to clean up and
    terminate itself (which it will eventually do, after all
    running tasks have completed).
    */
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            for (int i = 0; i < Integer.MAX_VALUE ; ) {
                System.out.println("i: " + i++);
                if(Thread.currentThread().isInterrupted()){
                    System.out.println("interrupted");
                    return;
                }
            }
        });
        TimeUnit.SECONDS.sleep(1);
        executorService.shutdown();
    }
}
