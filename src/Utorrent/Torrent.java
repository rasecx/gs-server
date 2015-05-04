package Utorrent;

import GsServer.Utils;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * represents a torrent and related information as provided by the utorrent API
 *
 * @author glenn
 *
 */
public class Torrent {

    private static String[] arrayUnidades = {"B", "KB", "MB", "GB", "TB"};
    public static String PAUSED = "Pausado";//"Paused";
    public static String SEEDING = "Enviando";//"Seeding";
    public static String DOWNLOADING = "Baixando";//"Downloading";
    public static String FORCEDSEEDING = "Enviando (Forçado)";//"Seeding (Forced)";
    public static String FORCEDDOWNLOADING = "Baixando (Forçado)";//"Downloading (Forced)";
    public static String CHECKING = "Verificando";//"Checking";
    public static String ERROR = "Error";
    public static String QUEUED = "Enfileirando";//"Queued";
    public static String FINISHED = "Concluido";//"Finished";
    public static String STOPPED = "Parado";//"Stopped";
    public static String CONNECTION_TO_PEERS = "Conectando aos Peers";

    private String _hash;
    private int _status;
    private String _name;
    private long _size;
    private int _progress;
    private long _downloaded;
    private long _uploaded;
    private int _ratio;
    private long _uploadSpeed;
    private long _downloadSpeed;
    private long _eta;
    private String _label;
    private int _peersConnected;
    private int _peersInSwarm;
    private int _seedsConnected;
    private int _seedsInSwarm;
    private long _availability;
    private int _queueOrder;
    private long _remaining;
    private String formatedSize;
    private String statusStringFromArray;
    private String fileLocation;
    private List<TorrentFile> filesInTorrent;
    private List<TorrentFile> movieFiles;

    public Torrent(JSONArray array) {
        try {

            setHash(array.getString(0));
            setStatus(array.getInt(1));
            setName(array.getString(2));
            setSize(array.getLong(3));

            _progress = array.getInt(4);
            _downloaded = array.getLong(5);
            _uploaded = array.getLong(6);
            _ratio = array.getInt(7);
            _uploadSpeed = array.getLong(8);
            _downloadSpeed = array.getLong(9);
            _eta = array.getLong(10);
            _label = array.getString(11);
            _peersConnected = array.getInt(12);
            _peersInSwarm = array.getInt(13);
            _seedsConnected = array.getInt(14);
            _seedsInSwarm = array.getInt(15);
            _availability = array.getLong(16);
            _queueOrder = array.getInt(17);
            _remaining = array.getLong(18);
            //setFormatedSize(_size);
            statusStringFromArray = array.getString(21); //v 0.388 webUI
            setFileLocation(array.getString(26));
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public Torrent() {

    }

    public final long getAvailability() {
        return _availability;
    }

    public final long getDownloaded() {
        return _downloaded;
    }

    public final long getDownloadSpeed() {
        return _downloadSpeed;
    }

    public final long getEta() {
        return _eta;
    }

    public final String getHash() {
        return _hash;
    }

    public final String getLabel() {
        return _label;
    }

    public final String getName() {
        return _name;
    }

    public final int getPeersConnected() {
        return _peersConnected;
    }

    public final int getPeersInSwarm() {
        return _peersInSwarm;
    }

    public final int getProgress() {
        return _progress;
    }

    public final int getQueueOrder() {
        return _queueOrder;
    }

    public final int getRatio() {
        return _ratio;
    }

    public final long getRemaining() {
        return _remaining;
    }

    public final int getSeedsConnected() {
        return _seedsConnected;
    }

    public final int getSeedsInSwarm() {
        return _seedsInSwarm;
    }

    public final long getSize() {
        return _size;
    }

    public final int getStatus() {
        return _status;
    }

    public final String getStatusString() {
        boolean flag = false;
        String ret = "";

        if ((_status & 1) == 1) { //Started
            if ((_status & 32) == 32) { //paused
                ret = PAUSED;
                flag = true;
            } else { //seeding or leeching
                if ((_status & 64) == 64) {
                    ret = (_progress == 1000) ? SEEDING : DOWNLOADING;
                    flag = true;
                } else {
                    ret = (_progress == 1000) ? FORCEDSEEDING : FORCEDDOWNLOADING;
                    flag = true;
                }
            }
        } else if ((_status & 2) == 2) { //checking
            ret = CHECKING;
            flag = true;
        } else if ((_status & 16) == 16) { //error
            ret = ERROR;
            flag = true;
        } else if ((_status & 64) == 64) { //queued
            ret = QUEUED;
            flag = true;
        }

        if (_progress == 1000 && !flag) {
            ret = FINISHED;
        } else if (_progress < 1000 && !flag) {
            ret = STOPPED;
        }

        if (_name.contains("magnet")) {
            ret = getStatusStringFromArray();
        }

        return ret;
    }

    public static String converterBytes(long valor) {
        int potencia = 0;
        int proxima = 0;
        boolean cond1;
        boolean cond2;
        if (valor > 0) {
            NumberFormat ftTamamho = NumberFormat.getIntegerInstance();
            Double tamanho = new Double(0.00);
            String strTamanhoFormatado;
            proxima = potencia + 1;
            cond1 = (Math.pow(2, potencia * 10) <= valor);
            cond2 = (valor < Math.pow(2, proxima * 10));
            potencia++;

            while (!(cond1 && cond2)) {
                proxima = potencia + 1;
                cond1 = (Math.pow(2, potencia * 10) <= valor);
                cond2 = (valor < Math.pow(2, proxima * 10));
                potencia++;
            }
            potencia--;
            tamanho = valor / Math.pow(2, potencia * 10);
            ftTamamho.setMaximumFractionDigits(2);
            strTamanhoFormatado = ftTamamho.format(tamanho);
            return strTamanhoFormatado + " " + arrayUnidades[potencia];
        } else {
            return "0 MB";
        }
    }

    public final long getUploaded() {
        return _uploaded;
    }

    public final long getUploadSpeed() {
        return _uploadSpeed;
    }

    public void setHash(String _hash) {
        this._hash = _hash;
    }

    public void setStatus(int _status) {
        this._status = _status;

    }

    public void setName(String _name) {
        this._name = _name;
    }

    public void setSize(long _size) {
        this._size = _size;
        setFormatedSize(getSize());
    }

    public void setProgress(int _progress) {
        this._progress = _progress;
    }

    public void setDownloaded(long _downloaded) {
        this._downloaded = _downloaded;
    }

    public void setUploaded(long _uploaded) {
        this._uploaded = _uploaded;
    }

    public void setRatio(int _ratio) {
        this._ratio = _ratio;
    }

    public void setUploadSpeed(long _uploadSpeed) {
        this._uploadSpeed = _uploadSpeed;
    }

    public void setDownloadSpeed(long _downloadSpeed) {
        this._downloadSpeed = _downloadSpeed;
    }

    public void setEta(long _eta) {
        this._eta = _eta;
    }

    public void setLabel(String _label) {
        this._label = _label;
    }

    public void setPeersConnected(int _peersConnected) {
        this._peersConnected = _peersConnected;
    }

    public void setPeersInSwarm(int _peersInSwarm) {
        this._peersInSwarm = _peersInSwarm;
    }

    public void setSeedsConnected(int _seedsConnected) {
        this._seedsConnected = _seedsConnected;
    }

    public void setSeedsInSwarm(int _seedsInSwarm) {
        this._seedsInSwarm = _seedsInSwarm;
    }

    public void setAvailability(long _availability) {
        this._availability = _availability;
    }

    public void setQueueOrder(int _queueOrder) {
        this._queueOrder = _queueOrder;
    }

    public void setRemaining(long _remaining) {
        this._remaining = _remaining;
    }

    public String getFormatedSize() {
        return formatedSize;
    }

    public void setFormatedSize(long size) {
        this.formatedSize = converterBytes(size);
    }

    public String getStatusStringFromArray() {
        return statusStringFromArray;
    }

    public void setStatusStringFromArray(String statusStringFromArray) {
        this.statusStringFromArray = statusStringFromArray;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    /**
     * @return the filesInTorrent
     */
    public List<TorrentFile> getFilesInTorrent() {
        List<TorrentFile> f = new RequestUtorrent().getFilesInTorrent(this);
        for (TorrentFile tFile : f) {
            filesInTorrent.add(tFile);
        }

        return filesInTorrent;
    }

    /**
     * @return the movieFile
     */
    public List<TorrentFile> getMovieFiles() {
        movieFiles = new ArrayList<TorrentFile>();
        List<TorrentFile> f = new RequestUtorrent().getFilesInTorrent(this);
        for (TorrentFile tFile : f) {
            if(tFile.getFileName().contains("sample"))
                continue;
            if (Utils.isExtVideoValida(Utils.retornaExtensao(tFile.getFileName()))) {
                movieFiles.add(tFile);
            }
        }

        return movieFiles;
    }


}
