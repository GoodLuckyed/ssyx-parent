package com.lucky.ssyx.home;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lucky
 * @date 2023/9/29
 */
//计算完成时回调方法->whenComplete
public class CompletableFutureDemo3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        System.out.println("main begin.....");
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());
            int value = 1024;
            System.out.println("value:" + value);
            return value;
        }, executorService).whenComplete((res,exc)->{
            System.out.println(res);
            System.out.println(exc);
        });
        Integer result = completableFuture.get();
        System.out.println("main over..... " + result);
    }
}

