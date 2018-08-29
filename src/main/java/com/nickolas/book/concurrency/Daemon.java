package com.nickolas.book.concurrency;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Daemon extends Thread {

    private static Thread[] daemonSpawns = new Thread[10];

    private static final ThreadFactory THREAD_FACTORY = new BasicThreadFactory();

    @Override
    public void run() {
        for (int i = 0; i < daemonSpawns.length; i++) {
//            daemonSpawns[i] = new Thread(() -> {
//                while (true){
//                    Thread.yield();
//                }
//            }); // 如果换用 ThreadFactory 来创建 Thread 呢？
            daemonSpawns[i] = THREAD_FACTORY.newThread(() -> {
                while (true) {
                    Thread.yield();
                }
            });
            daemonSpawns[i].start();
        }
        System.out.println("Daemon spawns started.");
        for (int i = 0; i < daemonSpawns.length; i++) {
            System.out.println("daemonSpawns[" + i + "].isDaemon(): " + daemonSpawns[i].isDaemon());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Daemon aDaemonThread = new Daemon();
//        aDaemonThread.setDaemon(true);
        aDaemonThread.start();
        System.out.println("Daemon started.");
        TimeUnit.SECONDS.sleep(1);
    }
}
