package com.lucky.ssyx.home;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lucky
 * @date 2023/9/29
 */
//supplyAsync->支持返回值
public class CompletableFutureDemo2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        System.out.println("main begin.....");
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());
            int value = 1024;
            System.out.println("value:" + value);
            return value;
        }, executorService);
        Integer result = completableFuture.get();
        System.out.println("main over..... " + result);
    }
}

