
import java.io.BufferedReader;
import java.io.InputStreamReader;

import util.*;
public class Clientxat {
    public static String username;
    public static void main(String[] args){        
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Escriu el teu nom: ");

        try{
            username = input.readLine();
            System.out.println("Benvingut " + username);
        }catch(Exception e){
            System.out.println("Error llegint el teu usuari");
        }

        MySocket sc = new MySocket(username);        
        
        Thread socketThread = new Thread(() -> {
            try{
                String line;
                while ((line = input.readLine()) != null){
                    sc.write(line);                                        
                }
            }catch (Exception e){
                System.out.println("Error llegint el teu missatge");
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
