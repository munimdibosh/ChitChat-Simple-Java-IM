/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MessagePanel.java
 *
 * Created on May 26, 2012, 8:04:19 AM
 */

package com.cb.chitchat;

import java.awt.event.KeyEvent;

/**
 *
 * @author mangoesmobile
 */
public class MessagePanel extends javax.swing.JPanel {

    MessagePanelDelegate delegate;
    
    String ID;
    //serial in chat panels array list in main window.
    int serial;
    
    /**
     * Chat Pane with an User
     * @param del
     * @param with the user with whom the chat will be initiated.
     * @param n serial of this chat panel 
     */
    public MessagePanel(MessagePanelDelegate del,String with,int n) {
        initComponents();
        this.delegate=del;
        this.ID=with;
        this.serial=n;
    }
    /**
     * Sets the serial of  this pane.
     * @param n
     */
    public void setSerial(int n)
    {
        this.serial=n;
    }
    /**
     * Returns the serial of this chat client.
     * @return
     */
    public int getSerial()
    {
        return this.serial;
    }
    /**
     * Sets the ID for this panel.
     * @param id
     */
    public void setID(String id)
    {
        this.ID=id;
    }
    /**
     * Gets the ID for this panel.
     * @return
     */
    public String getID()
    {
        return this.ID;
    }
    /**
     * Updates the chat pane.
     * @param text
     */
    public void setTextForMessagePane(String text)
    {
       this.messagePane.setText(text);
       this.messagePane.invalidate();
            
    }
    /**
     * Get the current text in chat pane.
     * @return
     */
    public String getCurrentTextInMessagePane()
    {
        return this.messagePane.getText();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        messagePane = new javax.swing.JEditorPane();
        messageField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();

        messagePane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        messagePane.setContentType("text/html");
        messagePane.setEditable(false);
        messagePane.setText("<html><body></body><html>");
        jScrollPane1.setViewportView(messagePane);

        messageField.setForeground(new java.awt.Color(51, 51, 51));
        messageField.setText("message...");
        messageField.setToolTipText("type message here.add space between emoticon and your text.");
        messageField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                MessagePanel.this.focusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                MessagePanel.this.focusLost(evt);
            }
        });
        messageField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                messageFieldKeyPressed(evt);
            }
        });

        sendButton.setText("send...");
        sendButton.setToolTipText("send the message...");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(messageField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sendButton))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 265, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(messageField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(sendButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void focusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_focusLost
        // TODO add your handling code here:
        if(this.messageField.getText().trim().equals(""))
        {
            this.messageField.setText("message...");
        }
    }//GEN-LAST:event_focusLost

    private void focusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_focusGained
        // TODO add your handling code here:
        if(this.messageField.getText().trim().equals("message..."))
        {
            this.messageField.setText("");
        }
    }//GEN-LAST:event_focusGained
    /**
     * User pressed enter after completing message
     * @param evt
     */
    private void messageFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
        {
            String txt=this.messageField.getText().trim();
            if(!txt.isEmpty())
            {
                this.delegate.textFromMessageField(txt,this.ID,this.serial);
                this.messageField.setText("");
            }
        }
    }//GEN-LAST:event_messageFieldKeyPressed
    /**
     * User pressed send button
     * @param evt
     */
    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        // TODO add your handling code here:
         String txt=this.messageField.getText().trim();
            if(!txt.isEmpty())
            {
                this.delegate.textFromMessageField(txt,this.ID,this.serial);
                this.messageField.setText("");
            }

    }//GEN-LAST:event_sendButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField messageField;
    private javax.swing.JEditorPane messagePane;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables

}
