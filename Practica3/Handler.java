import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.channels.Selector;

import util.*;
import java.io.IOException;

public class Handler implements Runnable {

    static final int READING = 0, SENDING = 1, PROCESSING = 2;
    static int CLIENT_CONNECTED = 0, CLIENT_DISCONNECTED = 1, CLIENT_MESSAGE = 2;

    static ExecutorService pool = Executors.newFixedThreadPool(2);
    static ConnectionTable dicc = new ConnectionTable();

    final SocketChannel socketChannel;
    final SelectionKey selectionKey;

    ByteBuffer input = ByteBuffer.allocate(1024);
    int state = READING;
    boolean firstTime = true;
    String username = "";
    String message;

    Handler(Selector selector, SocketChannel c) throws IOException {
        socketChannel = c;
        c.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();

    }

    @Override
    public void run() {
        try {
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                sendManager(CLIENT_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void read() throws IOException {
        int readCount = socketChannel.read(input); // leo el mensaje
        if (readCount > 0) { // si tengo algo que leer, proceso
            state = PROCESSING;
            pool.execute(new Processer(readCount));
        }
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    class Processer implements Runnable {
        int readCount;

        Processer(int readCount) {
            this.readCount = readCount;
        }

        public void run() {
            processAndHandOff(readCount);
        }
    }

    synchronized void processAndHandOff(int readCount) {
        readProcess(readCount);
        state = SENDING;
    }

    synchronized void readProcess(int readCount) {
        StringBuilder sb = new StringBuilder();
        input.flip();
        byte[] subStringBytes = new byte[readCount];
        byte[] array = input.array();

        System.arraycopy(array, 0, subStringBytes, 0, readCount);
        sb.append(new String(subStringBytes));
        input.clear();

        if (firstTime) {
            username = sb.toString().trim();
            username = dicc.addParticipant(username, socketChannel); // updated v5
            System.out.println(username + " has just connected!");
            try {
                sendManager(CLIENT_CONNECTED);
            } catch (IOException e) {
                e.printStackTrace();
            }
            firstTime = false;
        } else {
            message = sb.toString().trim();
            if (message.contains(" has left")) {
                try {
                    sendManager(CLIENT_DISCONNECTED);
                    dicc.removeParticipant(username);
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    void sendManager(int type) throws IOException {
        String toOtherClients;
        if (type == CLIENT_CONNECTED) {                        
            send("[WELCOME CLIENT] Connected " + "\n[CLIENT LIST]:" + dicc.getUsernames() + "\n", socketChannel); // mensaje al cliente de bienvenido            
            broadcast_info(username + " ---> has joined!\n");
        } else if (type == CLIENT_DISCONNECTED) {                        
            broadcast_info(username + " ---> has left!\n");
        } else if (type == CLIENT_MESSAGE) {            
            broadcast(">>" + username +": " + message + "\n");
        }

        selectionKey.interestOps(SelectionKey.OP_READ);
        state = READING;
    }

    void send(String text, SocketChannel s) throws IOException {
        ByteBuffer output = ByteBuffer.wrap((text).getBytes());
        s.write(output);
    }

    public void broadcast_info(String message) throws IOException {
        int i;
        for (i = 0; i < dicc.getUsernames().size(); i++) {
            if (dicc.getUsernames().get(i) != username) {
                send(message, dicc.getSocketChannel((String) dicc.getUsernames().get(i)));
            }
        }
    }

    public void broadcast(String content) throws IOException {
        int i;
        for (i = 0; i < dicc.getUsernames().size(); i++) {
            if (dicc.getUsernames().get(i) != username && message != null) {
                send(content, dicc.getSocketChannel((String) dicc.getUsernames().get(i)));
            }
        }
    }

}
