/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.chitchat.server;

/**
 *
 * @author mangoesmobile
 */
public interface ServerDelegate {

    //send the current server status to UI.
    public void updateStatus(String text);
    //if the flag is false the button will be disabled.
    public void updateStopButtonState(boolean flag);

}
