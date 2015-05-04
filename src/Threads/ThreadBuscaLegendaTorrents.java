/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import GsServer.DownloadLegenda;
import GsServer.FrmPrincipal;
import GsServer.Mensagens;
import GsServer.Utils;
import Utorrent.RequestUtorrent;
import Utorrent.Torrent;
import Utorrent.TorrentFile;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julio
 */
public class ThreadBuscaLegendaTorrents extends Thread {

    private static void baixarLegendasPendentes() {
        RequestUtorrent utorrent = new RequestUtorrent();
        List<Torrent> list = utorrent.listTorrent();
        List<TorrentFile> listFile;
        String localDoArquivo = "";
        String nomeArquivo = "";
        boolean legendasEncontradas = false;
        if (!list.isEmpty()) {

            for (int i = 0; i < list.size(); i++) {

                if (list.get(i).getStatusString().equals("Concluido") || list.get(i).getStatusString().equals("Enviando")) {
                    localDoArquivo = list.get(i).getFileLocation();
                    listFile = utorrent.getFilesInTorrent(list.get(i));
                    for (int j = 0; j < listFile.size(); j++) {
                        nomeArquivo = listFile.get(j).getFileName();
                        if (nomeArquivo.contains("sample")) {
                            continue;
                        }

                        if (Utils.isExtVideoValida(Utils.retornaExtensao(nomeArquivo))) {
                            DownloadLegenda.BaixaLegenda(localDoArquivo, nomeArquivo);
                            legendasEncontradas = true;
                        }
                    }
                }
            }
            if (!legendasEncontradas) {
                Utils.escreveLog("Nenhuma legenda encontrada", Mensagens.NORMAL);
            }

        }

        if (!FrmPrincipal.btnBaixarPendentes.isEnabled()) {
            FrmPrincipal.btnBaixarPendentes.setEnabled(true);
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                Utils.limpaLog();
                int intervalo = Integer.valueOf(FrmPrincipal.edtIntervalo.getText());
                baixarLegendasPendentes();
                Thread.sleep(1000 * (60 * intervalo));//a cada 5 min verifica se o torrent foi concluido e baixa a legenda
            } catch (InterruptedException ex) {
                Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
