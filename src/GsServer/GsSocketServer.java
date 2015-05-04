/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GsServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jcferreira
 */
public class GsSocketServer {

    private static ServerSocket server;
    private static Socket client;
    static String nomeArquivo="";
    static String localArquivo="";
    

    public static void startServer() {
        try {
            Properties p = Utils.getProp();
            Integer porta =Integer.valueOf(p.getProperty("PortaGetSeries","1088"));            
            server = new ServerSocket(porta);
            
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    Scanner s;
                    
                    try {
                        String receiverText="";
                        FrmPrincipal.txtIPServidorGetSeries.setText("");
                        
                        while(true){
                            
                            client = server.accept();
                            s = new Scanner(client.getInputStream()); 
                            receiverText="";
                            FrmPrincipal.txtIPServidorGetSeries.setText("");

                            while (s.hasNextLine()) {
                                receiverText+=s.next().trim();
                            }                            
                            FrmPrincipal.txtIPServidorGetSeries.setText(receiverText);
                            Utils.escreveLog(receiverText+" Conectado!",2);
                            client.close();
                           
                            //server.close();
                        }
                      } catch (IOException ex) {
                        Logger.getLogger(GsSocketServer.class.getName()).log(Level.SEVERE, null, ex);
                        Utils.escreveLog("Erro Fatal: "+GsSocketServer.class.getName(), Mensagens.ERROR);
                    }

                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });

            t.start();
          

        } catch (IOException ex) {
            Logger.getLogger(GsSocketServer.class.getName()).log(Level.SEVERE, null, ex);
            Utils.escreveLog("Erro Fatal: "+GsSocketServer.class.getName(), Mensagens.ERROR);
        }
      
    }
    
    
}
