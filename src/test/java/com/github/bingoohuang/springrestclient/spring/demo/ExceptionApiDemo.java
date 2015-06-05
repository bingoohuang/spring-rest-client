package com.github.bingoohuang.springrestclient.spring.demo;

public class ExceptionApiDemo {

    public void sleep(long l){
        try{
            Thread.sleep(l);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
