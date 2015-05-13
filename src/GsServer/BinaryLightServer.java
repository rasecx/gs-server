/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GsServer;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.Icon;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;

/**
 *
 * @author Julio
 */
public class BinaryLightServer implements Runnable {

    public static void main(String[] args) throws Exception {
        // Start a user thread that runs the UPnP stack
        Thread serverThread = new Thread(new BinaryLightServer());
        serverThread.setDaemon(false);
        serverThread.start();
    }

    public void run() {
        try {

            final UpnpService upnpService = new UpnpServiceImpl();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    upnpService.shutdown();
                }
            });

            // Add the bound local device to the registry
            upnpService.getRegistry().addDevice(
                    createDevice()
            );

        } catch (Exception ex) {
            System.err.println("Exception occured: " + ex);
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    LocalDevice createDevice()
        throws ValidationException, LocalServiceBindingException, IOException, URISyntaxException {

    DeviceIdentity identity =
            new DeviceIdentity(
                    UDN.uniqueSystemIdentifier("Demo Binary Light")
            );

    DeviceType type =
            new UDADeviceType("BinaryLight", 1);

    DeviceDetails details =
            new DeviceDetails(
                    "Friendly Binary Light",
                    new ManufacturerDetails("ACME"),
                    new ModelDetails(
                            "BinLight2000",
                            "A demo light with on/off switch.",
                            "v1"
                    )
            );

    Icon icon =
            new Icon(
                    "image/png", 48, 48, 8,
                    new File("C:\\Users\\Julio\\Documents\\NetBeansProjects\\GsServer\\GsServer\\icons\\ic_launcher32x32.png")
            );

    LocalService<SwitchPower> switchPowerService =
            new AnnotationLocalServiceBinder().read(SwitchPower.class);

    switchPowerService.setManager(
            new DefaultServiceManager(switchPowerService, SwitchPower.class)
    );

    return new LocalDevice(identity, type, details, icon, switchPowerService);

    /* Several services can be bound to the same device:
    return new LocalDevice(
            identity, type, details, icon,
            new LocalService[] {switchPowerService, myOtherService}
    );
    */
    
}

}