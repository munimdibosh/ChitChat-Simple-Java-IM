/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.chitchat.server;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A common constants list in server and client.
 * @author mangoesmobile
 */
public class Constants {
   public final static String LOGIN_ID="login";
   public final static String LOGOUT_ID="logout";
   public final static String LOGIN_SUCCESSFUL="sucessful";
   public final static String LOGIN_UNSUCCESSFUL="unsuccessful";
   public final static String EXISTING_USER="existing";
   public final static String MESSAGE_ID="message";
   public final static String NOTIFICATION_ID="notification";
   public final static String NEW_USER="new_user";
   public final static String ALL_USERS="all_users";
   public final static String USER_REMOVED="user_removed";
   //
   public final static String PUBLIC_ROOM="public";
   public final static String FILE="file";
   public final static String RECEIVE="receive";

   /**
     * Creates tokens from input text based on delemeter.
     * @param input
     * @param delem
     * @return
     */
    public static ArrayList<String> tokens(String input,String delem)
    {
        ArrayList<String> retVal=new ArrayList<String>();
        StringTokenizer tokens=new StringTokenizer(input,delem);
        while(tokens.hasMoreTokens())
        {
            retVal.add(tokens.nextToken());
        }
        
        return retVal;
    }

}
