/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GsServer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jcferreira
 */
public class CapturaAreaTransferencia implements ClipboardOwner {

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }

    public String getClipboardContents() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        File file;
        boolean hasTransferableText
                = (contents != null)
                && contents.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
        if (hasTransferableText) {
            try {

                List pathFile = (List) contents.getTransferData(DataFlavor.javaFileListFlavor);
                Utils.showBallonHint(true);
                Utils.escreveInfoDownload(Mensagens.INICIANDO_DOWNLOAD, Mensagens.INFO);
                for (int i = 0; i < pathFile.size(); i++) {
                    file = new File(pathFile.get(i).toString());
                    if (file.isFile()) {
                        if (Utils.isExtVideoValida(Utils.retornaExtensao(file.getName()))) {
                            DownloadLegenda.BaixaLegenda(file.getParent(), file.getName());
                        }
                    } else if (file.isDirectory()) {
                        for (File fileN1 : file.listFiles()) {//procura no 1º nivel
                            if (fileN1.isDirectory()) {
                                for (File fileN2 : fileN1.listFiles()) {//procura no 2º nivel
                                    if (Utils.isExtVideoValida(Utils.retornaExtensao(fileN2.getName()))) {
                                        DownloadLegenda.BaixaLegenda(fileN2.getParent(), fileN2.getName());
                                    }

                                }
                            } else {
                                if (Utils.isExtVideoValida(Utils.retornaExtensao(fileN1.getName()))) {
                                    DownloadLegenda.BaixaLegenda(fileN1.getParent(), fileN1.getName());
                                }
                            }
                        }
                    }
                }
                Utils.escreveInfoDownload(Mensagens.DOWNLOAD_CONCLUIDO, Mensagens.INFO);
                Thread.sleep(3000);
                Utils.showBallonHint(false);

                result = "";

            } catch (UnsupportedFlavorException | IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                Logger.getLogger(CapturaAreaTransferencia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public void capturaArquivo() {

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        File file;
        boolean hasTransferableFile
                = (contents != null)
                && contents.isDataFlavorSupported(DataFlavor.javaFileListFlavor);

        try {
            if (hasTransferableFile) {
                List pathFile = (List) contents.getTransferData(DataFlavor.javaFileListFlavor);
                 SysTray.setDisplayMessage(Mensagens.PROCURANDO_LEGENDA);
                for (int i = 0; i < pathFile.size(); i++) {
                    file = new File(pathFile.get(i).toString());
                    if (file.isFile()) {
                        if (Utils.isExtVideoValida(Utils.retornaExtensao(file.getName()))) {                           
                            DownloadLegenda.BaixaLegenda(file.getParent(), file.getName());
                        }
                    }
                }
                SysTray.setDisplayMessage(Mensagens.LEGENDAS_BAIXADAS+Mensagens.DOWNLOAD_CONCLUIDO);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    public void capturaPasta() {

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        File file;
        boolean hasTransferableFile
                = (contents != null)
                && contents.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
        try {
            if (hasTransferableFile) {
                List pathFile = (List) contents.getTransferData(DataFlavor.javaFileListFlavor);
                SysTray.setDisplayMessage(Mensagens.PROCURANDO_LEGENDA);
                for (int i = 0; i < pathFile.size(); i++) {
                    file = new File(pathFile.get(i).toString());
                    if (file.isDirectory()) {                        
                        
                        for (File fileN1 : file.listFiles()) {//procura no 1º nivel
                            if (fileN1.isDirectory()) {
                               
                                
                                for (File fileN2 : fileN1.listFiles()) {//procura no 2º nivel
                                    if (Utils.isExtVideoValida(Utils.retornaExtensao(fileN2.getName()))) {
                                        
//                                        if(!Utils.isShowingBallon)
//                                            Utils.showBallonHint(true);
//                                        Utils.escreveInfoDownload(Mensagens.PROCURANDO_LEGENDA, Mensagens.INFO);                                        
                                        DownloadLegenda.BaixaLegenda(fileN2.getParent(), fileN2.getName());
                                       
                                    }

                                }
                            } else {
                                if (Utils.isExtVideoValida(Utils.retornaExtensao(fileN1.getName()))) {
//                                    if(!Utils.isShowingBallon)
//                                            Utils.showBallonHint(true);
//                                        Utils.escreveInfoDownload(Mensagens.PROCURANDO_LEGENDA, Mensagens.INFO);                                                                            
                                   
                                    DownloadLegenda.BaixaLegenda(fileN1.getParent(), fileN1.getName());
                                    
                                }
                            }
                        }
                        SysTray.setDisplayMessage(Mensagens.LEGENDAS_BAIXADAS+Mensagens.DOWNLOAD_CONCLUIDO);                
                        //Utils.escreveInfoDownload(Mensagens.DOWNLOAD_CONCLUIDO, Mensagens.INFO);
                        //Thread.sleep(3000);
                        //Utils.showBallonHint(false);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
