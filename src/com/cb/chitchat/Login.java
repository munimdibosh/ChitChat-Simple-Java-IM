/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Login.java
 *
 * Created on May 20, 2012, 7:40:04 PM
 */

package com.cb.chitchat;

import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author mangoesmobile
 */
public class Login extends javax.swing.JDialog implements ConnectionManagerDelegate{

    LoginDelegate delegate;
    Login context;
    /** Creates new form Login */
    public Login(java.awt.Frame parent, boolean modal,LoginDelegate del) {
        super(parent, modal);
        initComponents();
        this.delegate=del;
        context=this;
        this.initWindowListener();
    }
    /**
     * Confirmation of exit.
     */
    private void initWindowListener() {
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent event) {
                int ret = JOptionPane.showConfirmDialog(context, "Are you sure?", "Confirm exit...", JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    context.dispose();
                    System.exit(0);
                }
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameField = new javax.swing.JTextField();
        loginButton = new javax.swing.JButton();
        passwordField = new javax.swing.JPasswordField();
        portField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("log in ChitChat...");
        setResizable(false);

        nameField.setForeground(new java.awt.Color(51, 51, 51));
        nameField.setText("username...");
        nameField.setToolTipText("your unique user id");
        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameFieldFocusLost(evt);
            }
        });

        loginButton.setText("login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        passwordField.setForeground(new java.awt.Color(51, 51, 51));
        passwordField.setText("00000");
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                passwordFieldFocusLost(evt);
            }
        });

        portField.setForeground(new java.awt.Color(51, 51, 51));
        portField.setText("port...");
        portField.setToolTipText("port number of your server");
        portField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                portFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                portFieldFocusLost(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(passwordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, portField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, loginButton)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, nameField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(nameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(passwordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(portField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(loginButton)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        // TODO add your handling code here:
        String name=this.nameField.getText().trim();
        String password=new String(this.passwordField.getPassword());
        String port=this.portField.getText();
        if(name.isEmpty() || password.isEmpty() || port.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please fill in the fields", "Error...", JOptionPane.ERROR_MESSAGE);
        }
        else{
            this.delegate.login(name, password,port);
        }

    }//GEN-LAST:event_loginButtonActionPerformed

    // <editor-fold defaultstate="collapsed" desc="Input Fields focus handling">
    private void portFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_portFieldFocusGained
        // TODO add your handling code here:
        if(this.portField.getText().equals("port..."))
            this.portField.setText("");
    }//GEN-LAST:event_portFieldFocusGained

    private void portFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_portFieldFocusLost
        // TODO add your handling code here:
        if(this.portField.getText().trim().equals(""))
        this.portField.setText("port...");
    }//GEN-LAST:event_portFieldFocusLost

    private void nameFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameFieldFocusGained
        // TODO add your handling code here:
        if(this.nameField.getText().equals("username..."))
        this.nameField.setText("");
    }//GEN-LAST:event_nameFieldFocusGained

    private void nameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameFieldFocusLost
        // TODO add your handling code here:
        if(this.nameField.getText().trim().equals(""))
        this.nameField.setText("username...");
    }//GEN-LAST:event_nameFieldFocusLost

    private void passwordFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFieldFocusGained
        // TODO add your handling code here:
        String pw=new String(this.passwordField.getPassword());
        if(pw.equals("00000"))
        this.passwordField.setText("");
    }//GEN-LAST:event_passwordFieldFocusGained

    private void passwordFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFieldFocusLost
        // TODO add your handling code here:
        String pw=new String(this.passwordField.getPassword());
        if(pw.trim().equals(""))
        this.passwordField.setText("00000");
    }//GEN-LAST:event_passwordFieldFocusLost
    //</editor-fold>


    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton loginButton;
    private javax.swing.JTextField nameField;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField portField;
    // End of variables declaration//GEN-END:variables

    //<editor-fold desc="Connection Manager Delegate Methods">
    /**
     * Login has been successful
     */
    public void successful_login()
    {
        this.delegate.successful();
        this.dispose();
    }
    public void unsuccessful_login()
    {
        int YES=JOptionPane.showConfirmDialog(this, "Could not log you in!Retry?", "Login unsuccessful...", JOptionPane.YES_NO_OPTION);
        if(YES==JOptionPane.NO_OPTION)
        {
            System.exit(0);
        }
        
    }
    public void message(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Connection failed...", JOptionPane.ERROR_MESSAGE);
    }

    public void fileBytes(byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void fileInfo(String info) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void notifyServerStop(String msg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void newUserAdded(String userID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onlineUsers(ArrayList<String> users) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     * Existing user
     * @param ID
     */
    public void existing_user(String ID) {
        JOptionPane.showMessageDialog(this, ID+" is already taken.Try another.", "Existing user...", JOptionPane.INFORMATION_MESSAGE);
    }

    public void userRemoved(String ID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void message(String from, String msg, String to) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    

    //</editor-fold>

}
