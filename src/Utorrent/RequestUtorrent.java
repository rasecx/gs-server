package Utorrent;

import GsServer.LogError;
import GsServer.Mensagens;
import GsServer.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class RequestUtorrent {
    
    private String _enderecoServidor;
    private String _credenciaisUsuario;
    
    private String _token;    
    public static List<Torrent> _torrents = new ArrayList<Torrent>();
    public static List<TorrentFile> arquivosTorrent = new ArrayList<TorrentFile>();
    
    public static Boolean fimExecucao = false;
    /*
     * Descricao das acoes 
     * 0 = Listar todos os torrents 
     * 1 = iniciar torrent 
     * 2 = iniciar torrent forcado 
     * 3 = parar torrent 
     * 4 = pausar torrent 
     * 5 = remover somente torrent 
     * 6 = remover torrent e arquivos 
     * 7 = pegar arquivos contidos no torrent 
     * 8 = pegar proriedades do torrent 
     * 9 = mudar prioridade de um arquivo 
     * 10 = adicionar um torrent via url
     */
    private String[] _fazerAcao = {"list=1", "action=start&hash=",
        "action=forcestart&hash=", "action=stop&hash=",
        "action=pause&hash=", "action=remove&hash=",
        "action=removedata&hash=", "action=getfiles&hash=",
        "action=getprops&hash=",
        "action=setprio&hash=_hash&p=_prioridade2&f=_indexArquivo",// substituir
        // as
        // variaveis
        // por
        // stringreplace
        "action=add-url&s="};
    
    public RequestUtorrent() {
        
            Properties p = Utils.getProp();
            String host = p.getProperty("ServerUtorrent");
            String porta = p.getProperty("PortaUtorrent");
            setConnServidor(false, host, Integer.valueOf(porta));
            setCredenciaisUsuario(p.getProperty("UsuarioUtorrent"), p.getProperty("SenhaUtorrent"));
        
        
    }
    
    public List<Torrent> listTorrent() {
        try {
            
            getToken();
            getServerResponse(_fazerAcao[0]);
            
        } catch (Exception e) {
            e.printStackTrace();
            Utils.escreveLog("Erro Fatal: "+e.getMessage(), Mensagens.ERROR);
            return null;
        }
        
        return _torrents;
    }
    
    public void setConnServidor(boolean useSSL, String enderecoServer, int porta) {
        _enderecoServidor = (useSSL ? "https" : "http") + "://"
                + enderecoServer + ":" + Integer.toString(porta) + "/gui/";
    }
    
    public void setCredenciaisUsuario(String userName, String password) {
        String decode = userName + ":" + password;
        String encodedLogin = Base64.encodeBase64String(decode.getBytes());
        //Base64.encodeToString(decode.getBytes(),Base64.NO_WRAP);
        _credenciaisUsuario = "Basic " + encodedLogin;
    }
    
    private String getServerResponse(String acao) {
        
        String retorno = "";
        StringBuffer stringJson = new StringBuffer();
        String url = _enderecoServidor + "token.html";
        
        try {
            // connecta pra pegar o token
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 3500);
            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpGet httpget = new HttpGet(url);
            httpget.setHeader("Authorization", _credenciaisUsuario);
            httpget.setHeader("X-J2me-Auth", _credenciaisUsuario);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            getToken(instream);

            // conecta pra pegar objeto Json da mesma secao do token
            url = _enderecoServidor + "?token=" + _token + "&" + acao;
            httpget = new HttpGet(url);
            httpget.setHeader("Authorization", _credenciaisUsuario);
            httpget.setHeader("X-J2me-Auth", _credenciaisUsuario);
            response = httpclient.execute(httpget);
            entity = response.getEntity();
            
            if (entity != null) {
                
                instream = entity.getContent();
                BufferedReader br = null;
                br = new BufferedReader(new InputStreamReader(instream));
                // le conexao
                String res = "";
                while (null != ((res = br.readLine()))) {
                    stringJson.append(res);
                }
            }
            
            if (acao.contains("list")) {
                _torrents = getTorrents(stringJson.toString());
            }
            retorno = stringJson.toString();
            
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            Utils.escreveLog("Erro Fatal: "+e.getMessage(), Mensagens.ERROR);
            fimExecucao = true;
            retorno = "erro";
        }
        fimExecucao = true;
        return retorno;
    }
    
    private List<Torrent> getTorrents(String json) {
        List<Torrent> listTorrents = new ArrayList<Torrent>();
        try {
            
            JSONObject jsonObj = new JSONObject(json);
            JSONArray jsonArray = jsonObj.getJSONArray("torrents");
            
            Torrent t;
            
            for (int i = 0; i < jsonArray.length(); i++) {
                t = new Torrent(jsonArray.getJSONArray(i));
                listTorrents.add(t);
//				if(mBolListaArquivos)
//					arquivosTorrent.addAll(getFilesInTorrent(t));

            }
        } catch (JSONException e) {
            fimExecucao = true;
            e.printStackTrace();
        }
        
        return listTorrents;
    }
    
    public List<TorrentFile> getFilesInTorrent(Torrent torrent) {
        
        String response = getServerResponse(_fazerAcao[7] + torrent.getHash());
        List<TorrentFile> result = new ArrayList<TorrentFile>();
        TorrentFile torrentFile;
        
        try {
            JSONTokener jt = new JSONTokener(response);
            JSONObject jo = new JSONObject(jt);
            
            JSONArray files = jo.getJSONArray("files").getJSONArray(1);
            for (int i = 0; i < files.length(); ++i) {
                torrentFile = new TorrentFile(files.getJSONArray(i));
                torrentFile.setTorrentHash(torrent.getHash());
                result.add(torrentFile);
                
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        return result;
    }
    
    private String getToken() {
        
        String retorno = "";
        String url = _enderecoServidor + "token.html";
        StringBuffer s = new StringBuffer();
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3500);
        
        HttpClient httpclient = new DefaultHttpClient(httpParams);
        
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Authorization", _credenciaisUsuario);
        //httpget.setHeader("X-J2me-Auth", _credenciaisUsuario);

        try {
            
            HttpResponse response = httpclient.execute(httpget);
            
            HttpEntity entity = response.getEntity();
            
            if (entity != null) {
                InputStream instream = entity.getContent();
                byte[] buff = new byte[512];
                int bytesRead = instream.read(buff, 0, 512);
                while (bytesRead > 0) {
                    s.append(UTF8EncoderDecoder.UTF8Decode(buff, 0, bytesRead));
                    bytesRead = instream.read(buff, 0, 512);
                }
                instream.close();
                
                String tokenResponse = s.toString();
                
                String preToken = "<html><div id='token' style='display:none;'>";
                String postToken = "</div></html>";
                
                _token = tokenResponse.substring(preToken.length(),
                        tokenResponse.length() - postToken.length());
                retorno = _token;
                
            }
        } catch (Exception e) {
            e.printStackTrace();
            
            
            if(e.getMessage().contains("Connection refused")) {           
                LogError.connectionRefused = true;
                Utils.escreveLog("Erro ao Conectar: Não foi possivel conectar ao servidor torrent\n"
                        + "atraves do endereço: "+_enderecoServidor
                        + "\nFavor verificar se o IP e porta estao iguais a do uTorrent", Mensagens.ERROR);
            }
            
            if(e.getMessage().contains("String index out of range")){
                LogError.connectionRefused = true;
                Utils.escreveLog("Erro de Login: Não foi possivel recupera o token do servidor\n"
                        + "Favor vericar o usuario e senha", Mensagens.ERROR);
            
            }
            return "erro";
        }
        return retorno;
    }
    
    private String getToken(InputStream instream) {
        String retorno = "";
        StringBuffer s = new StringBuffer();
        
        try {
            
            byte[] buff = new byte[512];
            int bytesRead = instream.read(buff, 0, 512);
            while (bytesRead > 0) {
                s.append(UTF8EncoderDecoder.UTF8Decode(buff, 0, bytesRead));
                bytesRead = instream.read(buff, 0, 512);
            }
            instream.close();
            
            String tokenResponse = s.toString();
            
            String preToken = "<html><div id='token' style='display:none;'>";
            String postToken = "</div></html>";
            
            _token = tokenResponse.substring(preToken.length(),
                    tokenResponse.length() - postToken.length());
            retorno = _token;
            
        } catch (Exception e) {
            e.printStackTrace();
            return "erro";
        }
        return retorno;
    }
    
}
