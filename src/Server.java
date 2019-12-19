import models.Command;
import models.Field;
import models.GameEvent;
import models.GameItem;

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
        world = new World();

        try {
            server = new ServerSocket(port);

            ExecutorService executorService = Executors.newFixedThreadPool(200);
            while (true) {

                //Ai
                AI ai = new AI(this);
                executorService.execute(ai);

                //players
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
        List<GameEvent> events = world.processMove(id, message);
        String messageToSent;
        for (Player player : players) {
            try {
                for (GameEvent event : events) {
                    if (event.getCommand() == Command.MOVE) {
                        messageToSent = event.getCommand() + " " + event.getItemType() + " " + event.getItemId() + " " + event.getField().x + " " + event.getField().y;
                    } else {
                        messageToSent = event.getCommand() + " " + event.getItemType() + " " + event.getItemId();
                    }
                    player.sendMessage(messageToSent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public World getWorld() {
        return world;
    }

}
