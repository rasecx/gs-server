/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GsServer;

import static GsServer.FrmPrincipal.LINGUA;
import static GsServer.FrmPrincipal.frameHint;
import Utorrent.Torrent;
import Utorrent.TorrentFile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Julio
 */
public class Utils {

    public static String ext[]
            = {".3g2", ".3gp", ".3gp2", ".3gpp", ".60d", ".ajp", ".asf", ".asx",
                ".avchd", ".avi", ".bik", ".bix", ".box", ".cam", ".dat",
                ".divx", ".dmf", ".dv", ".dvr-ms", ".evo", ".flc", ".fli", ".flic",
                ".flv", ".flx", ".gvi", ".gvp", ".h264", ".m1v", ".m2p", ".m2ts", ".m2v",
                ".m4e", ".m4v", ".mjp", ".mjpeg", ".mjpg", ".mkv", ".moov", ".mov",
                ".movhd", ".movie", ".movx", ".mp4", ".mpe", ".mpeg", ".mpg", ".mpv", ".mpv2",
                ".mxf", ".nsv", ".nut", ".ogg", ".ogm", ".omf", ".ps", ".qt", ".ram", ".rm", ".rmvb",
                ".swf", ".ts", ".vfw", ".vid", ".video", ".viv", ".vivo", ".vob", ".vro", ".wm", ".wmv",
                ".wmx", ".wrap", ".wvx", ".wx", ".x264", ".xvid"
            };

    private static final String[] arrayUnidades = {"B", "KB", "MB", "GB", "TB"};
    public static boolean isShowingBallon = false;
    private static boolean firstUpdate = false;

    public static String[] montaExtFiltro() {
        String extFiltro[] = new String[ext.length];
        for (int i = 0; i < ext.length; i++) {
            extFiltro[i] = ext[i].replace(".", "");
        }
        return extFiltro;
    }

    public static String removeExtensao(String nomeArquivo) {
        String ret, ext = "";
        if (nomeArquivo.lastIndexOf(".") > 0) {
            ext = nomeArquivo.substring(nomeArquivo.lastIndexOf("."), nomeArquivo.length());
            ret = nomeArquivo.substring(0, nomeArquivo.length() - ext.length());
        } else {
            ret = nomeArquivo;
        }

        return ret;
    }

    public static String retornaExtensao(String nomeArquivo) {

        if (nomeArquivo.lastIndexOf(".") > 0) {
            return nomeArquivo.substring(nomeArquivo.lastIndexOf("."), nomeArquivo.length());
        } else {
            return nomeArquivo;
        }
    }

    public static Boolean isExtVideoValida(String extArquivo) {
        Boolean ret = false;

        for (int k = 0; k < ext.length; k++) {
            if (extArquivo.equals(ext[k])) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public static Boolean isVideoTemLegenda(Torrent t) {
        Boolean ret = false;
        if (!t.getMovieFiles().isEmpty()) {
            for (TorrentFile tf : t.getMovieFiles()) {
                File f = new File(escreveUri(t.getFileLocation(), nomeaLegenda(tf.getFileName())));
                if (!f.canRead()) {
                    ret = false; //se qualquer um dos arquivos nao tiver legenda considera que nao tem legenda
                    break;
                } else {
                    ret = true;
                }
            }
        } else {
            ret = false;
        }

        return ret;
    }

    public static Boolean isVideoTemLegenda(String localArquivo, String nomeArquivo) {
        File f = new File(escreveUri(localArquivo, nomeaLegenda(nomeArquivo)));
        return f.canRead();//?true:false;

    }

    public static String nomeaLegenda(String nomeArquivo) {
        return removeExtensao(nomeArquivo) + ".srt";
    }

    public static String escreveUri(String localArquivo, String nomeArquivo) {
        return localArquivo + "\\" + nomeArquivo;
    }

    public static String converterBytes(long valor) {
        int potencia = 0;
        int proxima = 0;
        boolean cond1;
        boolean cond2;
        if (valor > 0) {
            NumberFormat ftTamamho = NumberFormat.getIntegerInstance();
            Double tamanho; //= new Double(0.0);
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

    public static String readStream(InputStream in) {

        String arquivo = "";
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "ISO-8859-1"));
            while ((arquivo = reader.readLine()) != null) {
                sb.append(arquivo).append("\n\r");

            }
            reader.close();
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(SubDb.class.getName()).log(Level.SEVERE, null, ex);
            //return "";
        }

        return sb.toString();
    }

    public static Properties getProp() {
        Properties props = new Properties();
        try {

            String dir = new File(".").getCanonicalPath();
            String caminho = dir + "\\configs.properties";

            try (InputStream file = new FileInputStream(caminho);//(".\\src\\GsServer\\configs.properties") //
                    ) {
                props.load(file);
            }

        } catch (Exception e) {
            return null;
        }

        return props;
    }

    public static String getDataHora() {
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return dateFormat.format(cal.getTime());
    }

    public static void escreveLog(String msg, Integer tipoMsg) {
        Color cor = Color.blue;
        if (tipoMsg == 0) {
            cor = new Color(0, 0, 255);
        } else if (tipoMsg == 1) {
            cor = new Color(255, 0, 0);
        } else if (tipoMsg == 2) {
            cor = new Color(0, 150, 0);
        }

        StyledDocument doc = FrmPrincipal.txtPanel.getStyledDocument();
        Style style = FrmPrincipal.txtPanel.addStyle("Stilo", null);
        StyleConstants.setForeground(style, cor);
        String txt =FrmPrincipal.txtPanel.getText();
        String espaco ="             ";
        String msgQuebra[] =msg.split("\n");
        msg ="";
        for(String s:msgQuebra){
            msg+=espaco+s+"\n";
        }
        String linha ="------------------------------------------------------------------";
        try {
           
            if(!txt.contains(msg))
                doc.insertString(doc.getLength(), "\n" + getDataHora() + "\n" +msg+linha, style);
            
            
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void limpaLog() {
        FrmPrincipal.txtPanel.setText("");
        FrmPrincipal.txtPanel.setText(getDataHora());
    }

    public static void escreveInfoDownload(String msg, Integer tipoMsg) {

        Color cor = Color.blue;
        if (tipoMsg == 0) {
            cor = new Color(0, 0, 255);
        } else if (tipoMsg == 1) {
            cor = new Color(255, 0, 0);
        } else if (tipoMsg == 2) {
            cor = new Color(51, 153, 0);
        }

        try {
            Document doc = pnBallonHint.txtPanelInfoHint.getDocument();
            Style style = pnBallonHint.txtPanelInfoHint.addStyle("Stilo", null);
            StyleConstants.setForeground(style, cor);

            doc.insertString(doc.getLength(), msg + "\n", style);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

    public static void showBallonHint(Boolean show) {

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int w = d.width;
        int h = d.height;

        if (show) {
            isShowingBallon = true;

            JPanel f = new pnBallonHint();
            frameHint.setUndecorated(true);
            frameHint.setBackground(new Color(255, 255, 204));
            frameHint.setSize(538, 327);
            f.setSize(frameHint.getWidth(), frameHint.getHeight());
            frameHint.setLocation(w - frameHint.getWidth() - 10, h - frameHint.getHeight() - 60);
            frameHint.add(f);
            frameHint.setVisible(show);

//            Document doc = pnBallonHint.txtPanelInfoHint.getDocument();
//            ScrollingDocumentListener scroll = new ScrollingDocumentListener();
//            DefaultCaret caret = (DefaultCaret) pnBallonHint.txtPanelInfoHint.getCaret();
//            caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
//            doc.addDocumentListener(scroll);
        } else {
            frameHint.dispose();
            isShowingBallon = false;
        }

    }

    public static void populaComboLingua() {
        for (String l : Idioma.lingus) {
            FrmPrincipal.cbxLingua.addItem(l);
        }
    }

    public static void carregaConfiguracoes() {

        Properties p = getProp();
        FrmPrincipal.edtIntervalo.setText(p.getProperty("IntervaloBusca"));
        FrmPrincipal.cbxLingua.setSelectedIndex(Integer.valueOf(p.getProperty("linguaLegenda")));
        FrmPrincipal.edtIpUtorrent.setText(p.getProperty("ServerUtorrent"));
        FrmPrincipal.edtPortaUtorrent.setText(p.getProperty("PortaUtorrent"));
        FrmPrincipal.edtUsuarioUtorrent.setText(p.getProperty("UsuarioUtorrent"));
        FrmPrincipal.edtSenhaUtorrent.setText(p.getProperty("SenhaUtorrent"));
        FrmPrincipal.edtPortaGetSeries.setText(p.getProperty("PortaGetSeries"));
        FrmPrincipal.chkAvisoTorrentConcluido.setSelected(Boolean.valueOf(p.getProperty("AvisaConclusao")));
        FrmPrincipal.chkHabilitaAtalho.setSelected(Boolean.valueOf(p.getProperty("HabilitaAtalho")));
        FrmPrincipal.rdHabilitaDiretorio.setSelected(Boolean.valueOf(p.getProperty("HabilitaArquivoPasta")));
        FrmPrincipal.txtIntervaloNotificacao.setText(p.getProperty("IntervaloNotificacao"));

        LINGUA = Idioma.SelecionaLinguagem[Integer.valueOf(p.getProperty("linguaLegenda"))];

    }

    public static void escreviArquivoLog(String msg) {

        try {
            String dir = new File(".").getCanonicalPath();
            File file = new File(dir + "\\log.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileInputStream in = new FileInputStream(file);

            FileOutputStream arquivo = new FileOutputStream(dir + "\\log.txt");
            int bytesRead = 0;
            while ((bytesRead = in.read()) != -1) {
                arquivo.write(bytesRead);
            }

            in.close();
            arquivo.close();

        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void soNumero(KeyEvent e) {
        char c = e.getKeyChar();
        if (!(((c >= '0') && (c <= '9')) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
            e.consume();
        }
    }

    public static boolean verificarIPValido(String ip) {
        String ipRegex = "\\d{1,3}(\\.\\d{1,3}){3}";
//        Pattern pattern = Pattern.compile(ipRegex);
//        Matcher matcher = pattern.matcher(ip);
//        return matcher.matches();
        return ip.matches(ipRegex);

    }

    public static void setNotificaoEnviada(String enviada) {
        Properties p;
        try {
            p = getProp();
            String dir = new File(".").getCanonicalPath();
            File file = new File(dir + "\\configs.properties");
            p.setProperty("NotificaoEnviada", enviada);
            FileOutputStream fos = new FileOutputStream(file);

            p.store(fos, "");

            fos.flush();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void verificaVersao(){
        String versao =System.getProperty("java.version");
        Double ver = Double.valueOf(versao.substring(0, 3));
        if (ver<1.7){
            JOptionPane.showMessageDialog(null, "A versao do Java("+versao+") da sua maquina esta Obsoleta\n"
                    + "favor atualizar para 1.7 ou superior","Erro de Versão",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
           
        }else{
           
        }
        
    }
}
