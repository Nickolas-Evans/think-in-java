package com.myself;

import java.util.concurrent.TimeUnit;

/**
 * In fact, the only place you can call wait( ), notify( ), or notifyAll( ) is
 * within a synchronized method or block (sleep( ) can be called within
 * non-synchronized methods since it doesn’t manipulate the lock). If you call
 * any of these within a method that’s not synchronized, the program will
 * compile, but when you run it, you’ll get an IllegalMonitorStateException
 * with the somewhat nonintuitive message "current thread not owner." This
 * message means that the task calling wait( ), notify( ), or notifyAll( )
 * must "own" (acquire) the lock for the object before it can call any of
 * those methods.
 * <p>
 * 让我来尝试一把
 */
public class TestIllegalMonitorStateException {

    public static void main(String[] args) {
        final Object obj = new Object();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (obj) {
                    while (true) {
                        Thread.yield();
                    }
                }
            }
        });
        t.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            obj.wait();  // 的确报错了
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
