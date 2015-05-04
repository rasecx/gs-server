/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jcferreira
 */
public class ThreadGarbageCollector extends Thread {
    //a cada 2 hrs solicita o garbageCollector
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000 * (60 * 120));
                System.out.println("GarbageCollector");
                System.gc();

            }

        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadGarbageCollector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
