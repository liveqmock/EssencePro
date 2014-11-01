package com.test;

import java.util.TimerTask;

public class RemindTask extends TimerTask {
    public void run() {
        System.out.println("Time's up!");
    }
}
