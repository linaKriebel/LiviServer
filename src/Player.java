import models.*;

import java.io.*;
import java.net.Socket;

/**
 * a registered player that handles communication from server to client
 */
public class Player implements Runnable {

    private int id;
    private Socket socket;
    private Server server;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public Player(Server server, Socket socket, int id) throws IOException {
        this.server = server;
        this.socket = socket;
        this.id = id;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    public Player(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        //receive message from client
        while (true) {
            ClientCommand command = null;
            try {
                command = (ClientCommand) objectInputStream.readObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
            server.handle(command, id);
            System.out.println("Message from client " + id + ": " + command);
        }
    }

    public void sendMessage(GameEvent message) throws IOException {
        //Send response back to the client
        objectOutputStream.writeObject(message);
    }

    public Socket getSocket() {
        return socket;
    }

    public int getId() {
        return id;
    }
}
