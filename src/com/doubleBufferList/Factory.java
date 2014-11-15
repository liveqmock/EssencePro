package com.doubleBufferList;

public class Factory extends Thread {
    public void run() {
        while (true) {
            Toy t = new Toy();
            t.setName("玩具");
            // synchronized (Tools.lT) {

            // }
            try {
                Tools.lT.put(t);
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
