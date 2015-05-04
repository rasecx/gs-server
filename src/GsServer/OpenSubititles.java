/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GsServer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.rmi.CORBA.Util;
import org.apache.ws.commons.util.Base64;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 *
 * @author jcferreira
 */
public class OpenSubititles {

    /**
     * Size of the chunks that will be hashed in bytes (64 KB)
     */
    private static final int HASH_CHUNK_SIZE = 64 * 1024;
    private static final String baseUrl = "http://api.opensubtitles.org/xml-rpc";
    public static final String _UserAgent = "getseries v1.0";//"VuzeLegendasBaixator" + " v" + "0.1";
    private static XmlRpcClient xmlRpcClient;
    private static XmlRpcClientConfigImpl xmlRpcConfig;
    private static String token;

    public void OpenSubititles() {

    }

    public static Boolean iniciaDownload(File f) {
        boolean ret = false;
        try {
            token = getToken("rasecx", "virus1", "pt");  //(String) resp.get("token");
            List<Legenda>listLegenda =procuraLegenda(f);
            
            Collections.sort(listLegenda, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
             
                    Legenda l1 = (Legenda) o1;  
                    Legenda l2 = (Legenda) o2;  
                return l1.getDownloads() < l2.getDownloads() ? +1 : (l1.getDownloads() > l2.getDownloads() ? -1 : 0);  
                }
            });
            
            Legenda l = listLegenda.get(0);
            getLegendaArquivo(l, f);
            
            if (listLegenda.size()>0)
                ret = true;
        } catch (Exception ex) {
            Logger.getLogger(OpenSubititles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }

    public static byte[] decodeBase64(String contentBase64) {
        //return Base64.decodeBase64(contentBase64.getBytes());
        return null;
    }

    public static InputStream gZip(byte[] dados) {
        try {
            ByteArrayInputStream byteInput = new ByteArrayInputStream(dados);
            InputStream in = new GZIPInputStream(byteInput);
            return in;
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    private static Object executeRpcMethod(String method, List params) {
        try {
            return xmlRpcClient.execute(method, params);
        } catch (XmlRpcException e) {
            throw new RuntimeException("Erro on calling method " + method, e);
        }
    }

    public static String computeHash(File file) throws IOException {
        long size = file.length();
        long chunkSizeForFile = Math.min(HASH_CHUNK_SIZE, size);

        try (FileChannel fileChannel = new FileInputStream(file).getChannel()) {
            long head = computeHashForChunk(fileChannel.map(MapMode.READ_ONLY, 0, chunkSizeForFile));
            long tail = computeHashForChunk(fileChannel.map(MapMode.READ_ONLY, Math.max(size - HASH_CHUNK_SIZE, 0), chunkSizeForFile));

            return String.format("%016x", size + head + tail);
        }
    }

    public static String computeHash(InputStream stream, long length) throws IOException {

        int chunkSizeForFile = (int) Math.min(HASH_CHUNK_SIZE, length);

        // buffer that will contain the head and the tail chunk, chunks will overlap if length is smaller than two chunks
        byte[] chunkBytes = new byte[(int) Math.min(2 * HASH_CHUNK_SIZE, length)];

        DataInputStream in = new DataInputStream(stream);

        // first chunk
        in.readFully(chunkBytes, 0, chunkSizeForFile);

        long position = chunkSizeForFile;
        long tailChunkPosition = length - chunkSizeForFile;

        // seek to position of the tail chunk, or not at all if length is smaller than two chunks
        while (position < tailChunkPosition && (position += in.skip(tailChunkPosition - position)) >= 0);

        // second chunk, or the rest of the data if length is smaller than two chunks
        in.readFully(chunkBytes, chunkSizeForFile, chunkBytes.length - chunkSizeForFile);

        long head = computeHashForChunk(ByteBuffer.wrap(chunkBytes, 0, chunkSizeForFile));
        long tail = computeHashForChunk(ByteBuffer.wrap(chunkBytes, chunkBytes.length - chunkSizeForFile, chunkSizeForFile));

        return String.format("%016x", length + head + tail);
    }

    private static long computeHashForChunk(ByteBuffer buffer) {

        LongBuffer longBuffer = buffer.order(ByteOrder.LITTLE_ENDIAN).asLongBuffer();
        long hash = 0;

        while (longBuffer.hasRemaining()) {
            hash += longBuffer.get();
        }

        return hash;
    }

    private static String getToken(String usuario, String senha, String linguagem) {
        String token = "";
        try {
            xmlRpcClient = new XmlRpcClient();
            xmlRpcConfig = new XmlRpcClientConfigImpl();
            xmlRpcConfig.setServerURL(new URL(baseUrl));
            xmlRpcConfig.setEnabledForExtensions(true);
            xmlRpcConfig.setBasicPassword(baseUrl);
            xmlRpcConfig.setUserAgent(_UserAgent);
            //xmlRpcConfig.setGzipCompressing(true);
            //xmlRpcConfig.setGzipRequesting(true);

            xmlRpcClient.setConfig(xmlRpcConfig);
            List<String> params = new ArrayList<>();
            params.add(usuario);
            params.add(senha);
            params.add(linguagem);
            params.add(_UserAgent);

            Map resp = (Map) executeRpcMethod("LogIn", params);
            token = (String) resp.get("token");
        } catch (Exception e) {
        }

        return token;

    }

    private static List<Legenda> procuraLegenda(File f) {
        List params = new ArrayList();
        List<Legenda> list = new ArrayList<>();
        try {

            String hash = computeHash(f);
            HashMap<String, Object> movieMap = new HashMap<String, Object>();
            Legenda legenda;

            //movieMap.put("sublanguageid", "pob");
            movieMap.put("sublanguageid", getCodigoLingua());
            movieMap.put("moviehash", hash);
            movieMap.put("moviebytesize", f.length());

            params.add(token);
            params.add(new Object[]{movieMap});
            Map resp = (Map) executeRpcMethod("SearchSubtitles", params);
            Object[] subtitlesResp = (Object[]) resp.get("data");

            for (Object item : subtitlesResp) {
                Map subtitleResp = (Map) item;
                legenda = new Legenda();
                legenda.setID((String) subtitleResp.get("IDSubtitleFile"));
                legenda.setDescricao((String) subtitleResp.get("MovieName"));
                legenda.setDownloads(Integer.parseInt((String) subtitleResp.get("SubDownloadsCnt")));
                legenda.setFileName((String) subtitleResp.get("SubFileName"));
                legenda.setFps(Double.valueOf((String) subtitleResp.get("MovieFPS")));
                legenda.setRelease((String)subtitleResp.get("MovieReleaseName"));
//            subTitleVO.setCds(Integer.parseInt((String)subtitleResp.get("SubSumCD")));            
//            subTitleVO.setMovieSize(Long.parseLong((String)subtitleResp.get("MovieByteSize")));
//            
                list.add(legenda);
                
            }
            if(list.isEmpty())
                Utils.escreveLog(f.getName()+Mensagens.LEGENDA_NAO_ENCONTRADA, Mensagens.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.escreveLog(f.getName()+Mensagens.DOWNLOAD_FALHA, Mensagens.ERROR);
        }
        return list;
    }

    public static void getLegendaArquivo(Legenda legenda, File f) {
        try {

            Map resp = null;//(Map) executeRpcMethod("LogIn", params);

            List paramsD = new ArrayList();

            paramsD.add(token);
            paramsD.add(new Object[]{legenda.getID()});
            FileOutputStream stream =null;

            resp = (Map) executeRpcMethod("DownloadSubtitles", paramsD);
            Object[] subtitlesResp = (Object[]) resp.get("data");
            if (subtitlesResp.length > 0) {
                Map subRe = (Map) subtitlesResp[0];
                String data = (String) subRe.get("data");
                byte[] decode = Base64.decode(data); //decodeBase64(data.getBytes()); //decodeBase64(data);

                InputStream inStream = gZip(decode);

                File file = new File(Utils.nomeaLegenda(f.getAbsolutePath()));
                

                try {
                    stream = new FileOutputStream(file);

                    int bytesRead = 0;
                    while ((bytesRead = inStream.read()) != -1) {
                        stream.write(bytesRead);
                    }                    
//                    Utils.escreveInfoDownload(legenda.getFileName()+Mensagens.DOWNLOAD_SUCESSO, Mensagens.INFO);
                    Mensagens.LEGENDAS_BAIXADAS+=f.getName()+"\n";

                } catch (IOException ex) {
                    String message = "Error writing subtitle file: " + ex.getMessage();

                    throw new RuntimeException(message, ex);
                }finally{
                    inStream.close();
                    stream.close();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    private static String getCodigoLingua(){
        Utils.carregaConfiguracoes();
        if(FrmPrincipal.LINGUA.equals("pt"))
            return "pob";
        else
            return FrmPrincipal.LINGUA;
    }
}
