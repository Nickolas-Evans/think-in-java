package com.slef;

import java.util.Date;

/**
 * 只是不能捕捉从 thread 中 throw 的异常，在 thread 内部进行捕捉是可以的
 */
public class ExceptionInThread implements Runnable {

    public static void main(String[] args) {
//        ExecutorService executor = Executors.newCachedThreadPool();
//        executor.execute(new ExceptionInThread());
//        executor.shutdown();
        try {
            new Thread(new ExceptionInThread()).start();
            System.out.println("thread started.");
        }catch (Exception e){
            System.out.println(e.getMessage()); // CANNOT CATCH THE EXCEPTION
        }
    }


    @Override
    public void run() {
//        try {
            long time = new Date().getTime();
            if (time > 0) {
                System.out.println("time:" + time);
                throw new RuntimeException("time > 0");
            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//
//        }
    }
}
