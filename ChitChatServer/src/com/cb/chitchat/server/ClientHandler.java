/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.chitchat.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mangoesmobile
 */
/**
 * Multithread enabled client interface.
 * This is actually a server side interface for client portion.
*/
public class ClientHandler implements Runnable
{
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;
    ClientAdditionDelegate client_delegate;
    String userID;
    String password;
    /**
     * 
     * @param clientSocket
     * @param del A server that implements ClientAdditionDelegate
     * @throws IOException
     */
    public ClientHandler(Socket clientSocket,ClientAdditionDelegate del) throws IOException
    {
        this.sock=clientSocket;
	this.openInputStream();
        this.openOutputStream();
        this.client_delegate=del;

    }
    /**
     * Opens the output stream for this client.
     * This is the place where server writes/inputs something and client gets informed.
     * @throws IOException
     */
    private void openOutputStream() throws IOException {
        writer=new PrintWriter(this.sock.getOutputStream());
    }
    /**
     * Opens the input stream for this client.
     * InputStream is actually where the client gives input and the server grabs them.
     * @throws IOException
     */
    private void openInputStream() throws IOException {
        InputStreamReader isReader = new InputStreamReader(this.sock.getInputStream());
        reader = new BufferedReader(isReader);
    }
    /**
     * Writes data in client input stream
     * @param data
     */
    private void writeData(String data) throws IOException
    {
        this.writer.println(data);
        this.writer.flush();
    }
    /**
     * This will keep running the process in separate thread.
     */
    public void run()
    {
        
        try {
            String Msg = "";
            ArrayList<String> tokens=new ArrayList<String>();
            while ((Msg = this.reader.readLine())!=null)
            {
                    if(!tokens.isEmpty())
                    {
                        //not empty,clear.
                        tokens.clear();

                    }
                    //check the msg
                    System.out.println("Inside while, Received msg in client handler:"+Msg);
                    //all the meta datas are separated using :
                    tokens.addAll(Constants.tokens(Msg, ":"));
                    //if it's a login request
                    if(tokens.contains(Constants.LOGIN_ID))
                    {
                        this.userID=tokens.get(1);//the 2nd string of this format LOGIN_ID:username:password
                        this.password=tokens.get(2);
                        this.client_delegate.validate(this);
                    }
                    //if it's a log out request.
                    if(tokens.contains(Constants.LOGOUT_ID))
                    {
                        this.client_delegate.logout(this);
                    }
                    //public room message
                    if(tokens.contains(Constants.PUBLIC_ROOM))
                    {
                        
                        if(tokens.get(1).contains("file"))
                        {
                            //there is smiley,but the parts were separated due to the presence of an internal : after word 'file'
                            this.client_delegate.sendMessage(tokens.get(1)+":"+tokens.get(2));
                        }
                        else
                        {
                            //the message is without smiley...
                            this.client_delegate.sendMessage(tokens.get(1));
                        }
                        
                    }
                    //private message
                    if(tokens.contains(Constants.MESSAGE_ID))
                    {
                        //private message
                        if(tokens.get(2).contains("file"))
                        {
                            //there is smiley,but the parts were separated due to the presence of an internal : after word 'file'
                            this.client_delegate.sendMessageFromThisTo(tokens.get(1), tokens.get(2)+":"+tokens.get(3), tokens.get(4));
                        }
                        else
                        {
                            //the message is without smiley...
                            this.client_delegate.sendMessageFromThisTo(tokens.get(1), tokens.get(2), tokens.get(3));
                        }
                    }
                    if(tokens.contains(Constants.FILE))
                    {
                        this.client_delegate.sendFileTo(tokens.get(1), Integer.parseInt(tokens.get(2)), tokens.get(3));
                    }
                
                  

            }
        } catch (Exception ex) {
            System.out.printf("Error parsing message from client:%s.",this.userID);
        }
    }
    
    /**
     * To notify the client from server end.
     * @param msg
     */
    public void notifyWithMessage(String msg)
    {
        try {
            this.writeData(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * notify each user when a new user added.
     * @param userID
     */
    public void newUserAdded(String ID)
    {
        try
        {
            this.writeData(Message.getNewUserNotifyMessage(ID));
        }catch(Exception ex){ex.printStackTrace();}
    }
    /**
     * User removed notification.
     * @param ID
     */
    public void userRemoved(String ID)
    {
        try {
            this.writeData(Message.getUserRemovedNotificationMessage(ID));
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Send the array of all online users
     * @param users
     */
    public void sendAllOnlineUsers(ArrayList<String> users)
    {
        try {
            this.writeData(Message.getAllUsersMessage(users));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Send the existing user notification.
     * @param ID
     */
    public void notifyExistingUser(String ID)
    {
        try {
            this.writeData(Message.getExistingUserNotifyMessage(ID));
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

      /**
     * Receives file
     */
    public void transferFile(String fileName,int size,String to) throws Exception
    {
            this.notifyWithMessage(Message.getFileReceiveMessage(fileName, size, to));
            int filesize=size; // filesize temporary hardcoded


            int bytesRead;
            int current = 0;
            
            // receive file
            byte [] mybytearray  = new byte [filesize];
            InputStream is = sock.getInputStream();
            FileOutputStream fos = new FileOutputStream(System.getProperty("user.home")+"/"+fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;

            // thanks to A. Cádiz for the bug fix
            do {
               bytesRead =
                  is.read(mybytearray, current, (mybytearray.length-current));
               if(bytesRead >= 0) current += bytesRead;
            } while(bytesRead > -1);

            bos.write(mybytearray, 0 , current);
            bos.flush();
            bos.close();
    }
    /**
     * Sends the file to user
     * @param file
     * @param to
     */
    public void sendFile(File file)
    {
        byte [] mybytearray  = new byte [(int)file.length()];
        FileInputStream fis;
        try {
            
            fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(mybytearray,0,mybytearray.length);
            OutputStream os = sock.getOutputStream();
            os.write(mybytearray,0,mybytearray.length);
            os.flush();
            
        } catch (Exception ex) {
            
        }

    }


}
