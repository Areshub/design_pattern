package com.lnf.springboot;

public class GdsfeChild extends Gdsfes{
    public GdsfeChild(){
        System.out.println("子类构造");
    }
    static{
        System.out.println("子类静态块儿");
    }
    {
        System.out.println("子类块儿");
    }
    @Override
    public void say(){
        System.out.println("hello1");
    }
    public static void main(String[] args) {
        GdsfeChild g = new GdsfeChild();

    }
}
