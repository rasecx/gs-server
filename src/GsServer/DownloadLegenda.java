/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GsServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Julio
 */
public class DownloadLegenda {

    public static void BaixaLegenda(String localArquivo, String NomeArquivo) {
        if (!Utils.isVideoTemLegenda(localArquivo, NomeArquivo)) {
            File f = new File(Utils.escreveUri(localArquivo, NomeArquivo));
            if(OpenSubititles.iniciaDownload(f))
                System.out.println("Legendas Baixadas por OpenSubtitles");
            else 
                SubDb.iniciaDownload(f);

        }
    }

    public static void BaixaLegenda(File f) {
        List<Legenda> sub = SubDb.getListaLegendas(f);
        OutputStream os;
        try {
            os = new FileOutputStream(Utils.nomeaLegenda(f.getAbsolutePath()));
            OutputStreamWriter osw = new OutputStreamWriter(os, "ISO-8859-1");
            try (BufferedWriter bw = new BufferedWriter(osw)) {

                bw.write(sub.get(0).getDescricao());//sb.toString());
                JOptionPane.showMessageDialog(null, "Legenda salva com sucesso!\n" + f.getParent());

                bw.close();
            }
            os.close();
            osw.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DownloadLegenda.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DownloadLegenda.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
