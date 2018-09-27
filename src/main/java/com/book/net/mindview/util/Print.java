package com.book.net.mindview.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Print {
    private static final Logger logger = LogManager.getLogger(Print.class);

    public static void print(String str) {
        logger.info(str);
    }

    public static void printnb(String str) {
        print(str);
    }
}
