package com.myself.concurrency.waxomatic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * wait() 方法本身并不会释放锁
 */
public class Temp {

    public static void main(String[] args) {
        final Object obj = new Object();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            synchronized (obj) {
                System.out.println("0");
                try {
                    obj.wait(); // 此时就会直接释放锁，而无视 synchronized
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("1");
            }
        });
        executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (obj) {
                try {
                    System.out.println("i get the object lock");
                    int i = 0;
                    for (; i < 20; i++) {
                        System.out.println(i);
                        TimeUnit.SECONDS.sleep(1);
                    }
                    obj.notifyAll(); // 此时并不会释放锁
                    System.out.println("im already to release the object lock");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executorService.shutdown();
    }
}
