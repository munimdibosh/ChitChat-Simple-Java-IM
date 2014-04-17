/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.chitchat;

/**
 *
 * @author mangoesmobile
 */
public interface LoginDelegate {
    public void login(String ID,String password,String port);
    /**
     * called when the login is successful.
     */
    public void successful();
   
}
