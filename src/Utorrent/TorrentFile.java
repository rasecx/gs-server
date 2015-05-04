package Utorrent;

import org.json.JSONArray;
import org.json.JSONException;



/**
 * A file contained within a torrent
 * @author glenn
 *
 */
public class TorrentFile {
	public static int DONT_DOWNLOAD = 0;
	public static int LOW = 1;
	public static int NORMAL = 2;
	public static int HIGH = 3;
	
	private String _fileName;
	private long _fileSize;
	private long _downloaded;
	private int _priority;
	private String _torrentHash;
	
	

	public TorrentFile(JSONArray array) {
		try {
			_fileName = array.getString(0);
			_fileSize = array.getInt(1);
			_downloaded = array.getInt(2);
			_priority = array.getInt(3);
		}
		catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
        
        public TorrentFile() {
	
	}
	
	public final long getDownloaded() {
		return _downloaded;
	}
	
	public final String getFileName() {
		return _fileName;
	}

	public final int getPriority() {
		return _priority;
	}
	
	public final void setPriority(int value) {
		_priority = value;
	}
	
	public final String getPriorityString() {
		switch (_priority) {
		case 0:
			return "Não Baixar";//"Don't Download";
		case 1:
			return "Baixa";//"Low Priority";
		case 2:
			return "Normal";//Normal Priority";
		case 3:
			return "Alta";//"High priority";
		default:
			return "Desconheciada";//"Unknown";
		}
	}

	public final long getFileSize() {
		return _fileSize;
	}
	
	public double getProgress() {
		return (_downloaded / (double)_fileSize) * 100;
	}
	public String getTorrentHash() {
		return _torrentHash;
	}

	public void setTorrentHash(String _torrentHash) {
		this._torrentHash = _torrentHash;
	}
}
