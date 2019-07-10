package com.example.wifidirectdemo;

/**
 * 线程封装
 */
public abstract class IThread extends Thread {
    @Override
    public void run() {
        super.run();
        try {
            IRun();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public abstract void IRun();
}
