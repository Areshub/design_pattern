package com.lnf.threadPool;

import java.util.Random;
import java.util.concurrent.*;

public class T01_00_Future {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
/*
        FutureTask<Integer> integerFutureTask = new FutureTask<>(() -> {
            TimeUnit.SECONDS.sleep(60);
            return 1000;
        });
        new Thread(integerFutureTask).start();
        System.out.println(integerFutureTask.get());
*/

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,
                4,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(4),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());
        for (int i = 0; i <9; i++) {
            threadPoolExecutor.execute(new Thread(()->{
                System.out.println(new Random().nextInt());
            }));
        }
    }
}
