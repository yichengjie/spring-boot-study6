package com.yicj.mvc.common;


public class CommonUtils {
    public static void sleep(int time, Runnable runnable){
        new Thread(()->{
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runnable.run();
        }).start();
    }
}
