package com.book.concurrency;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 允许多线程操作 ThreadLocalVariableHolder 中的 ThreadLocal
 * 并输出操作结果
 */
class Accessor implements Runnable {

    private int id;

    public Accessor(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        int i = 0;
        while (!Thread.currentThread().isInterrupted() && i++ < 5) {
//        while (true) { // while true 为啥没被自动中断？
           /*
           因为：
            * <p>There are no guarantees beyond best-effort attempts to stop
            * processing actively executing tasks.  For example, typical
            * implementations will cancel via {@link Thread#interrupt}, so any
            * task that fails to respond to interrupts may never terminate.
           */
            ThreadLocalVariableHolder.increment();
            System.out.println("id: " + id + ", int: " + ThreadLocalVariableHolder.get());
        }
    }
}

/**
 * 内置一个 ThreadLocal 并提供 increment 和 get 方法
 */
public class ThreadLocalVariableHolder {

    // ThreadLocal 可以定义成 static !!!!!
    // 所以ThreadLocalVariableHolder.get .increment 方法也就可以定义成 static 了
    private static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> new Random().nextInt());

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {
            executorService.execute(new Accessor(i));
        }
        TimeUnit.SECONDS.sleep(10);
        executorService.shutdownNow();

    }

    // get 和 increment 需要保证同步吗？
    // 不需要，这两个方法并没有被并发调用
    // Notice that increment( ) and get( ) are not synchronized,
    // because ThreadLocal guarantees that no race condition can occur.
    public static int get() {
        return threadLocal.get();
    }

    public static void increment() {
        threadLocal.set(get() + 1);
    }
}
