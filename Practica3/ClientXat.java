
import java.io.BufferedReader;
import java.io.InputStreamReader;

import util.*;
import swing.*;
import java.awt.event.*;

public class ClientXat {
    public static String username;
    private static Window window;
    private static MySocket sc;

    public static void main(String[] args) {
        window = new Window();

        window.getLoginWindow().getUsername().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                window.getLoginWindow().getLoginButton().doClick();
            }
        });

        window.getLoginWindow().getLoginButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionevent) {
                username = window.getLoginWindow().getUsername().getText();
                if (username.isBlank()) {// considerem que un nom en blanc no és valid
                    System.out.println("[ERROR] Escriu un un d'usuari");
                } else {
                    connectToServer();
                }
            }
        });
    }

    public static void connectToServer() {
        sc = new MySocket(username);

        Thread serverThread = new Thread(() -> {
            String text;
            while ((text = sc.read()) != null) {
                
                if (text.contains("[WELCOME CLIENT]")) {
                    setupXat(text);                   
                } else if (text.contains(" has joined!")) { //update                    
                    writeInXat(text);                    
                    String[] aux = text.split(" ");
                    window.getXatWindow().getUserLList().addElement(aux[0]);
                } else if (text.contains(" has left!")) {                    
                    writeInXat(text);
                    String[] aux = text.split(" ");
                    window.getXatWindow().getUserLList().removeElement(aux[0]);
                } else {
                    writeInXat(text);
                }
            }
        });
        serverThread.start();
    }

    public static void writeInXat(String text) {
        window.getXatWindow().getXatText().append("\n" + text);
    }

    public static void setupXat(String setup) {
        window.ONxat(username);
        String str = setup.substring(setup.indexOf("]") + 2);
        setup = sc.read();
        str = setup.substring(setup.indexOf("]") + 1);        
        String aux = str.substring(2, str.length() - 1);                
        String[] aux1 = aux.split(",");       
        int i = 0;
        if(!aux1[i].equals(username)){
            window.getXatWindow().getUserLList().addElement(aux1[i]);                
        }  
        for (i = 1; i < aux1.length; i++) {            
            if(!aux1[i].substring(1).equals(username)){
                window.getXatWindow().getUserLList().addElement(aux1[i].substring(1));                
            }            
            
        }
        window.revalidate();
        window.repaint();

        window.getXatWindow().getSendButton().addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String message = window.getXatWindow().getMessageField().getText();
                if (message.isEmpty()) {
                    System.out.println("Message fiel is empty!");
                } else {
                    sendMessage(message);
                    window.getXatWindow().getMessageField().setText("");
                }
            }
        }));

        window.getXatWindow().getOutButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                disconnect();
            }
        });

        window.getXatWindow().getem1Button().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                sendEm1();
            }
        });
        window.getXatWindow().getem2Button().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                sendEm2();
            }
        });
    }

    public static void sendMessage(String message) {
        sc.write(message);
        writeInXat(message);
    }

    public static void disconnect() {
        sc.write(username + " has left");
        sc.close();
        window.dispose();
        System.exit(0);
    }
    
    public static void sendEm1(){
        sc.write("\ud83e\udd13");
        writeInXat("\ud83e\udd13");
    }

    public static void sendEm2(){
        sc.write("\ud83d\ude15");
        writeInXat("\ud83d\ude15");
    }
}
