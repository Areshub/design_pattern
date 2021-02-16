package com.lnf.test;

public class QuickSort {

    public static void quickSort(int[] array ,int left,int right){
        int l,r,flag,temp;
        if(left >= right){
            return;
        }
        l = left;
        r = right;
        flag = array[left];
        while (l < r){
            //先从右边找
            while (array[r] >= flag && l < r){
                r --;
            }
            //再从左边找
            while (array[l] <= flag && l < r){
                l ++;
            }
            //交换
            if(l < r){
                temp = array[l];
                array[l] = array[r];
                array[r] = temp;
            }
        }
        //一趟交换完将基准值放入临界位置
        array[left] = array[l];
        array[l] = flag;
        //向左递归
        quickSort(array,left,l-1);
        quickSort(array,l+1,right);
    }
}
