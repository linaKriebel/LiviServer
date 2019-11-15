import java.io.IOException;
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
            for (int i = 0; i<=2; i++) { //max 3 clients can connect to server
                Player player = new Player(server.accept(), i);
                System.out.println("connected");
                int message = player.getSocket().getInputStream().read();
                player.getSocket().getOutputStream().write(2*message);

                players.add(player);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start(){

    }
}
