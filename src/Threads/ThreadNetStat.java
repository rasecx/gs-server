/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jcferreira
 */
public class ThreadNetStat extends Thread {

    @Override
    public void run() {

        String line;
        String output = "";
        // execute the netstat command
        Process p;
        try {
            p = Runtime.getRuntime().exec("netstat -n -b");
            // capture the output of netstat
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                output = output + " \n" + line;
            }
            input.close();
        } catch (IOException ex) {
            Logger.getLogger(ThreadNetStat.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
