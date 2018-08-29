package com.nickolas.book.concurrency;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 允许多线程操作 ThreadLocalVariableHolder 中的 ThreadLocal
 * 并输出操作结果
 */
class Accessor implements Runnable {

    ThreadLocalVariableHolder holder = new ThreadLocalVariableHolder();
    private int id;

    public Accessor(int id) {
        this.id = id;
    }

    @Override
    public void run() {
//        while (Thread.currentThread().isInterrupted()) {
        while (true) { //todo scs while true 为啥没被自动中断？
            holder.increment();
            System.out.println("id: " + id + ", int: " + holder.get());
        }
    }
}

/**
 * 内置一个 ThreadLocal 并提供 increment 和 get 方法
 */
public class ThreadLocalVariableHolder {
    private ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> new Random().nextInt());

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {
            executorService.execute(new Accessor(i));
        }
        TimeUnit.SECONDS.sleep(3);
        executorService.shutdownNow();

    }

    // get 和 increment 需要保证同步吗？
    // 不需要，这两个方法并没有被并发调用
    public int get() {
        return threadLocal.get();
    }

    public void increment() {
        threadLocal.set(get() + 1);
    }
}
