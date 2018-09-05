package com.book.concurrency;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * SleepBlock is an example of interruptible blocking,
 * whereas IOBlocked and SynchronizedBlocked are uninterruptible blocking.
 */
class SleepBlocked implements Runnable {

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("0. InterruptedException"); // 这行代码与 cancel(true) 之后的那行代码也是并行执行的
        }
        System.out.println("1. Exiting SleepBlocked.run()");
        //  Q：一旦 SleepBlocked 示例被打断，触发 InterruptedException 之后，
        // 是否是优先执行完 com.book.concurrency.SleepBlocked.run
        // 才会执行 f.cancel(true); 之后的代码？ 也即，线程排出异常后就成了，顺序执行？

        // A： 并不是
    }
}

/**
 * SleepBlock is an example of interruptible blocking,
 * whereas IOBlocked and SynchronizedBlocked are uninterruptible blocking.
 */
class IOBlocked implements Runnable {
    private InputStream in;

    IOBlocked(InputStream is) {
        in = is;
    }

    @Override
    public void run() {
        try {
            System.out.println("1. Waiting for read():");
            int i = in.read();
            System.out.println("2. in.read(): " + i);
        } catch (InterruptedIOException e) {
            // Runnable.run() 执行过程中被外部中断的话，自身并不会抛出异常，
            // 这有别于 TimeUnit.XXX.sleep() 方法 ?
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("4A. Interrupted from blocked I/O");
            } else {
                System.out.println("4B. e.getMessage(): " + e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("5A. Exiting IOBlocked.run()");
    }
}

/**
 * SleepBlock is an example of interruptible blocking,
 * whereas IOBlocked and SynchronizedBlocked are uninterruptible blocking.
 * <p>
 * This is accomplished in the constructor by creating an instance of an
 * anonymous Thread class that acquires the object lock by calling f()
 * (the thread must be different from the one driving run() for
 * SynchronizedBlock because one thread can acquire an object lock multiple
 * times). Since f() never returns, that lock is never released.
 * SynchronizedBlock.run() attempts to call f() and is blocked waiting for
 * the lock to be released.
 * <p>
 * 当前实例的锁应该一直被构造函数霸占啊，怎么会被 run() 方法获取到的？
 * <p>
 * You’ll see from the output that you can interrupt a call to sleep()
 * (or any call that requires you to catch InterruptedException).
 * However, you cannot interrupt a task that is trying to acquire a
 * synchronized lock or one that is trying to perform I/O. This is a
 * little disconcerting, especially if you’re creating a task that
 * performs I/O, because it means that I/O has the potential of
 * locking your multi-threaded program. Especially for Web-based programs,
 * this is a concern.
 *
 * TODO scs So,how to solve this.
 *
 * A heavy-handed but sometimes effective solution to this problem is to
 * close the underlying resource on which the task is blocked:
 */
class SynchronizedBlocked implements Runnable {

    private synchronized void f() {

        int i = 0;

        while (true) {// Never releases lock until the end

            System.out.println("f() get the object lock. i:" + i++ + ", " + Thread.currentThread().getName());
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("AAAAAA");
            }
        }

    }

    public SynchronizedBlocked() {
        // Lock acquired by this thread
        new Thread(this::f).start();
    }

    @Override
    public void run() {
        System.out.println("1. Trying to call f(), " + Thread.currentThread().getName()); // 这行为什么还是能被执行？
        f(); // blocked. because never access the object lock
        System.out.println("Never access! Exiting SynchronizedBlocked.run()");
    }
}

/**
 * Each task represents a different kind of blocking.
 * <p>
 * The program proves that I/O and waiting on a synchronized lock are not interruptible,
 * but you can also anticipate this by looking at the code—no InterruptedException
 * handler is required for either I/O or attempting to call a synchronized method.
 * <p>
 * The first two classes are straightforward:
 * The run() method calls sleep() in the first class and read() in the second.
 * To demonstrate SynchronizedBlocked, however, we must first acquire the lock.
 */
public class Interrupting {
    private static ExecutorService exec =
            Executors.newCachedThreadPool();

    private static void test(Runnable r) throws InterruptedException {
        Future<?> f = exec.submit(r);
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("3. Interrupting " + r.getClass().getName());
        f.cancel(true); // Interrupts if running
        System.out.println("4. Interrupt sent to " + r.getClass().getName());
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 20; i++) {
//            test(new SleepBlocked());
            test(new IOBlocked(System.in));
//            test(new SynchronizedBlocked());
            TimeUnit.SECONDS.sleep(3);
            System.out.println("5. Aborting with System.exit(0)");
            System.exit(0); // ... since last 2 interrupts failed
            System.out.println();
        }
    }
} 