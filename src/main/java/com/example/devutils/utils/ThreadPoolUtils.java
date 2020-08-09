package com.example.devutils.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by AMe on 2020-08-09 15:48.
 */
public class ThreadPoolUtils {

    private static final ExecutorService singleThreadExecutor = newSingleThreadExecutor();

    public static ExecutorService newSingleThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return Executors.newSingleThreadExecutor(threadFactory);
    }

    public static ExecutorService newFixedThreadPool(int fixedNum) {
        return Executors.newFixedThreadPool(fixedNum);
    }

    public static ExecutorService newFixedThreadPool(int fixedNum, ThreadFactory threadFactory) {
        return Executors.newFixedThreadPool(fixedNum, threadFactory);
    }

    public static ExecutorService newCachedThreadPool() {
        return Executors.newCachedThreadPool();
    }

    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        return Executors.newCachedThreadPool(threadFactory);
    }

    public static ExecutorService newSingleThreadScheduledExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    public static ExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
        return Executors.newSingleThreadScheduledExecutor(threadFactory);
    }

    public static ExecutorService newScheduledThreadPool(int corePoolSize) {
        return Executors.newScheduledThreadPool(corePoolSize);
    }

    public static ExecutorService newScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory) {
        return Executors.newScheduledThreadPool(corePoolSize, threadFactory);
    }

    public static ExecutorService newWorkStealingPool() {
        return Executors.newWorkStealingPool();
    }

    public static ExecutorService newWorkStealingPool(int parallelism) {
        return Executors.newWorkStealingPool(parallelism);
    }

    public static ExecutorService newThreadPoolExecutor(
        int corePoolSize, int maximumPoolSize,
        long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
        ThreadFactory threadFactory, RejectedExecutionHandler handler
    ) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static void submit(Runnable runnable) {
        singleThreadExecutor.submit(runnable);
    }

    public static <T> Future<T> submit(Callable<T> runnable) {
        return singleThreadExecutor.submit(runnable);
    }

    public static <T> Future<T> submit(Runnable runnable, T retVal) {
        return singleThreadExecutor.submit(runnable, retVal);
    }

    public static void execute(Runnable runnable) {
        singleThreadExecutor.execute(runnable);
    }

}
