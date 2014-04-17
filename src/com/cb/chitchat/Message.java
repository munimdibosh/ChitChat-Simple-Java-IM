/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.chitchat;
import java.util.ArrayList;

/**
 * Creates message for different action formats.
 * These message formats are known in client and server.
 * Serves as the connection protocol of this chat communication.
 * @author mangoesmobile
 */
public class Message {

    /**
     * generates proper formatted message for login
     * @param ID
     * @param password
     * @return
     */
    public static String getLoginMessage(String ID,String password)
    {
        return Constants.LOGIN_ID+":"+ID+":"+password;
    }
    
    
    /**
     * Creates a smiley added(if available) public message in format messagetoken:from:color code of from:message
     * @param message
     * 
     * @return
     */
    public static String getPublicMessage(String message)
    {
        
        return Constants.PUBLIC_ROOM+":"+message;
    }
    /**
     * Creates a smiley added(if available) private message in format messagetoken:from:color code of from:message:to
     * @param fromID
     * @param toID
     * @param message
     * @param colorCode
     * @return
     */
    public static String getPrivateMessage(String fromID,String message,String toID)
    {
        
        return Constants.MESSAGE_ID+":"+fromID+":"+message+":"+toID;
    }

    /**
     * Generates notification message.
     * @param msg
     * @return
     */
    public static String getNotificationMessage(String msg)
    {
        return Constants.NOTIFICATION_ID+":"+msg;
    }
    /**
     * Start receiving file .
     * @param fileName
     * @param filesize
     * @return
     */
    public static String getFileReceiveMessage(String fileName,int filesize)
    {
        return Constants.RECEIVE+":"+fileName+":"+filesize;
    }

    public static String getFileReceivedConfirmationMessage(String fileName,String dir,String to)
    {
        return "Downloaded file "+fileName+" at location "+dir+" from "+to;
    }
    /**
     * File transfer request
     * @param fileName
     * @param to
     * @return
     */
    public static String getFileTransferMessage(String fileName,String to,int filesize)
    {
        return Constants.FILE+":"+fileName+":"+filesize+":"+to;
    }
    /**
     * Generates the new user notification.
     * @param userID
     * @return
     */
    public static String getNewUserNotifyMessage(String userID)
    {
        return Constants.NEW_USER+":"+userID;
    }
    /**
     * Creates the message containing all online user names.
     * @param users
     * @return
     */
    public static String getAllUsersMessage(ArrayList<String> users)
    {
        String retval=Constants.ALL_USERS;
        for(String user : users)
        {
            retval+=user+":";
        }
        return retval;
    }

}
