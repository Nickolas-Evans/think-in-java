package com.myself;


import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Interrupting2 implements Runnable {
    final private static Lock lock;
    final private String id;

    static {
        lock = new ReentrantLock();
    }

    private Interrupting2() {
        id = String.valueOf(new Random().nextInt(100));
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Interrupting2());
        executorService.submit(new Interrupting2());
        TimeUnit.MILLISECONDS.sleep(100);
        executorService.shutdownNow();
        System.out.println();
    }

    @Override
    public void run() {
        try {
            System.out.println("I try to get the lock, id: " + id);
            lock.lockInterruptibly();
            System.out.println("I have got the lock, id: " + id);
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException ignored) {

            }
        } catch (InterruptedException e) {
            System.out.println("I'm interrupted, id: " + id);
        } finally {
            lock.unlock();
            System.out.println("I have released the lock, id: " + id);
        }
    }
}
