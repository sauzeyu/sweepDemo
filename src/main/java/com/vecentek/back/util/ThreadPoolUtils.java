package com.vecentek.back.util;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * 单例模式创建线程池
 * 该模式可以保证程序中只创建一个线程池
 */
public class ThreadPoolUtils {
    private ThreadPoolUtils() {
    }

    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1, 2, TimeUnit.HOURS, new ArrayBlockingQueue<Runnable>(20));

    public static ExecutorService getThreadPool() {
        return threadPool;
    }
}
