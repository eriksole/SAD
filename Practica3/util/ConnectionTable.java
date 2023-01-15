package util;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionTable {
    Map<String, SocketChannel> clients_map;
    Lock l;

    public ConnectionTable() {
        clients_map = new HashMap<>();
        l = new ReentrantLock();
    }

    public String addParticipant(String nickname_original, SocketChannel sc) {
        String nickname = null;
        l.lock();
        try { 
            //Si el nom d'usuari ja existeix, hi afegeix 3 números ranodm al final
            if(clients_map.containsKey(nickname_original)){
                do {
                    nickname = nickname_original + "_"
                            + (int) (Math.random() * 10)
                            + (int) (Math.random() * 10)
                            + (int) (Math.random() * 10);
                } while (clients_map.containsKey(nickname));            
            }
            else{
                nickname = nickname_original;
            }  
            clients_map.put(nickname, sc);                      
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            l.unlock();
        }
        return nickname;
    }

    public void broadcast(String content, String nick) { //
        l.lock();
        try {
            for (String nickname : clients_map.keySet()) {
                if (!nickname.equals(nick)) {
                    SocketChannel sc = clients_map.get(nickname);
                    //sc.write(nick + ": " + content);
                    ByteBuffer out = ByteBuffer.wrap((content).getBytes());
                    sc.write(out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            l.unlock();
        }
    }

    public void removeParticipant(String meu_nickname) {
        l.lock();
        try {
            clients_map.remove(meu_nickname);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            l.unlock();
        }
    }

    public List getUsernames(){
        List<String> l = new ArrayList<String>(clients_map.keySet());
        return l;        
    }

    public SocketChannel getSocketChannel(String ref){
        return clients_map.get(ref);
    }
}
