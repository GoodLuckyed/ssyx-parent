package com.lucky.ssyx.home;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lucky
 * @date 2023/9/29
 */
//线程串行化
public class CompletableFutureDemo4 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CompletableFuture<Integer> futureA = CompletableFuture.supplyAsync(() -> {
            int value = 1024;
            System.out.println("任务一：" + value);
            return value;
        }, executorService);

        CompletableFuture<Integer> futureB = futureA.thenApply((res) -> {
            System.out.println("任务二：" + res);
            return res;
        });

        CompletableFuture<Void> futureC = futureA.thenRun(() -> {
            System.out.println("任务三：xxx");
        });
    }
}

