package com.myself;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestExecutorServiceShutdown {


    //TODO scs 看到 Using explicit Lock and Condition objects
    //TODO scs 验证 This method does not wait for previously submitted tasks to
    //TODO scs 验证 complete execution.
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println();
                }
            }
        });
        executorService.shutdown();
    }
}
