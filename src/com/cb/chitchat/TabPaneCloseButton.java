/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.chitchat;

import javax.swing.JLabel;

/**
 *
 * @author mangoesmobile
 */
public class TabPaneCloseButton extends JLabel{
    private String actionCommand;
    public TabPaneCloseButton()
    {
        super();
    }
    /**
     * Sets the action command for this component.
     * @param text
     */
    public void setActionCommand(String text)
    {
        this.actionCommand=text;
    }
    /**
     * Gets the action command
     * @return
     */
    public String getActionCommand()
    {
        return this.actionCommand;
    }


}
