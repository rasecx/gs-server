/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GsServer;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author jcferreira
 */
public class SysTray {
    final SystemTray tray = SystemTray.getSystemTray();
    static TrayIcon trayIcon;
    public static boolean isShowing =false;
    public void showTryIcon(){
         //Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        String dir="";
        
        try {
            dir = new File(".").getCanonicalPath();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String caminho =dir+"\\icons\\ic_launcher16x16.png";
        
        Image image = Toolkit.getDefaultToolkit().getImage(caminho);//("..\\..\\GsServer\\GsServer\\icons\\ic_launcher16x16.png");

        final PopupMenu popup = new PopupMenu();
         trayIcon = new TrayIcon(image, "GetSeries Server", popup);
        //trayIcon.setImageAutoSize(true);
        
        
        
       
        // Create a pop-up menu components
        MenuItem showItem = new MenuItem("Abrir");
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu("Display");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem noneItem = new MenuItem("None");
        MenuItem exitItem = new MenuItem("Sair");
       
        //Add components to pop-up menu
        popup.add(showItem);
        popup.addSeparator();
        //popup.add(cb1);
        //popup.add(cb2);
       // popup.addSeparator();
        //popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(noneItem);
        popup.add(exitItem);
       
        trayIcon.setPopupMenu(popup);
        
        
        exitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        showItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FrmPrincipal.frm.setVisible(true);
                tray.remove(trayIcon);
                isShowing =false;
            }
        });
        
        trayIcon.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    FrmPrincipal.frm.setVisible(true);
                    tray.remove(trayIcon);
                    isShowing=false;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        
        
        
       
        try {
            tray.add(trayIcon);
            isShowing =true;
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    
    }
    
    public static void setDisplayMessage(String msg){
    
        if (isShowing)
            trayIcon.displayMessage("Downloads", msg, TrayIcon.MessageType.INFO);
        
        
    
    }
    
}
