package com.nickolas.book.net.mindview.util;

import com.nickolas.book.concurrency.DaemonThreadFactory;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//TODO scs ThreadPoolExecutor 这个类是干啥的？
public class DaemonThreadPoolExecutor extends ThreadPoolExecutor {

    public DaemonThreadPoolExecutor() {

        /*
         * To get the values for the constructor base-class call,
         * I simply looked at the Executors.java source code.
         *
         * @see java.util.concurrent.Executors.newCachedThreadPool(java.util.concurrent.ThreadFactory)
         */
        super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new DaemonThreadFactory());
    }
}
