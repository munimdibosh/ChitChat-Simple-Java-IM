/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.chitchat;

import java.io.File;

/**
 *
 * @author mangoesmobile
 */
public interface FileTransferDelegate {

    public void file(File file,String to,FileSendWizard wizard);

}
