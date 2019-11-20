import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket server;
    private List<Player> players;


    public Server(int port) {
        players = new ArrayList<>();

        try {
            server = new ServerSocket(port);
            ExecutorService executorService = Executors.newFixedThreadPool(200);
            while (true) {
                executorService.execute(new Player(server.accept(), 1));
                executorService.execute(new Player(server.accept(), 2));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
