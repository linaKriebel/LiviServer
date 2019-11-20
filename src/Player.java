import java.io.*;
import java.net.Socket;

public class Player implements Runnable {

    private Socket socket;
    private int id;

    public Player(Socket socket, int id){
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run() {
    }

    public Socket getSocket() {
        return socket;
    }
}
