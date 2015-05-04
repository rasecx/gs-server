/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GsServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author jcferreira
 */
public class SubDb {

    private static final String _UserAgent = "SubDB/1.0 (" + SystemInfo.NOME_SISTEMA + "/" + SystemInfo.VERSAO + ";" + SystemInfo.URL + ")";
    private static final String baseUrl = "http://api.thesubdb.com/?";
    private static final String actionDownload = "action=download&hash=";
    private static final String actionLanguage = "&language=";
    private static final String actionSearch = "action=search&hash=";

    public static String getHash(File f) {

        FileInputStream in;
        MessageDigest m = null;
        String ret = "";
        try {
            RandomAccessFile arquivo = new RandomAccessFile(f, "r");

            int size = 64 * 1024;
            byte[] ini = new byte[64 * 1024];
            byte[] fim = new byte[64 * 1024];
            arquivo.read(ini);
            arquivo.seek(arquivo.length() - size);
            arquivo.read(fim);

            m = MessageDigest.getInstance("MD5");

            m.update(ini);
            m.update(fim);
            ret = stringHexa(m.digest());
            //System.out.println(stringHexa(m.digest()));

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }

    private static String stringHexa(byte[] bytes) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
            int parteBaixa = bytes[i] & 0xf;
            if (parteAlta == 0) {
                s.append('0');
            }
            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        return s.toString();
    }

    public static List<Legenda> getListaLegendas(File f) {
        List<Legenda> resultList = new ArrayList<>();
        String hashVideo = getHash(f);
        String urlString ;//= baseUrl + actionDownload + hashVideo + actionLanguage + getCodigoLingua();
//                "http://api.thesubdb.com/?action=download&hash="+
//                             hashVideo+"&language=pt";
        try {

            urlString ="http://sandbox.thesubdb.com/?action=search&hash="+hashVideo;
           
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httpget = new HttpGet(urlString);
            httpget.addHeader("User-Agent", _UserAgent);
            HttpResponse response = httpclient.execute(httpget);

            if (response.getStatusLine().getStatusCode() == 200) {

                HttpEntity entity = response.getEntity();
                String contentDisp = response.getFirstHeader("Content-Disposition").getValue();
                contentDisp = contentDisp.replace(" attachment; filename=", "");

                InputStream entStream = entity.getContent();

                Legenda sub = new Legenda();
                sub.setID(hashVideo);
                sub.setFileName(contentDisp);
                //salva o texto da legenda
                //sub.setDescricao(InputToString(entStream, "ISO-8859-1"));

                sub.setDescricao(Utils.readStream(entStream));
                resultList.add(sub);
                System.out.println(sub.getFileName());
                EntityUtils.consume(entity);
            } else {
                Utils.escreveLog("Legenda Não Econtrada " + f.getName(), 1);
                //Utils.escreveInfoDownload(f.getName() + Mensagens.LEGENDA_NAO_ENCONTRADA, Mensagens.ERROR);
                // JOptionPane.showMessageDialog(null, "Legenda Não Econtrada!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    private static String[] getListaLingua() {
        String url = baseUrl + "action=languages";
        HttpClient httpclient = new DefaultHttpClient();
        String ret[] = null;

        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("User-Agent", _UserAgent);
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {

                HttpEntity entity = response.getEntity();
                InputStream entStream = entity.getContent();
                ret = Utils.readStream(entStream).split(",");

            }
        } catch (IOException ex) {
            Logger.getLogger(SubDb.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }

    public static List<Legenda> searchListaLegendas(String hashVideo) {
        List<Legenda> resultList = new ArrayList<>();
        String urlString = baseUrl + actionSearch + hashVideo + actionLanguage + getCodigoLingua();
//                "http://api.thesubdb.com/?action=download&hash="+
//                             hashVideo+"&language=pt";
        try {

            //"http://sandbox.thesubdb.com/?action=search&hash=edc1981d6459c6111fe36205b4aff6c2";
            //
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httpget = new HttpGet(urlString);
            httpget.addHeader("User-Agent", _UserAgent);
            HttpResponse response = httpclient.execute(httpget);

            if (response.getStatusLine().getStatusCode() == 200) {

                HttpEntity entity = response.getEntity();

//                String contentDisp = response.getFirstHeader("Content-Disposition").getValue();
//                contentDisp = contentDisp.replace(" attachment; filename=", "");
                InputStream entStream = entity.getContent();

                Legenda sub = new Legenda();
                sub.setID(hashVideo);
                //salva o texto da legenda

                sub.setDescricao(Utils.readStream(entStream));
                resultList.add(sub);
                EntityUtils.consume(entity);

            } else {
                JOptionPane.showMessageDialog(null, "Legenda Não Econtrada!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public static String InputToString(InputStream inStream, String enconding) {
        try {
            final char[] buffer = new char[0x10000];
            StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(inStream, enconding);
            int read;
            do {
                read = in.read(buffer, 0, buffer.length);
                if (read > 0) {
                    out.append(buffer, 0, read);
                }
            } while (read >= 0);

            in.close();
            return out.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static String getCodigoLingua() {
        String idiomas[] = getListaLingua();
        Utils.carregaConfiguracoes();
        if (FrmPrincipal.LINGUA.equals("eng")) {
            return idiomas[0];
        } else {
            return FrmPrincipal.LINGUA;
        }
    }

    public static Boolean iniciaDownload(File f) {
        boolean ret = false;
        List<Legenda> sub = SubDb.getListaLegendas(f);
        OutputStream os;

        try {
            os = new FileOutputStream(Utils.escreveUri(f.getParent(), Utils.nomeaLegenda(f.getName())));//localArquivo + "\\" + Utils.nomeaLegenda(NomeArquivo),);
            OutputStreamWriter osw = new OutputStreamWriter(os, "ISO-8859-1");
            try (BufferedWriter bw = new BufferedWriter(osw)) {

                bw.write(sub.get(0).getDescricao());//sb.toString
                Utils.escreveLog("Legenda Baixada: " + f.getName(), 0);
                Mensagens.LEGENDAS_BAIXADAS += f.getName() + "\n";

                bw.close();
                os.close();
                osw.close();

            } catch (IOException ex) {
                Logger.getLogger(SubDb.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(SubDb.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!sub.isEmpty()) {
            ret = true;
        }
        return ret;
    }
}
