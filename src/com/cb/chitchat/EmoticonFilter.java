/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.chitchat;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

/**
 *
 * @author mangoesmobile
 */
public class EmoticonFilter {
    private static final Map EMOTICON_MAP = new HashMap();
    private static final String url="file:";
    private EmoticonFilter() {
        // Not instantiable.
    }

    /**
     * Applys the emoticon filter to a string. For example, if you wanted the
     * actual graphic for :) :<p>
     * <pre>
     * String graphic = EmoticonFilter.applyFilter(":)");
     * </pre>
     * </p>
     *
     * You would receive Resources/1.gif.
     * @param string the string to parse for emoticon images.
     * @return the emoticon image link.
     */
    public static String applyFilter(String string) {
        

        final StringBuffer buf = new StringBuffer();
        final StringTokenizer tkn = new StringTokenizer(string, " ", false);
        while (tkn.hasMoreTokens()) {
            String str = tkn.nextToken();
            String found = (String) EMOTICON_MAP.get(str);
            if (found != null) {
                str = buildURL(found);
            }
            buf.append(str + " ");
        }
        return buf.toString();
    }
    /*
    public String replaceSmileys(String text){
        for(Entry<String, String> smißley :EMOTICON_MAP.entrySet())
            text = text.replaceAll(smiley.getKey(), smiley.getValue());
        return text;
    }

    /**
     * Build image tags
     */
    static
    {
        EMOTICON_MAP.put(":)", url+"images/emoticons/happy.gif");
        EMOTICON_MAP.put(":-)", url+"images/emoticons/happy.gif");
        EMOTICON_MAP.put(":(", url+"images/emoticons/sad.gif");
        EMOTICON_MAP.put(":-(", url+"images/emoticons/sad.gif");
        EMOTICON_MAP.put(":D", url+"images/emoticons/grin.gif");
        EMOTICON_MAP.put(";\\", url+"images/emoticons/mischief.gif");
        EMOTICON_MAP.put("B)", url+"images/emoticons/cool.gif");
        EMOTICON_MAP.put("]:)", url+"images/emoticons/devil.gif");
        EMOTICON_MAP.put(":p", url+"images/emoticons/silly.gif");
        EMOTICON_MAP.put("X(", url+"images/emoticons/angry.gif");
        EMOTICON_MAP.put(":^0", url+"images/emoticons/laugh.gif");
        EMOTICON_MAP.put(";)", url+"images/emoticons/wink.gif");
        EMOTICON_MAP.put(";-)", url+"images/emoticons/wink.gif");
        EMOTICON_MAP.put(":8}", url+"images/emoticons/blush.gif");
        EMOTICON_MAP.put(":'(", url+"images/emoticons/cry.gif");
        EMOTICON_MAP.put(":?", url+"images/emoticons/confused.gif");
        EMOTICON_MAP.put(":0", url+"images/emoticons/shocked.gif");
        EMOTICON_MAP.put(":|", url+"images/emoticons/plain.gif");
        EMOTICON_MAP.put(":P", url+"images/emoticons/tongue.gif");
        EMOTICON_MAP.put(":p", url+"images/emoticons/tongue.gif");
    }
    /*
    static {
        EMOTICON_MAP.put(":)", url+"/1.gif");
        EMOTICON_MAP.put(":-)", url+"/1.gif");
        EMOTICON_MAP.put(":(", url+"/2.gif");
        EMOTICON_MAP.put(":-(", url+"/2.gif");
        EMOTICON_MAP.put(":D", url+"/3.gif");
        EMOTICON_MAP.put(":p", url+"/4.gif");
        EMOTICON_MAP.put(":P", url+"/4.gif");
        EMOTICON_MAP.put(";)", url+"/5.gif");
        EMOTICON_MAP.put(";-)", url+"/5.gif");
        EMOTICON_MAP.put(":'(", url+"/6.gif");
    }
    */

    /**
     * Returns an HTML image tag using the base image URL and image name.
     * @param imageName the relative url of the image to build.
     * @return the new img tag to use.
     */
    private static String buildURL(String imageName) {
        return "<img border=\"0\" src=\"" + imageName + "\">";
    }
}