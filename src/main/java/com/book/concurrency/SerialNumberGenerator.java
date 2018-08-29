package com.book.concurrency;

public class SerialNumberGenerator {

    private volatile static int i;

    public static int generateSerialNumber() {
        return i++;
    }
}
