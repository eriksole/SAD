
import java.io.BufferedReader;
import java.io.InputStreamReader;

import util.*;
public class Clientxat {
    public static String username;
    public static void main(String[] args){        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Write your username: ");

        try{
            username = in.readLine();
            System.out.println("Welcome to the chat " + username);
        }catch(Exception e){
            System.out.println("Error reading your username");
        }

        MySocket sc = new MySocket(username);        
        
        Thread socketThread = new Thread(() -> {
            try{
                String line;
                while ((line = in.readLine()) != null){
                    sc.write(line);                                        
                }
            }catch (Exception e){
                System.out.println("Error reading your message");
            }
        });
        socketThread.start();

        Thread keyboardThread =new Thread(() -> {
            String line;
            while((line = sc.read())!= null){ 
                System.out.println(line);
            }
        });  
        keyboardThread.start();
    }
}
