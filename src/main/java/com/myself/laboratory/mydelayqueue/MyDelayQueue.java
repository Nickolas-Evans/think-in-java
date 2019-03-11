package com.myself.laboratory.mydelayqueue;

import javax.annotation.Nonnull;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

/**
 * DelayQueue 内部是链表？
 */
public class MyDelayQueue<T extends Delayed> extends DelayQueue<T> {

    @Override
    public void put(T t) {
        super.put(t);
    }

    /**
     * Q: java.util.concurrent.DelayQueue#take() 会怎么调用 Delayed.compareTo() 方法？
     * A: 在往 DelayQueue 中塞值的时候就会调用 compareTo 方法进行排序，take 的时候直接take queue[0]
     * java.util.PriorityQueue#siftUpComparable(int, java.lang.Object)
     *
     */
    @Override
    @Nonnull
    public T take() throws InterruptedException {
        return super.take();
    }
}
