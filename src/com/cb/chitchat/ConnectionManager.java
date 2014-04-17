/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.chitchat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mangoesmobile
 */
public class ConnectionManager implements Runnable
{

    Socket clientSock;
    PrintWriter writer;
    BufferedReader reader;
    int port;
    String userHomeDir="";
    ConnectionManagerDelegate delegate;
    public ConnectionManager(int port,ConnectionManagerDelegate del)
    {
        this.port=port;
        this.delegate=del;
        this.setNetwork(port);
        this.userHomeDir=System.getProperty("user.home")+"/";
    }
    /**
     * Set the delegate for this connection.
     * @param del
     */
    public void setDelegate(ConnectionManagerDelegate del)
    {
        this.delegate=del;
    }
    /**
     * Sets the connection with server in appropriate port
     * @param port
     */
    private void setNetwork(int port)
    {
        try
        {
            clientSock=new Socket("localhost",port);
            writer=new PrintWriter(this.clientSock.getOutputStream());
            InputStreamReader isReader = new InputStreamReader(this.clientSock.getInputStream());
            reader = new BufferedReader(isReader);
        }catch(Exception ex)
        {
            this.delegate.message("Port might be unavailable."+ex.getMessage()+".");
        }
    }
    /**
     * Continuous ping to server for any incoming.
     */
    private void waitForServerMessage()
    {
        try {
            String Msg = "";
            ArrayList<String> tokens=new ArrayList<String>();
            while ((Msg=this.read())!=null)
            {
                if(!tokens.isEmpty())
                {
                    //not empty,clear.
                    tokens.clear();
                    
                }
                //check the incoming for validation
                System.out.println("Inside Client,ConManager,MSG received:"+Msg);
                //all the meta datas are separated using :
                tokens.addAll(Constants.tokens(Msg, ":"));
                //if this user is existing
                if(tokens.contains(Constants.EXISTING_USER))
                {
                    this.delegate.existing_user(tokens.get(1));
                }
                //if login successful
                if(tokens.contains(Constants.LOGIN_SUCCESSFUL))
                {
                    this.delegate.successful_login();
                }
                //if it's  unsuccessful.
                if(tokens.contains(Constants.LOGIN_UNSUCCESSFUL))
                {
                    this.delegate.unsuccessful_login();
                }
                //this should be sent to main window.
                if(tokens.contains(Constants.NOTIFICATION_ID))
                {
                    //send the message in the tokens.
                    this.delegate.notifyServerStop(tokens.get(1));
                }
                //this should be sent to main view.
                if(tokens.contains(Constants.NEW_USER))
                {
                    this.delegate.newUserAdded(tokens.get(1));
                }
                //all online users has been received
                if(tokens.contains(Constants.ALL_USERS))
                {
                    
                    ArrayList<String> users=new ArrayList<String>();
                    users.addAll(tokens);
                    //the first one is the IDENTIFIER,remove it.
                    users.remove(Constants.ALL_USERS);
                    this.delegate.onlineUsers(users);
                }
                //user removed notification
                if(tokens.contains(Constants.USER_REMOVED))
                {
                    this.delegate.userRemoved(tokens.get(1));
                }
                //send the public message on screen
                if(tokens.contains(Constants.PUBLIC_ROOM))
                {
                    System.out.println("Parsed tokens:"+tokens.size());
                    //
                    if(tokens.get(1).contains("file"))
                    {
                        //there is smiley,but the parts were separated due to the presence of an internal : after word 'file'
                        this.delegate.message(tokens.get(1)+":"+tokens.get(2));
                    }
                    else
                    {
                        //the message is without smiley...
                        this.delegate.message(tokens.get(1));
                    }
                }
                //private message
                if(tokens.contains(Constants.MESSAGE_ID))
                {
                    //private message
                    //
                    if(tokens.get(2).contains("file"))
                    {
                        //there is smiley,but the parts were separated due to the presence of an internal : after word 'file'
                        this.delegate.message(tokens.get(1),tokens.get(2)+":"+tokens.get(3),tokens.get(4));
                    }
                    else
                    {
                        //the message is without smiley...
                        this.delegate.message(tokens.get(1), tokens.get(2),tokens.get(3));
                    }
                    
                }
                
                if(tokens.contains(Constants.RECEIVE))
                {
                    System.out.println("file size:"+tokens.get(2));
                    //
                    this.receiveFile(tokens.get(1), Integer.parseInt(tokens.get(2)),tokens.get(3));
                }
                
                 
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Writes the message in server's input stream,i.e.client's output stream.
     * @param message
     */
    public void write(String message)
    {
        this.writer.println(message);
        //
        System.out.println("Sending message to server from client:"+message);
        //
        this.writer.flush();
    }
    /**
     * Sends the public message
     * @param from
     * @param message
     */
    public void sendPublicMessage(String from,String message)
    {
        this.write(Message.getPublicMessage(message));
    }
    /**
     * Receives file
     */
    private void receiveFile(String fileName,int size,String to) throws Exception
    {
            //confirm file received
            this.delegate.fileInfo(Message.getFileReceivedConfirmationMessage(fileName, this.userHomeDir,to));
            //
            int filesize=size; // filesize temporary hardcoded


            int bytesRead;
            int current = 0;
            

            // receive file
            byte [] mybytearray  = new byte [filesize];
            InputStream is = clientSock.getInputStream();
            FileOutputStream fos = new FileOutputStream(this.userHomeDir+"/"+fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;

            
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
     * Sends private message
     * @param from
     * @param to
     * @param message
     */
    public void sendPrivateMessage(String from,String message,String to)
    {
        String text=Message.getPrivateMessage(from, message, to);
        //
        System.out.println("Private message:"+text);
        //
        this.write(text);
    }
    /**
     * Reads string from input stream.
     * @return
     * @throws IOException
     */
    private String read() throws IOException
    {
        return this.reader.readLine();
    }
    /**
     * Sends the file to user
     * @param file
     * @param to
     */
    public void sendFile(File file,String to,FileSendWizard wizard)
    {
        this.write(Message.getFileTransferMessage(file.getName(),to,(int)file.length()));
        byte [] mybytearray  = new byte [(int)file.length()];
        FileInputStream fis;
        try {
            
            //
            fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(mybytearray,0,mybytearray.length);
            OutputStream os = clientSock.getOutputStream();
            os.write(mybytearray,0,mybytearray.length);
            os.flush();
            wizard.finished();
        } catch (Exception ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void run() {
        this.waitForServerMessage();
    }

}
