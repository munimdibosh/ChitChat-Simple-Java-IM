/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.chitchat.server;

/**
 *
 * @author mangoesmobile
 */
public interface ClientAdditionDelegate
{
    /**
     * Client added confirmation.
     * @param ID
     * @param password
     */
    public void validate(ClientHandler client);
    /**
     * Logs out this client from server
     * @param client
     */
    public void logout(ClientHandler client);
    /**
     * Message to specific client
     * @param fromID the client itself
     * @param message
     * @param toID the receiver
     */
    public void sendMessageFromThisTo(String fromID,String message,String toID);
    /**
     * Message to public room.
     * @param fromID the client itself
     * @param message
     */
    public void sendMessage(String message);
    /**
     * File stream to user
     * @param buffer
     */
    public void sendBytesFromThisTo(String fromID,byte[] buffer,String toID);
    /**
     * Sends the file.
     * @param fileName
     * @param size
     * @param to
     */
    public void sendFileTo(String fileName,int size,String to);
}
