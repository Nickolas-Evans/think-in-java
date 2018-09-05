package com.myself;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 被 interrupted 的 Thread 会被回收吗？
 */
public class Interrupting {

    public static void main(String[] args)  {
        SoftReference<Thread> threadToBeInterrupted = new SoftReference<>(new Thread(() -> {
            int i = 0;
            while (!Thread.currentThread().isInterrupted()){
                System.out.println("i:" + i++);
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(new InterruptedException());
                }
            }
        }));
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(threadToBeInterrupted.get());
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdownNow();

        System.out.println("threadToBeInterrupted == null：  " + (threadToBeInterrupted.get() == null));

        System.gc();

        boolean executorTerminated = false;

        try {
            executorTerminated = executorService.awaitTermination(1,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("22222");
        }
        System.out.println("executorTerminated: " + executorTerminated);

        //TODO scs 不会被回收？
        //TODO scs 线程什么情况下才会被回收？
        System.out.println("threadToBeInterrupted == null：  " + (threadToBeInterrupted.get() == null));
//        System.exit(0);
    }

}
