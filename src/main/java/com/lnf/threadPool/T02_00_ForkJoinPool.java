package com.lnf.threadPool;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class T02_00_ForkJoinPool {
    static int[] nums = new int[1000000];
    static final int MAX_NUM = 50000;
    static Random r = new Random();

    static {
        for (int i = 0; i < nums.length; i++) {
            nums[i]= r.nextInt(100);
        }
        System.out.println("-------------"+Arrays.stream(nums).sum());
    }

    static class AddTask extends RecursiveAction{

        int start,end;

        AddTask(int s,int e){
            this.start=s;
            this.end =e;
        }

        @Override
        protected void compute() {
            if(end-start<=MAX_NUM){
                long sum =0l;
                for (int i = start; i < end; i++) sum+=nums[i];
                System.out.println("from"+start+"to"+end+"="+sum);
            }else {
                int middle = start +(end-start)/2;
                AddTask addTask1 = new AddTask(start, middle);
                AddTask addTask2 = new AddTask(middle, end);
                addTask1.fork();
                addTask2.fork();
            }
        }
    }


    static class AddTaskRet extends RecursiveTask<Long> {

        int start,end;

        AddTaskRet(int s,int e){
            this.start=s;
            this.end =e;
        }

        @Override
        protected Long compute() {
            if(end-start<=MAX_NUM){
                long sum =0l;
                for (int i = start; i < end; i++) sum+=nums[i];
                return sum;
            }

                int middle = start +(end-start)/2;
                AddTaskRet addTask1 = new AddTaskRet(start, middle);
                AddTaskRet addTask2 = new AddTaskRet(middle, end);
                addTask1.fork();
                addTask2.fork();


            return addTask1.join()+addTask2.join();
        }
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
     /*   AddTask addTask = new AddTask(0, nums.length);
        forkJoinPool.execute(addTask);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        AddTaskRet addTask = new AddTaskRet(0, nums.length);
        forkJoinPool.execute(addTask);
        Long join = addTask.join();
        System.out.println(join);
    }
}
