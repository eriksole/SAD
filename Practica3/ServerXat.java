import util.ConnectionTable;
import util.MyServerSocket;
import util.MySocket;
import java.io.IOException;

public class ServerXat {
      

    public static void main(String[] args) {
        try {
            Reactor reactor  = new Reactor();
            new Thread(reactor).start();
            System.out.println("Esperant connexions...");
        } catch (IOException e) {
            System.out.println("Hi ha hagut un error");
        }
    }
}

