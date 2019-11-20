import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket server;
    private List<Player> players;


    public Server(int port){
        players = new ArrayList<>();

        try {
            server = new ServerSocket(port);
            while(true) {
            Player player = new Player(server.accept(), 1);

            //receive message from client
            InputStream is = player.getSocket().getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String receivedMessage = br.readLine();
            System.out.println("Message received from client is " + receivedMessage);

            //Send response back to the client
            String sendMessage = "Hello Client" + "\n";
            OutputStream os = player.getSocket().getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(sendMessage);
            System.out.println("Message sent to the client is " + sendMessage);
            bw.flush();

            players.add(player);
            System.out.println("connected");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
