import util.ConnectionTable;
import util.MyServerSocket;
import util.MySocket;

public class Serverxat {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    
    public static void main(String[] args){
        MyServerSocket ss = new MyServerSocket();
        ConnectionTable dicc = new ConnectionTable();
        System.out.println("Esperant clients...");

        while(true){
            MySocket sc = ss.accept();

            Thread handlerThread = new Thread(() -> {
                String my_nickname=sc.read();                
                String my_nickname_def = dicc.addParticipant(my_nickname, sc);
                System.out.println(ANSI_GREEN + my_nickname_def + " -> ha entrat" + ANSI_GREEN);
                String content;
                while((content=sc.read())!=null){
                    dicc.broadcast(content, my_nickname_def);              
                }
                System.out.println(ANSI_RED + my_nickname_def + " -> ha marxat" + ANSI_RESET);
                dicc.removeParticipant(my_nickname_def);
                sc.close();
            });
            handlerThread.start();

        }
    }
}
