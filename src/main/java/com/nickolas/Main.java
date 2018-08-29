package com.nickolas;

import com.nickolas.book.net.mindview.util.DaemonThreadPoolExecutor;

public class Main {
    // volatile 除了修饰基础类型外，能修饰引用类型吗？
    volatile DaemonThreadPoolExecutor executor; // 可以的！
}
