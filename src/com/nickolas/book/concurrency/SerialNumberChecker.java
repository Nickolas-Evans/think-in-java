package com.nickolas.book.concurrency;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SerialNumberChecker implements Runnable {

    private Set<Integer> serialNumberSet = new LinkedHashSet<>(1000);
    private int id;

    public SerialNumberChecker(int id) {
        this.id = id;
    }

    private boolean continueCheck = true;

    public void check(int checkerId) {
        while (continueCheck) {
            int serialNumber = SerialNumberGenerator.generateSerialNumber();
            boolean alreadyContained = !serialNumberSet.add(serialNumber);
            if (alreadyContained) {
                // 为什么输出的数量并不等于 10 ？
                // 因为 System.exit(0); 是直接退出当前应用而不是杀死当前线程？
                System.out.println("i: " + checkerId + ", already contain: " + serialNumber);
//                        System.exit(0);

                // 线程怎么结束自己？
                continueCheck = false;
            }
        }
    }

    public static void main(String[] args) {


        // 启动 10 个线程去校验序列号生成器，如果有重复的元素则关闭线程
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executor.execute(new SerialNumberChecker(i));
        }
        executor.shutdown();
    }

    @Override
    public void run() {
        check(id);
    }
}

