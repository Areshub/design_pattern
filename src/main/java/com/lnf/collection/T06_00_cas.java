package com.lnf.collection;

public class T06_00_cas {

    enum ReadtToRun {T1,T2}
    
    static volatile ReadtToRun r = ReadtToRun.T2;

    public static void main(String[] args) {
        char[] char1= "1234567".toCharArray();
        char[] char2 ="ABCDEFG".toCharArray();

        new Thread(()->{
            for (char c :char1){
                while (r!=ReadtToRun.T1){}
                System.out.println(c);
                r=ReadtToRun.T2;
            }
        },"T1").start();

        new Thread(()->{
            for (char c :char2){
                while (r!=ReadtToRun.T2){}
                System.out.println(c);
                r=ReadtToRun.T1;
            }
        },"T2").start();
    }
}
