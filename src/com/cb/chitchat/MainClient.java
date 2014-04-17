/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainClient.java
 *
 * Created on May 20, 2012, 11:45:14 PM
 */

package com.cb.chitchat;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.text.BadLocationException;

/**
 * Main client is the UI for client side.
 * @author mangoesmobile
 */
public class MainClient extends javax.swing.JFrame implements LoginDelegate,ConnectionManagerDelegate,MessagePanelDelegate,FileTransferDelegate{

    
    MainClient context;
    String colorCode;
    String ID;
    String password;
    ConnectionManager conManager;
    //the login window
    Login login;
    //chat panels
    ArrayList<MessagePanel> chatPanels=new ArrayList<MessagePanel>();
    //online users
    ArrayList<String> onliners=new ArrayList<String>();
    //
    DefaultListModel listModel;
    int tabCounter=0;
    
    
    /** Creates new form MainClient */
    public MainClient() {
        initComponents();
        context=this;
        colorCode="000000";
        ID="";
        password="";
        this.initWindowListener();
        //first panel,public one.
        this.chatPanels.add(new MessagePanel(this,Constants.PUBLIC_ROOM,0));
        //add the public pane in tabbed pane.the space after Public is intentional to keep the icon a bit far.
        this.tabbedPane.addTab("Public ", new ImageIcon("images/icons/user_group.png"), this.chatPanels.get(0));
        //set up online list
        this.initListFactors();
        
    }
    /**
     * Creates a chat pane upon request
     * @param from
     * @param msg
     */
    private void initChatPaneForUser(String from, String msg) {
        MessagePanel pane = this.chatPanelForUser(from);
        //pane is not created before,create one.
        if (pane == null) {
            this.privatePane(from);
            //get the newly created pane
            MessagePanel created = this.chatPanels.get(this.chatPanels.size() - 1);
            String formattedMsg = this.replaceEndTags(msg, created.getCurrentTextInMessagePane());
            created.setTextForMessagePane(formattedMsg);
        } else {
            //pane is available just send the update.
            String formattedMsg = this.replaceEndTags(msg, pane.getCurrentTextInMessagePane());
            pane.setTextForMessagePane(formattedMsg);
        }
    }
    private void privatePane(String with)
    {
        System.out.println("Size before adding any private pane:"+this.chatPanels.size());
        //create the pane
        MessagePanel pane=new MessagePanel(this,with,this.chatPanels.size());
        this.chatPanels.add(pane);
        //
        System.out.println("size after adding privat pane:"+this.chatPanels.size()+" pane serial:"+pane.serial);
        //
        this.tabbedPane.addTab(null, this.chatPanels.get(this.chatPanels.size()-1));
        //the close button
        TabPaneCloseButton closeButton=new TabPaneCloseButton();
        closeButton.setIcon(new ImageIcon("images/icons/close.png"));
        
        //close action handler
        MouseListener closeButtonClickedHandler;
        closeButtonClickedHandler=new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent evt)
            {
                TabPaneCloseButton btn = (TabPaneCloseButton) evt.getSource();
                String s1 = btn.getActionCommand();
                for (int i = 1; i < tabbedPane.getTabCount(); i++) {
                  JPanel pnl = (JPanel) tabbedPane.getTabComponentAt(i);
                  TabPaneCloseButton btn2=(TabPaneCloseButton)pnl.getComponent(1);
                  String s2 = btn2.getActionCommand();
                  if (s1.equals(s2)) {
                    tabbedPane.removeTabAt(i);
                    tabCounter--;
                    chatPanels.remove(i);
                    break;
                  }
                }

            }
        };
        //
        closeButton.addMouseListener(closeButtonClickedHandler);
        
        //
        if (tabCounter >= 0) {
            JPanel tabTitlePanel=new JPanel();
            
            tabTitlePanel.setOpaque(false);//make transperant
            tabTitlePanel.setLayout(new FlowLayout());
            JLabel title=new JLabel(with+"  ");
            title.setIcon(new ImageIcon("images/icons/user.png"));
            title.setHorizontalTextPosition(JLabel.TRAILING);
            title.setIconTextGap(4);
            tabTitlePanel.add(title);//adding space to give space between text and close button
            closeButton.setActionCommand(""+tabCounter);
            tabTitlePanel.add(closeButton);
            closeButton.setPreferredSize(new Dimension(16,16));
            tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabTitlePanel);
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            tabTitlePanel.setPreferredSize(new Dimension(title.getPreferredSize().width+closeButton.getPreferredSize().width+10,20));
        }

        tabCounter++;
    }
    /**
     * Online list box related special factors initialize.
     */
    private void initListFactors()
    {
        //init the listModel
        this.listModel=new DefaultListModel();
        this.onlineList.setModel(listModel);
        //Create a custom list cell renderer and set it to list box
        this.onlineList.setCellRenderer(new OnlineUserCellRenderer());
        //double click handler
        MouseListener mouseListener = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                     if (e.getClickCount() == 2) {
                         int index = onlineList.locationToIndex(e.getPoint());
                         privatePane((String)listModel.getElementAt(index));
                      }
             }
         };
        onlineList.addMouseListener(mouseListener);
    }
   
    /**
     * Confirms exit.
     */
    private void initWindowListener() {
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent event) {
                int ret = JOptionPane.showConfirmDialog(context, "Are you sure?", "Confirm exit", JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    //log out from server.
                    logout();
                    context.dispose();
                    System.exit(0);
                }
            }
        });
    }
    
    /**
     * Returns hex value of a color
     * @param c
     * @return
     */
    public static String toHexString(Color c) {
        StringBuilder sb = new StringBuilder('#');

          if (c.getRed() < 16) sb.append('0');
          sb.append(Integer.toHexString(c.getRed()));

          if (c.getGreen() < 16) sb.append('0');
          sb.append(Integer.toHexString(c.getGreen()));

          if (c.getBlue() < 16) sb.append('0');
          sb.append(Integer.toHexString(c.getBlue()));

          return sb.toString();
    }
    /**
     * Replace any line ends in specified text.
     * @param text
     * @return
     */
    private String replaceNewLines(String text)
    {
        String newText=text.replaceAll("(\\r|\\n| |\\t)", "");
        return newText;
    }
    
    /**
     * Generates HTML Message format for chat area.
     * @param message
     * @return
     */
    private String generateHTMLMessage(String message)
    {
        
        String replacer="<p><font color="+this.colorCode+"><b>"+this.ID+"</b> - "+message+"</font></p>"+"</body></html>";
        return replacer;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        onlineList = new javax.swing.JList();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        exitMenu = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        textColorMenu = new javax.swing.JMenuItem();
        sendFileMenu = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(573, 386));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                MainClient.this.windowOpened(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon("/dibosh's folder/Works/ChitChat/dist/images/icons/online_icon.png")); // NOI18N
        jLabel1.setText("Online");

        onlineList.setToolTipText("double click on any user to chat in private...");
        jScrollPane1.setViewportView(onlineList);

        jMenu1.setText("File");

        exitMenu.setText("Exit");
        exitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenu);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Actions");

        textColorMenu.setIcon(new javax.swing.ImageIcon("/dibosh's folder/Works/ChitChat/dist/images/icons/edit_user.png")); // NOI18N
        textColorMenu.setText("Choose text color...");
        textColorMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textColorMenuActionPerformed(evt);
            }
        });
        jMenu2.add(textColorMenu);

        sendFileMenu.setIcon(new javax.swing.ImageIcon("/dibosh's folder/Works/ChitChat/dist/images/icons/send.png")); // NOI18N
        sendFileMenu.setText("Send file...");
        sendFileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendFileMenuActionPerformed(evt);
            }
        });
        jMenu2.add(sendFileMenu);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(11, 11, 11)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 123, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addContainerGap())
            .add(tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Creates the text for message panel's chat pane.
     * @param text Text in message panel's message field.
     * @return
     * @throws BadLocationException
     */
    private String generateTextForMessagePane(String text) throws BadLocationException {
        // TODO add your handling code here:
        //handle smiley codes
        String smileyAddedMessage=EmoticonFilter.applyFilter(text);
        //returned message
        String msg="";
        
        if (!smileyAddedMessage.isEmpty()) {
            //the text that will replace 
            msg=this.generateHTMLMessage(smileyAddedMessage);
        }
        return msg;
    }
    /**
     * Takes the present text in message pane.
     * @param newText
     * @param present
     * @return
     */
    private String replaceEndTags(String newText,String present)
    {
        String msg="";
        //first occurance of body closing tag
        int replacementIndex=present.indexOf("</body>");
        //replace the first occurance of body closing tag with spedified html message.
        msg=present.replace(present.subSequence(replacementIndex, present.length()),newText);
        return msg;
    }
    /**
     * Called once this window is opened.
     * @param evt
     */
    private void windowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowOpened
        // TODO add your handling code here:
        //show a login window as soon as it gets opened
        login=new Login(this,true,this);
        login.setLocationRelativeTo(this);
        login.setVisible(true);
    }//GEN-LAST:event_windowOpened

    /**
     * Sends the log out request to server
     */
    public void logout()
    {
        this.conManager.write(Constants.LOGOUT_ID+":");
    }

    /**
     * Creates an alert with custom options.
     * @param options
     * @param message
     * @param title
     * @param type
     */
    private void showAlertWithOptions(Object[] options,String message,String title,int type)
    {
        JOptionPane optionPane = new JOptionPane();
        optionPane.setName(title);
        optionPane.setMessage(message);
        optionPane.setMessageType(JOptionPane.WARNING_MESSAGE);
        optionPane.setOptions(options);
        JDialog dialog = optionPane.createDialog(null, "Icon/Text Button");
        dialog.setVisible(true);
    }
    // <editor-fold defaultstate="collapsed" desc="Event Handling">
    private void textColorMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textColorMenuActionPerformed
        // TODO add your handling code here:
        Color myColor=JColorChooser.showDialog(this, "Choose text color...", Color.yellow);
        this.colorCode=this.toHexString(myColor);
        
    }//GEN-LAST:event_textColorMenuActionPerformed

    private void exitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuActionPerformed
        // TODO add your handling code here:
        WindowEvent closingEvent = new WindowEvent(context, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);
    }//GEN-LAST:event_exitMenuActionPerformed

    private void sendFileMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendFileMenuActionPerformed
        // TODO add your handling code here:
        FileSendWizard sendFileFrame=new FileSendWizard(this,this.onliners);
        sendFileFrame.setLocationRelativeTo(this);
        sendFileFrame.setVisible(true);
    }//GEN-LAST:event_sendFileMenuActionPerformed

    //</editor-fold>
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem exitMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList onlineList;
    private javax.swing.JMenuItem sendFileMenu;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JMenuItem textColorMenu;
    // End of variables declaration//GEN-END:variables

    /**
     * Returns the opened panel serial of this user or 0 if not found.
     * @param user
     * @return
     */
    public int getPanelOfTheUser(String user)
    {
        int panelSerial=0;
        for(MessagePanel panel : this.chatPanels)
        {
            if(panel.ID.equals(user))
            {
                panelSerial=panel.serial;
                break;
            }
        }
        return panelSerial;
    }
    //<editor-fold desc="Login Delegate methods">
    /**
     * Login method;sent from login view
     * @param ID
     * @param password
     */
    public void login(String ID, String password,String port) {
        //get the port
        int portNum=Integer.parseInt(port);
        //save the credentials
        this.ID=ID;
        this.password=password;
        //set up the connection manager with this port and login window as delegate.
        this.conManager=new ConnectionManager(portNum,this.login);
        //start the con manager in new thread.
        Thread myThread=new Thread(this.conManager);
        myThread.start();
        //send login request to server
        this.conManager.write(Message.getLoginMessage(ID, password));
    }
    /**
     * The pane associated with this user
     * @param userID
     * @return
     */
    private MessagePanel chatPanelForUser(String userID)
    {
        MessagePanel pane=null;
        for(MessagePanel m : this.chatPanels)
        {
            if(m.ID.equals(userID))
            {
                pane=m;
                break;
            }
        }
        return pane;
    }
    
    /**
     * Login window ensures the login is valid.
     */
    public void successful() {
        //no more need of login window, delegation is handled to this window.
        this.conManager.setDelegate(this);
        this.setTitle(ID);
    }
    //</editor-fold>

    //<editor-fold desc="Con Manager Delegate Methods">
    public void successful_login() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void unsuccessful_login() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    //public room message
    public void message(String msg) {
        MessagePanel publicPane=this.chatPanels.get(0);
        String formattedMsg=this.replaceEndTags(msg,publicPane.getCurrentTextInMessagePane());
        //
        System.out.println("Inside main client,generated html:"+formattedMsg);
        //
        publicPane.setTextForMessagePane(formattedMsg);
    }
    //private message
    public void message(String from,String msg,String to) {
        if(!from.equals(this.ID))
        {
            //the first id is actually for which user.
            initChatPaneForUser(from, msg);
        }
        else
        {
            //as the server message to both sender and receiver,if we find that same message was sent to sender too,we need not
            //create pane for sender[i.e.me/this client],rather pass this to the receiver
            initChatPaneForUser(to,msg);
        }
        
    }
        


    public void fileBytes(byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void fileInfo(String info) {
        JOptionPane.showMessageDialog(this, info, "File received...", JOptionPane.INFORMATION_MESSAGE);
    }

    public void notifyServerStop(String msg) {
        JButton button=new JButton("Ok");
        button.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }


        })   ;
        this.showAlertWithOptions(new Object[]{button}, "Server is stopped unexpectedly.ChitChat needs to close.Sorry for the inconvenience.", msg, JOptionPane.WARNING_MESSAGE);
    }

    public void newUserAdded(String userID) {

        this.onliners.add(userID);
        this.listModel.addElement(userID);
        //
        System.out.println(userID+" is online.online list size:"+this.onliners.size());
        
    }
    /**
     * All online users received
     * @param users
     */
    public void onlineUsers(ArrayList<String> users) {
        for(String user : users)
        {
            this.onliners.add(user);
            this.listModel.addElement(user);
        }
    }
    /**
     * Existing user notification from server
     * @param ID
     */
    public void existing_user(String ID) {
        //we dont need it here.
    }
    /**
     * User is removed.Notify it.
     * @param ID
     */
    public void userRemoved(String ID) {

        int indexInList=this.onliners.indexOf(ID);
        //
        System.out.println("User at "+indexInList+" from onliners list was removed.");
        //
        this.listModel.remove(indexInList);
        this.onliners.remove(ID);
        //
        int n=this.getPanelOfTheUser(ID);
        //
        System.out.println("ChatPane with serial "+n+" for user"+ID +" is removed");
        if(n!=0)
        {
            this.chatPanels.remove(n);
            this.tabbedPane.removeTabAt(n);
        }
        
        
    }


    //</editor-fold>
    //file request from file file send wizard.
    public void file(File file, String to,FileSendWizard wizard) {
        this.conManager.sendFile(file, to, wizard);
        
    }


    //<editor-fold desc="MessagePanel Delegate Methods">
    

    
    public void textFromMessageField(String text, String toID, int n) {
         try {
            
            //get the associated message panel.
            MessagePanel panel=this.chatPanels.get(n);
            //create the new text
            String newText = this.generateTextForMessagePane(text);
            //
            System.out.println("ChatPanel Serial:"+n);
            //public room
            if(n==0)
            {
                this.conManager.sendPublicMessage(this.ID, newText);
            }
            else
            {
                this.conManager.sendPrivateMessage(this.ID, newText, toID);
            }
        } catch (BadLocationException ex) {
            Logger.getLogger(MainClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    

    
    

    
    //</editor-fold>
    //<editor-fold desc="ListView custom cell renderer">
    class OnlineUserCellRenderer extends JLabel implements ListCellRenderer {
        final  ImageIcon userIcon = new ImageIcon("images/icons/user.png");

         // This is the only method defined by ListCellRenderer.
         // We just reconfigure the JLabel each time it's called.

        public Component getListCellRendererComponent(
           JList list,
           Object value,            // value to display
           int index,               // cell index
           boolean isSelected,      // is the cell selected
           boolean cellHasFocus)    // the list and the cell have the focus
           {
             String s = value.toString();
             setText(s);
             setIcon(userIcon);
               if (isSelected) {
                 setBackground(list.getSelectionBackground());
                   setForeground(list.getSelectionForeground());
               }
             else {
                   setBackground(list.getBackground());
                   setForeground(list.getForeground());
               }
               setEnabled(list.isEnabled());
               setFont(list.getFont());
             setOpaque(true);
             return this;
         }
    }

    //</editor-fold>

}
