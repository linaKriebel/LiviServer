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
                Player playerOne = new Player(this, server.accept(), 1);
                Player playerTwo = new Player(this, server.accept(), 2);
                executorService.execute(playerOne);
                executorService.execute(playerTwo);
                players.add(playerOne);
                players.add(playerTwo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void handle(String message, int id) {
        for (Player player : players) {
            try {
                player.sendMessage(id + " " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
