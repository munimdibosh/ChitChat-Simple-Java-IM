import java.io.*;
import javax.swing.*;
import java.net.*;
import java.awt.*;
import java.lang.String;

import java.awt.event.*;

public class chatClient
{
	JTextField outgoing;
	PrintWriter writer;
	JButton send;
	Socket sock;
	JFrame welcomeScreen;
	public String id;
	JTextField number;
	public void welcome()
	{
		welcomeScreen=new JFrame("WELCOME!");
		welcomeScreen.setSize(400,200);
		welcomeScreen.setLayout(new FlowLayout());
		welcomeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		welcomeScreen.setVisible(true);
		JLabel proceed=new JLabel("Enter a number 1-10 as your client ID");
		welcomeScreen.add(proceed);
		number=new JTextField(2);
		welcomeScreen.add(number);
		JButton enter=new JButton("Enter");
		JLabel create=new JLabel("created by:0805033");
		
		enterButtonListener enterHandler=new enterButtonListener();
		enter.addActionListener(enterHandler);
		welcomeScreen.add(enter);
		welcomeScreen.add(create);
		
		
		
	
	}
	public void go(String ID)
	{
		id=ID;
		JFrame client=new JFrame("Client"+id);
		client.setSize(400,200);
		client.setLayout(new FlowLayout());
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.setVisible(true);
		
		JLabel clientId=new JLabel("Client"+" "+id);
		client.add(clientId);
		outgoing=new JTextField(30);
		client.add(outgoing);
		send=new JButton("SEND");
	
		
		client.add(send);
		
		
		
		sendButtonListener sendHandler=new sendButtonListener();
		send.addActionListener(sendHandler);
		setNetwork();
	}
	private void setNetwork()
	{
			try{
			
				sock=new Socket("localhost",12111);
				writer=new PrintWriter(sock.getOutputStream());
				
			}
			catch(IOException ex)
			{
				
			}
		
	}
	public class enterButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			
			go(number.getText());
			welcomeScreen.setVisible(false);
			
			
		}
	}
	public class sendButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			System.out.println(id+" "+outgoing.getText());
			try
			{
				
				writer.println(id+outgoing.getText());
			
				writer.flush();
			}
			catch (Exception ex)
			{
				
			}
			outgoing.setText("");
			outgoing.requestFocus();
				
		}
		
	}
	public static void main (String[] args)
			 {
		

			 	
			 	chatClient client=new chatClient();
			 	client.welcome();
			
			 	
			 	
			}
	
}