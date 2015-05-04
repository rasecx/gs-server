package GsServer;

import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;

public class GsSocketClient extends Thread {

    public static String downloadsConcluidos = "";

    private void conServer() {
        String ipServer = "";

        try {
            Properties p = Utils.getProp();

            ipServer = FrmPrincipal.txtIPServidorGetSeries.getText();

            if (ipServer.equals("")) {
                Utils.escreveLog("Não há nenhum dispotivo android conectado!", 0);
            } else {
                InetSocketAddress inet = new InetSocketAddress(ipServer, Integer.parseInt(p.getProperty("PortaGetSeries")));

                try (Socket s = new Socket()) {
                    s.connect(inet);
                    DataOutputStream dOut = new DataOutputStream(s.getOutputStream());

                    if (s.isConnected()) {
                        dOut.writeUTF(downloadsConcluidos);
                        dOut.flush();

                    }

                    Utils.setNotificaoEnviada(Mensagens.NOTIFICAO_ENVIADA);
                    

                } catch (Exception e) {

                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            //Utils.gravaLog("Conexão recusada pelo host - " + ipServer);
            Utils.escreveLog("O dispositivo " + ipServer + " não está mais conectado", 1);
            e.printStackTrace();

        }

    }

    ;
	
        public void run() {
        conServer();
    }

}
