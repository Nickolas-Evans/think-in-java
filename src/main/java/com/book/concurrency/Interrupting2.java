package com.book.concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.book.net.mindview.util.Print.print;

class BlockedMutex {
    private Lock lock = new ReentrantLock();

    BlockedMutex() {
        // Acquire it right away, to demonstrate interruption
        // of a task blocked on a ReentrantLock:
        lock.lock();
    }

    void f() {
        try {
            // This will never be available to a second task
            lock.lockInterruptibly(); // Special call
            print("lock acquired in f()");
        } catch (InterruptedException e) {

            print("Interrupted from lock acquisition in f()");
        }
    }
}

class Blocked2 implements Runnable {
    private BlockedMutex blocked = new BlockedMutex();

    /*
    In Blocked2, the run( ) method will be stopped at the call to blocked.f().
    When you run the program, you’ll see that, unlike an I/O call, interrupt()
    can break out of a call that’s blocked by a mutex.
    */
    @Override
    public void run() {
        print("Waiting for f() in BlockedMutex");
        blocked.f(); // suspending for the lock
        print("Broken out of blocked call");
    }
}

public class Interrupting2 {

    // Note that, although it’s unlikely, the call to t.interrupt()
    // could actually happen before the call to blocked.f( ).
    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new Blocked2());
        t.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Issuing t.interrupt()");
        t.interrupt();
    }
}