import models.Field;
import models.GameItem;
import models.ItemType;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class Player implements Runnable {

    private int id;
    private Field position;

    private Socket socket;
    private Server server;
    private OutputStream os;
    private OutputStreamWriter osw;
    private BufferedWriter bw;
    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    public Player(Server server, Socket socket, int id) throws IOException {
        this.server = server;
        this.socket = socket;
        this.id = id;
        os = socket.getOutputStream();
        osw = new OutputStreamWriter(os);
        bw = new BufferedWriter(osw);
    }

    @Override
    public void run() {
        //receive message from client
        while (true) {
            InputStream is = null;
            try {
                is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String receivedMessage = br.readLine();
                server.handle(receivedMessage, id);
                System.out.println(receivedMessage);
            } catch (IOException e) {
                logger.info("Player" + id + " disconnected.");

            }
        }
    }

    public void sendMessage(String message) throws IOException {
        //Send response back to the client
        System.out.println(message);
        bw.write(message + "\n");
        bw.flush();
    }

    public Socket getSocket() {
        return socket;
    }
}
