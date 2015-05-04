/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Threads;

import Utorrent.RequestUtorrent;
import Utorrent.Torrent;
import Utorrent.TorrentFile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jcferreira
 */
public class ThreadExecutaConexaoUtorrent extends Thread{
    public static List<Torrent> listaTorrent; 
    private List<TorrentFile>arquivos;
    public static List<List<TorrentFile>> listaDeArquivos = new ArrayList<>();
    
    @Override
    public void run(){
        RequestUtorrent utorrent = new RequestUtorrent();
        listaTorrent =utorrent.listTorrent();
        
        for(Torrent t:listaTorrent){
            arquivos=utorrent.getFilesInTorrent(t);
            listaDeArquivos.add(arquivos);
            
        }
        
        
        
    }
}
