package com.book.concurrency;

import java.util.concurrent.TimeUnit;

public class DaemonDontRunFinally implements Runnable {

    @Override
    public void run() {
        System.out.println("DaemonDontRunFinally start");
        int i = 0;
        try {
            ++i;
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            ++i;
            ++i;
            System.out.println("InterruptedException"); // 不管是否为守护进程，都看不到输出
        } finally {
            // 如果是守护进程，则不会执行第 23 行，但是却能执行第 24 行？
            ++i;
            ++i;
            ++i;
            System.out.println("i=" + i);
            System.out.println("DaemonDontRunFinally be interrupted");
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonDontRunFinally());
        thread.setDaemon(false); // 注释掉该行，则 finally 语句块能被执行
        thread.start();
    }
}
