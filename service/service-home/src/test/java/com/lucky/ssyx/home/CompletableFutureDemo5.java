package com.lucky.ssyx.home;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lucky
 * @date 2023/9/29
 */
//多任务组合
public class CompletableFutureDemo5 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CompletableFuture<Integer> futureA = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "begin....");
            int value = 1024;
            System.out.println("任务一：" + value);
            System.out.println(Thread.currentThread().getName() + "end....");
            return value;
        }, executorService);

        CompletableFuture<Integer> futureB = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "begin....");
            int value = 1024;
            System.out.println("任务二：" + value);
            System.out.println(Thread.currentThread().getName() + "end....");
            return value;
        }, executorService);

        CompletableFuture<Void> all = CompletableFuture.allOf(futureA, futureB);
        all.get();
        System.out.println("over....");
    }
}

