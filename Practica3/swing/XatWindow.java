package swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class XatWindow extends JPanel {
    private JTextArea messageTA; 
    private JButton sendButton, outButton, em1Button, em2Button;
    private JTextArea xatTA;
    private JList<String> userList;
    private DefaultListModel userListModel;
    private GridBagConstraints gbConstraints;        

    public XatWindow(String connectionString){
        super(new GridBagLayout());

        xatTA = new JTextArea(connectionString);
        xatTA.setEditable(false);
        sendButton = new JButton("Enviar");
        outButton = new JButton("Exit");
        em1Button= new JButton("\ud83e\udd13");
        em2Button= new JButton("\ud83d\ude15");
        messageTA = new JTextArea();
        gbConstraints= new GridBagConstraints();

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);

        //out button
        gbConstraints.gridx = 2;
        gbConstraints.gridy = 0;
        gbConstraints.gridwidth=1;
        gbConstraints.gridheight=1;
        add(outButton, gbConstraints);

        //Chat Text
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 1;   
        gbConstraints.gridwidth=2;
        gbConstraints.gridheight=2; 
        gbConstraints.weighty=1.0;    
        gbConstraints.weightx=1.0; 
        gbConstraints.fill = GridBagConstraints.BOTH; 
        JScrollPane scrollPane = new JScrollPane(xatTA);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, gbConstraints);
        gbConstraints.weighty=0.0;  
        gbConstraints.weightx=0.0;  

        //user List
        gbConstraints.gridx = 2;
        gbConstraints.gridy = 1;
        gbConstraints.gridwidth=1;
        gbConstraints.gridheight=2; 
        gbConstraints.weighty=1.0;               
        add(new JScrollPane(userList), gbConstraints);
        gbConstraints.weighty=0.0;               

        //user text to send
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 3;
        gbConstraints.gridwidth=2;
        gbConstraints.gridheight=1;
        gbConstraints.weightx=1.0;        
        JScrollPane scrollPane2 = new JScrollPane(messageTA);
        scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane2, gbConstraints);
        gbConstraints.weightx=0.0;

        //send button
        gbConstraints.gridx = 2;
        gbConstraints.gridy = 3;
        gbConstraints.gridwidth=1;
        gbConstraints.gridheight=1;
        add(sendButton, gbConstraints);

        //em1 button
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 4;
        gbConstraints.gridwidth=1;
        gbConstraints.gridheight=1;
        add(em1Button, gbConstraints);

        //em2 button
        gbConstraints.gridx = 1;
        gbConstraints.gridy = 4;
        gbConstraints.gridwidth=1;
        gbConstraints.gridheight=1;
        add(em2Button, gbConstraints);
    }

    public JTextArea getMessageField(){
        return messageTA;
    }

    public JTextArea getXatText(){
        return xatTA;
    }

    public DefaultListModel getUserLList(){
        return userListModel;
    }

    public JButton getSendButton(){
        return sendButton;
    }

    public JButton getOutButton(){
        return outButton;
    }
    public JButton getem1Button(){
        return em1Button;
    }
    public JButton getem2Button(){
        return em2Button;
    }
    
}
