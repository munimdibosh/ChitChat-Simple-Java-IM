/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.chitchat;

import java.util.ArrayList;

/**
 *
 * @author mangoesmobile
 */
public interface ConnectionManagerDelegate {
    //<editor-fold desc="Login Form Related delegation">
    public void successful_login();
    public void unsuccessful_login();
    public void existing_user(String ID);
    //</editor-fold>
    //<editor-fold desc="Main Client Related Delegation">
    /**
     * Plain message
     * @param msg
     */
    public void message(String msg);
    /**
     * Send the public message to delegate.
     * @param msg
     */
    public void message(String from,String msg,String to);
   
    /**
     * File transferring bytes.
     * @param bytes
     */
    public void fileBytes(byte[] bytes);
    /**
     * To be transferred file's info.
     * @param info
     */
    public void fileInfo(String info);
    /**
     * Updates the client with server stop event.
     * @param msg
     */
    public void notifyServerStop(String msg);
    /**
     * Updates the list view with new user.
     * @param userID
     */
    public void newUserAdded(String userID);
    /**
     * the online users received from server.
     * @param users
     */
    public void onlineUsers(ArrayList<String> users);
    public void userRemoved(String ID);
    //</editor-fold>

}
