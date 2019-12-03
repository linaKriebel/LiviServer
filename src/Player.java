import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class Player implements Runnable {

    private Socket socket;
    private Server server;
    private int id;
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
               // System.out.println(receivedMessage);
            } catch (Exception e) {
                logger.info("Player" + id + " disconnected.");
                break;
            }
        }
    }

    public void sendMessage(String sendMessage) throws IOException {
        //Send response back to the client
        bw.write(sendMessage + "\n");
        bw.flush();
    }

    public Socket getSocket() {
        return socket;
    }
}
