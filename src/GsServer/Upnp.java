/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GsServer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;
import org.wetorrent.upnp.GatewayDevice;
import org.wetorrent.upnp.GatewayDiscover;
import org.wetorrent.upnp.LogUtils;
import org.xml.sax.SAXException;

/**
 *
 * @author jcferreira
 */
public class Upnp {
    
     public static void main (String[] args){
       // Upnp.mapeaPortas();
         upnpCling();
                
    }

    public static void mapeaPortas() {

        Logger logger = LogUtils.getLogger();
        logger.info("Starting weupnp");
        GatewayDiscover discover = new GatewayDiscover();
        try {

            discover.discover();
            GatewayDevice d = discover.getValidGateway();

            if (null != d) {
                logger.log(Level.INFO, "Gateway device found.\n{0} ({1})", new Object[]{d.getModelName(), d.getModelDescription()});
            } else {
                logger.info("No valid gateway device found.");
                return;
            }

        } catch (UnknownHostException ex) {
            Logger.getLogger(Upnp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Upnp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Upnp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Upnp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void upnpCling(){
    
        RegistryListener listener = new RegistryListener() {

            public void remoteDeviceDiscoveryStarted(Registry registry,
                                                     RemoteDevice device) {
                System.out.println(
                        "Discovery started: " + device.getDisplayString()
                );
            }

            public void remoteDeviceDiscoveryFailed(Registry registry,
                                                    RemoteDevice device,
                                                    Exception ex) {
                System.out.println(
                        "Discovery failed: " + device.getDisplayString() + " => " + ex
                );
            }

            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
                System.out.println(
                        "Remote device available: " + device.getDisplayString()
                );
            }

            public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
                System.out.println(
                        "Remote device updated: " + device.getDisplayString()
                );
            }

            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                System.out.println(
                        "Remote device removed: " + device.getDisplayString()
                );
            }

            public void localDeviceAdded(Registry registry, LocalDevice device) {
                System.out.println(
                        "Local device added: " + device.getDisplayString()
                );
            }

            public void localDeviceRemoved(Registry registry, LocalDevice device) {
                System.out.println(
                        "Local device removed: " + device.getDisplayString()
                );
            }

            public void beforeShutdown(Registry registry) {
                System.out.println(
                        "Before shutdown, the registry has devices: "
                        + registry.getDevices().size()
                );
            }

            public void afterShutdown() {
                System.out.println("Shutdown of registry complete!");

            }
        };
        
         // This will create necessary network resources for UPnP right away
        System.out.println("Starting Cling...");
        UpnpService upnpService = new UpnpServiceImpl(listener);

        // Send a search message to all devices and services, they should respond soon
        upnpService.getControlPoint().search(new STAllHeader());

        // Let's wait 10 seconds for them to respond
        System.out.println("Waiting 10 seconds before shutting down...");
         try {
             Thread.sleep(10000);
         } catch (InterruptedException ex) {
             Logger.getLogger(Upnp.class.getName()).log(Level.SEVERE, null, ex);
         }

        // Release all resources and advertise BYEBYE to other UPnP devices
        System.out.println("Stopping Cling...");
        upnpService.shutdown();
    }
}
