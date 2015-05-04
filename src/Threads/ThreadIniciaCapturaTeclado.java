/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import GsServer.CapturaTeclado;
import GsServer.FrmPrincipal;
import GsServer.Mensagens;
import GsServer.Utils;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

/**
 *
 * @author jcferreira
 */
public class ThreadIniciaCapturaTeclado extends Thread {

    @Override
    public void run() {

        try {
            Mensagens.LEGENDAS_BAIXADAS ="";
            GlobalScreen.registerNativeHook();
            GlobalScreen.getInstance().addNativeKeyListener(
                    new CapturaTeclado());

        } catch (NativeHookException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            Utils.escreveLog("Erro Fatal: " + ex.getMessage(), Mensagens.ERROR);
            //Utils.escreviArquivoLog("Erro:"+ex.getMessage());
        }

    }

}
