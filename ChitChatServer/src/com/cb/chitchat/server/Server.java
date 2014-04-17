/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.chitchat.server;

/**
 *
 * @author mangoesmobile
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
public class Server implements ClientAdditionDelegate,Runnable
{
    int port;
    boolean serverUp;
    //the server socket
    ServerSocket serverSock;
    //contains the online users
    ArrayList<ClientHandler> clients;
    ServerDelegate delegate;
    /**
     * Creates a server with specified port.
     * @param prt
     * @throws IOException
     */
    public Server(int prt,ServerDelegate del) throws IOException
    {
        port=prt;
        serverSock=new ServerSocket(port);
        //server is down still now.
        serverUp=false;
        this.clients=new ArrayList<ClientHandler>();
        this.delegate=del;

    }
    public void run() {
        this.start();
        //stop the server
        if(this.serverUp==false)
        {
            try {
                this.serverSock.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * If the server is closed.
     * @return
     */
    public boolean isClosed()
    {
        return this.serverSock.isClosed();
    }
    /**
     * Runs the server and waits for clients.
     * @throws IOException
     */
    private void up() throws IOException
    {
        //endless loop until server is down
        while(this.serverUp)
        {
            Socket clientSocket=serverSock.accept();
            
            //each client creates one following thread
            if(clientSocket!=null && !clientSocket.isClosed())
            {
                ClientHandler temp=new ClientHandler(clientSocket,this);
                Thread client=new Thread(temp);
                client.start();
            }
        }
    }
    /**
     * Current time stamp
     * @return
     */
    private String timestamp()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    /**
     * Checks the validity of this user.
     * @param userID
     * @param password
     * @return
     * @throws Exception
     */
    public boolean checkForUser(String userID,String password) throws Exception
    {
        boolean retVal=false;
        FileReader fr=new FileReader("user");
        BufferedReader br=new BufferedReader(fr);
        String text="";
       
        while((text=br.readLine())!=null)
        {
            ArrayList<String> temp= new ArrayList<String>();
            //parse text from file into user id and password.
            temp.addAll(Constants.tokens(text, ":"));
            String ID=temp.get(0);
            String PW=temp.get(1);
            //check if the parsed data matches requested credentials.
            if(ID.equalsIgnoreCase(userID) && PW.equalsIgnoreCase(password))
            {
                
                retVal=true;
                break;
            }
        }
        return retVal;
    }
    /**
     * Notifies all clients the same message
     * @param msg
     */
    private void notifyAllClients(String msg)
    {
        for(ClientHandler client : this.clients)
        {
            client.notifyWithMessage(msg);
        }
    }
    /**
     * Start the server
     */
    public void start()
    {
        this.serverUp=true;
        try {
            this.delegate.updateStatus(this.timestamp()+"-"+"Server started.Waiting for clients...");
            this.up();
            
            
        } catch (Exception ex) {
            this.delegate.updateStatus(this.timestamp()+"-"+"Server hang out unexpectedly...");
            ex.printStackTrace();
            
        }
        
    }
    /**
     * Called when stop event is fired on this server.
     * This event can be called intentionally or unintentionally like
     * when user fires Stop button
     * or exits the server(unintentionally).
     */
    public void stopEventFired()
    {
        this.serverUp=false;
        this.notifyAllClients(Message.getNotificationMessage("Server is stopped..."));
    }
    /**
     * Stops the running server
     * @throws IOException
     */
    public void stop() throws IOException
    {
        this.stopEventFired();
        this.delegate.updateStatus(this.timestamp()+"-"+"Server is stopped...");
    }
    /**
     * Creates a list of user names except the given one
     * @param ID
     * @return
     */
    private ArrayList<String> allUserNamesExcept(String ID)
    {
        ArrayList<String> users=new ArrayList<String>();
        for(ClientHandler client : this.clients)
        {
            if(!client.userID.equals(ID))
                users.add(client.userID);
        }
        return users;
    }
    /**
     * Checks if this client is already added.
     * @param client
     * @return
     */
    boolean isExistingClient(ClientHandler client)
    {
        boolean retVal=false;
        for(ClientHandler c : this.clients)
        {
            if(c.userID.equals(client.userID))
            {
               retVal=true;
               break;
            }
        }
        return retVal;
    }
    /**
     * Returns the client object of this name.
     * @param username
     * @return
     */
    private ClientHandler clientWithName(String username)
    {
        ClientHandler client = null;
        //
        System.out.println("Client Array Size:"+this.clients.size());
        //
        for(ClientHandler c : this.clients)
        {
            if(c.userID.equals(username))
            {

                client = c;
                System.out.println("Found:"+c.userID);
                break;
                
            }
        }
        return client;
    }
    /**
     * Notify each online user about the new user.
     * @param userID
     */
    public void notifyNewUserAddedToAll(String ID)
    {
        for(ClientHandler client : this.clients)
        {
            //notify all except this newly added user.
            if(!client.userID.equals(ID))
                client.newUserAdded(ID);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Client Addition Delegate Methods">

    public void sendMessageFromThisTo(String fromID, String message, String toID) {
        String msg=Message.getPrivateMessage(fromID, message, toID);
        //Server message received
        System.out.println("Private message in server:"+msg);
        //find the destination client.
        ClientHandler toClient=this.clientWithName(toID);
        ClientHandler fromClient=this.clientWithName(fromID);
        if(toClient!=null && fromClient!=null)
        {
            //
            System.out.println("Found toclient<reading user ID from client object>: "+toClient.userID+" from client: "+fromClient.userID);
            //notify both with the same message
            toClient.notifyWithMessage(msg);
            fromClient.notifyWithMessage(msg);
        }

    }
    /**
     * Sends public message to each and every one.
     * 
     * @param message
     */
    public void sendMessage(String message) {
        for(ClientHandler client : this.clients)
        {
            
            String text=Message.getPublicMessage(message);
            //
            System.out.println("Inside Server Send Message:"+text);
            //
            client.notifyWithMessage(text);
        }
    }

    public void validate(ClientHandler client) {
        try {
            this.delegate.updateStatus(this.timestamp()+"-"+"Server receieved auth request from client: "+client.userID+"\n");
            //
            if(this.checkForUser(client.userID, client.password))
            {
                
                //if not existing client add it.
                if(!this.isExistingClient(client))
                {
                    //notify the client
                    client.notifyWithMessage(Constants.LOGIN_SUCCESSFUL+":");
                    //
                    this.clients.add(client);
                    //
                    this.delegate.updateStatus(this.timestamp()+"-"+"Total clients "+this.clients.size()+", "+client.userID+""
                            + " has logged in...\n");
                    //this client has become online,it should get all other online user list.
                    client.sendAllOnlineUsers(this.allUserNamesExcept(client.userID));
                    //notify all users about this new user.
                    this.notifyNewUserAddedToAll(client.userID);
                }
                else{
                    //notify this user is already existing.
                    client.notifyExistingUser(client.userID);

                }
                
                
            }
            else
            {
                //notify the client
                client.notifyWithMessage(Constants.LOGIN_UNSUCCESSFUL+":");
                this.delegate.updateStatus(this.timestamp()+"-Invalid client request blocked from "+client.userID+"\n");
            }
            
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /**
     * Notifies when an user gets offline.
     * @param ID
     */
    private void notifyUserRemoval(String ID)
    {
        for(ClientHandler client : this.clients)
        {
            if(!client.userID.equals(ID))
            {
                client.userRemoved(ID);
            }
        }
    }
    public void sendBytesFromThisTo(String fromID, byte[] buffer, String toID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void logout(ClientHandler client) {
        this.clients.remove(client);
        //notify all existing users
        this.notifyUserRemoval(client.userID);
        this.delegate.updateStatus(this.timestamp()+"-"+client.userID+" has logged out...");
    }

    public void sendFileTo(String fileName, int size, String to) {
        ClientHandler client=this.clientWithName(to);
        client.notifyWithMessage(Message.getFileReceiveMessage(fileName,size,to));

    }

    
    //</editor-fold>
}