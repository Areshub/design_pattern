package com.lnf.springboot;

public class Gdsfes {
    public Gdsfes(){
        System.out.println("父类构造");
    }
    static{
        System.out.println("父类静态块儿");
    }
    {
        System.out.println("父类块儿");
    }

    public void say(){
        System.out.println("hello");
    }
}
