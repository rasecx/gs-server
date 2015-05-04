/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GsServer;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author jcferreira
 */
public class CapturaTeclado implements NativeKeyListener {

    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
        String teclaFuncao = NativeKeyEvent.getModifiersText(nke.getModifiers());

        if (FrmPrincipal.chkHabilitaAtalho.isSelected()) {
            if (teclaFuncao.equals("Ctrl") && ((nke.getKeyCode() == NativeKeyEvent.VK_C))) {
                try {
                    Thread.sleep(1000);
                    if (FrmPrincipal.rdHabilitaArquivo.isSelected()) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                new CapturaAreaTransferencia().capturaArquivo();
                            }
                        }).start();
                    } else {

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                CapturaAreaTransferencia cap = new CapturaAreaTransferencia();
                                cap.capturaPasta();
                                cap.capturaArquivo();
                            }
                        }).start();

                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(CapturaTeclado.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
//        else if (nke.getKeyCode() == NativeKeyEvent.VK_D && teclaFuncao.equals("Shift+Ctrl")) {
//            try {
//                Thread.sleep(1000);                
//                new CapturaAreaTransferencia().capturaPasta();
//
//            } catch (InterruptedException ex) {
//                Logger.getLogger(CapturaTeclado.class.getName()).log(Level.SEVERE, null, ex);
//                
//            }
//            
//        }
        }

    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {

    }

}
