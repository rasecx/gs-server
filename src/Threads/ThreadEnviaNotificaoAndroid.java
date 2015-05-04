/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import GsServer.FrmPrincipal;
import GsServer.GsSocketClient;

import static GsServer.GsSocketClient.downloadsConcluidos;
import GsServer.Mensagens;
import GsServer.Utils;
import static GsServer.Utils.getProp;
import Utorrent.RequestUtorrent;
import Utorrent.Torrent;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jcferreira
 */
public class ThreadEnviaNotificaoAndroid extends Thread {

    @Override
    public void run() {
        Properties p = Utils.getProp();
        String notificacaoEnviada = p.getProperty("NotificaoEnviada");
        if (notificacaoEnviada.equals(Mensagens.NOTIFICAO_NAO_ENVIADA)) {
            while (true) {
                try {

                    int intervalo = Integer.valueOf(FrmPrincipal.txtIntervaloNotificacao.getText());

                    downloadsConcluidos = "Download(s) Concluído(s)|";
                    RequestUtorrent utorrent = new RequestUtorrent();
                    List<Torrent> list = utorrent.listTorrent();

                    for (Torrent t : list) {
                        if (t.getStatusString().equals("Concluido") || t.getStatusString().equals("Enviando")) {
                            downloadsConcluidos += t.getName() + "|";
                        }
                    }

                    new GsSocketClient().start();
                    // SysTray.setToolTip("Notificação Enviada");
                    Thread.sleep(1000 * (intervalo * 3600));//a cada x min verifica se o torrent foi concluido e baixa a legenda

                } catch (InterruptedException ex) {
                    Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

}
