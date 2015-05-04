/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GsServer;

/**
 *
 * @author jcferreira
 */
public class Legenda {
    
    private String _id;
    public String getID() {
        return _id;
    }
    public void setID(String value) {
        _id = value;
    }

    private String _descricao;
    public String getDescricao() {
        return _descricao;
    }
    public void setDescricao(String value) {
        _descricao = value;
    }

    private String _release;
    public String getRelease() {
        return _release;
    }
    public void setRelease(String value) {
        _release = value;
    }

    private int _cds;
    public int getCds() {
        return _cds;
    }
    public void setCds(int value) {
        _cds = value;
    }

    private long _movieSize;
    public long getMovieSize() {
        return _movieSize;
    }
    public void setMovieSize(long value) {
        _movieSize = value;
    }

    private Double _fps;
    public Double getFps() {
        return _fps;
    }
    public void setFps(Double value) {
        _fps = value;
    }

    private int _downloads;
    public int getDownloads() {
        return _downloads;
    }
    public void setDownloads(int value) {
        _downloads = value;
    }

    private String _fileName;
    public String getFileName() {
        return _fileName;
    }
    public void setFileName(String value) {
        _fileName = value;
    }

    
}
