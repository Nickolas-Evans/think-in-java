package com.book.concurrency;

import java.util.concurrent.ThreadFactory;

/*
 * SimpleDaemons.java creates explicit Thread objects in order to set their daemon flag.
 * It is possible to customize the attributes (daemon, priority, name) of threads
 * created by Executors by writing a custom ThreadFactory:
 */
public class DaemonThreadFactory implements ThreadFactory {

    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     * create a thread is rejected
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    }
}
