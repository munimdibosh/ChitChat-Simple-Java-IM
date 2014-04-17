/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.chitchat.server;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author mangoesmobile
 */
public class ServerLauncher {

    private static final int DEFAULT_USERS=10;
    private static void setUpLookUpAndFeel() {
        try {
                    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception e) {
                    // If Nimbus is not available, you can set the GUI to another look and feel.
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception ex) {
                    }
                    int val = JOptionPane.showConfirmDialog(new JFrame(), "Please make sure that you have at least JRE Java SE 6 Update 10 (6u10) release\n" + "installed to enable polished UI." + "\nOk-to continue,Cancel-to open latest JRE download page.", "", JOptionPane.OK_CANCEL_OPTION);
                    if (val == JOptionPane.CANCEL_OPTION) {
                        String url = "http://www.oracle.com/technetwork/java/javase/downloads/index.html";
                        try {
                            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
    }
   
   public ServerLauncher()
   {
        try {
            this.createDefaultUsers();
            ServerUI server=new ServerUI(new JFrame(),false);
            server.setLocationRelativeTo(null);
            server.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
   }

   
   public static void main(String args[])
   {
       setUpLookUpAndFeel();
       ServerLauncher launcher=new ServerLauncher();

       
   }
   /**
    * creates some users
    */
   private void createDefaultUsers(){
       // TODO add your handling code here:
        FileWriter file=null;
        PrintWriter writer=null;
        try {
                file= new FileWriter("user");
                writer=new PrintWriter(file);
                for(int i=1;i<=DEFAULT_USERS;i++)
                {
                    writer.append("User"+Integer.toString(i)+":"+"12345"+"\n");
                }

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            try {
                //close all buffers
                file.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            writer.close();
            
        }
    }
   
}
