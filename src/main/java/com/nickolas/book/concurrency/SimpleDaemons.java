package com.nickolas.book.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimpleDaemons implements Runnable {

    @Override
    public String toString() {
        return Thread.currentThread().toString();
    }

    @Override
    public void run() {
        System.out.println("isDaemon: " + Thread.currentThread().isDaemon());
        int i = 0;

        while (true) {
            System.out.println(this + ": " + String.valueOf(i++));

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException("in thread, " + e.getMessage());
            }
        }


    }

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(new SimpleDaemons());
        thread.setDaemon(true);
//        thread.start();
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            executorService.execute(thread); // 往 execute 方法里投一个 Thread 蛇用没有
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        System.out.println("One daemon has started.");

        TimeUnit.SECONDS.sleep(1);
    }
}
