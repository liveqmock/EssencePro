package com.doubleBufferList;

import javax.swing.JOptionPane;

public class Kid extends Thread {
    long time1 = System.currentTimeMillis();
    int count = 0;

    public void run() {
        while (true) {
            // synchronized (Tools.lT) {
            // if (Tools.lT.size() != 0)
            try {
                Tools.lT.take();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // }
            count++;
            if (count == 1000) {
                // swing自带的可视化窗口
                JOptionPane.showMessageDialog(null, "用时间: " + (System.currentTimeMillis() - time1));
                System.exit(0);
            }
        }
    }

}
