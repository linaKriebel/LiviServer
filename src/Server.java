import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket server;
    private List<Player> players;
    private World world;


    public Server(int port) {
        players = new ArrayList<>();

        try {
            server = new ServerSocket(port);
            world = new World();
            ExecutorService executorService = Executors.newFixedThreadPool(200);
            while (true) {
                Player playerOne = new Player(this, server.accept(), 1);
                //Player playerTwo = new Player(this, server.accept(), 2);
                executorService.execute(playerOne);
                //executorService.execute(playerTwo);
                players.add(playerOne);
                //players.add(playerTwo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void handle(String message, int id) {
        Field coordinate = world.processMove(id, message);

        for (Player player : players) {
            try {
                //decide what to do
                player.sendMessage(id, coordinate);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
