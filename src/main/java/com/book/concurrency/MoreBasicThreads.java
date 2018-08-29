package com.book.concurrency;

public class MoreBasicThreads {

    //TODO scs 2018-06-22 21:42:44
    //TODO scs 在本例中，单个线程（main()）在创建所有的 LiftOff 线程。但是，如果多个线程在创建 LiftOff 线程，
    //TODO scs 那么就有可能会有多个 LiftOff 拥有相同的 id。在本章稍后你会了解到这是为什么。
    public static void main(String[] args) {
        for(int i = 0; i < 5; i++){
            new Thread(new LiftOff()).start();
        }
        System.out.println("Waiting for LiftOff!");
    }
}
