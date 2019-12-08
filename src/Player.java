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

    public void sendMessage(int id, Field coordinate) throws IOException {
        //Send response back to the client
        String send = "MOVE PLAYER " + id + " " + coordinate.x + " " + coordinate.y;
        bw.write(send + "\n");
        bw.flush();
    }

    public Socket getSocket() {
        return socket;
    }
}
