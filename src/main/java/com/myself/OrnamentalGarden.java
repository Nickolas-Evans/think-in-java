package com.myself;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// 花园有 4 个门，每个人都统计各自进入的人数，同时统计花园的总人数
// 末了输出花园的总人数以及各个门进入人数的总和
class Entrance implements Runnable {
    private static volatile boolean canceled = false;
    private final int id;
    private int total = 0;

    Entrance(int id) {
        this.id = id;
    }

    static void cancel() {
        canceled = true;
    }

    private void entrySomeone() {
        total++;
    }

    int getTotal() {
        return total;
    }

    // 不停地进人，怎么停？
    @Override
    public void run() {
        while (!canceled) {
            entrySomeone();
            Thread.yield();
            int gardenTotal = OrnamentalGarden.entrySomeone();
            Thread.yield();
            System.out.println("entrance #" + id + ", " +
                    "entrance total: " + getTotal() + ", " +
                    "garden total: " + gardenTotal);
            Thread.yield();
//            if (new Random().nextBoolean()) {
//                try {
//                    TimeUnit.MILLISECONDS.sleep(500);
//                } catch (InterruptedException e) {
//                    System.err.println(e.getMessage());
//                }
//            }
        }
    }
}

public class OrnamentalGarden {

    private static int total = 0;
    private static List<Entrance> entranceList = new ArrayList<>();

    synchronized static int entrySomeone() {
        return ++total; //TODO scs 都不需要做 get 方法的同步？
    }

    private static int getTotalFromEntrances() {
        int total = 0;
        for (Entrance entrance : entranceList) {
            total += entrance.getTotal();
        }
        return total;
    }

    private void addEntrance(Entrance entrance) {
        entranceList.add(entrance);
    }

    public static void main(String[] args) throws InterruptedException {
        OrnamentalGarden garden = new OrnamentalGarden();
        int entranceNumber = 4;

        ExecutorService executorService = Executors.newFixedThreadPool(entranceNumber);
        for (int i = 0; i < entranceNumber; i++) {
            Entrance entrance = new Entrance(i);
            garden.addEntrance(entrance);
            executorService.execute(entrance);
        }
        executorService.shutdown();

        TimeUnit.SECONDS.sleep(10);

        Entrance.cancel();
        if (!executorService.awaitTermination(250, TimeUnit.MILLISECONDS)) {
            return;
        }
        System.out.println("garden total: " + total);
        // 怎么保证统计时候，所有 entrance 都已经停止进人？
        // 使用 java.util.concurrent.ExecutorService.awaitTermination
        System.out.println("garden total from entrances: " + OrnamentalGarden.getTotalFromEntrances());

    }
}
