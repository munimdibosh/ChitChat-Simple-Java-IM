/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.chitchat;

/**
 *
 * @author mangoesmobile
 */
public interface FileTransferUpdateDelegate {

    public void progress(int n);
    public void finished();

}
