package com.myself;

import java.util.concurrent.TimeUnit;

/**
 * When you run the program, you’ll see that, unlike an I/O call,
 * interrupt( ) can break out of a call that’s blocked by a mutex.19
 * <p>
 * Thread.interrupt() 不能中断 IO ？ 只是设置中断状态
 * TODO 怎么线程是处于阻塞状态还是处于被中断状态？
 *
 * 感觉更准确地说是无法优雅地中断被 IO 阻塞的线程
 */
public class InterruptIO {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            Integer i = null;
            try {
                i = System.in.read();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("i: " + i);
            }
        });
        System.out.println("status1: " + t.isInterrupted());
        System.out.println("status2: " + t.isAlive());
        System.out.println("status3: " + t.getState());
        System.out.println();

        t.start();
        System.out.println("status1: " + t.isInterrupted());
        System.out.println("status2: " + t.isAlive());
        System.out.println("status3: " + t.getState());
        System.out.println();

        TimeUnit.SECONDS.sleep(1);

        t.interrupt();
        System.out.println("status1: " + t.isInterrupted());
        System.out.println("status2: " + t.isAlive());
        System.out.println("status3: " + t.getState());
        System.out.println();

        t.interrupt();
        System.out.println("status1: " + t.isInterrupted());
        System.out.println("status2: " + t.isAlive());
        System.out.println("status3: " + t.getState());
        System.out.println();

        t.interrupt();
        System.out.println("status1: " + t.isInterrupted());
        System.out.println("status2: " + t.isAlive());
        System.out.println("status3: " + t.getState());
        System.out.println();

        System.exit(0);
    }
}
