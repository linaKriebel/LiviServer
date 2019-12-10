import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
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
        HashMap<GameItem, Field> coordinates = world.processMove(id, message);
        List gameItems = new ArrayList(coordinates.keySet());
        for (Player player : players) {
            try {
                //TODO: check how this can be done more generic, without being dependent on indices
                if (gameItems.size() < 2 ) {
                    Object playerInfo = gameItems.get(0);
                    player.sendMessage(coordinates.get(playerInfo), (GameItem) playerInfo);
                } else {
                    Object ballInfo = gameItems.get(0);
                    player.sendMessage(coordinates.get(ballInfo), (GameItem) ballInfo);

                    Object playerInfo = gameItems.get(1);
                    player.sendMessage(coordinates.get(playerInfo), (GameItem) playerInfo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
