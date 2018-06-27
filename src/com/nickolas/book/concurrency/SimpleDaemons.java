package com.nickolas.book.concurrency;

import java.util.concurrent.TimeUnit;

public class SimpleDaemons implements Runnable {

    @Override
    public String toString() {
        return Thread.currentThread().toString();
    }

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
        int i = 0;
        while (true) {
            System.out.println(this + ": " +String.valueOf(i++));
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(new SimpleDaemons());
        thread.setDaemon(true);
        thread.start();

        System.out.println("One daemon has started.");

        TimeUnit.SECONDS.sleep(3);
    }
}
